package br.com.cgkrl.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.oracle.svm.core.annotate.Inject;

import br.com.cgkrl.dto.PlaylistDTO;
import br.com.cgkrl.exceptions.AlreadyExistsException;
import br.com.cgkrl.exceptions.NotFoundException;
import br.com.cgkrl.models.Musica;
import br.com.cgkrl.models.Playlist;
import br.com.cgkrl.models.Usuario;
import br.com.cgkrl.repositories.PlaylistRepository;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class PlaylistService {

    PlaylistRepository playlistRepository;

    @Inject
    UsuarioService usuarioService;

    @Inject
    MusicaService musicaService;

    public List<Playlist> all() {
        return playlistRepository.listAll();
    }

    public Playlist findByUid(String uid) throws NotFoundException {
        return Optional.ofNullable(playlistRepository.findByUid(uid))
                .orElseThrow(() -> new NotFoundException("Playlist " + uid + " não foi encontrado"));
    }

    public List<Playlist> findByUsuario(String usuarioUid) {
        return playlistRepository.findByUsuario(usuarioUid);
    }

    public Playlist findByNomeAndUsuario(String nome, String usuarioUid) throws NotFoundException {
        Usuario usuario = usuarioService.findByUid(usuarioUid);
        return playlistRepository.findByNomeAndUsuario(nome, usuario);
    }

    @Transactional
    public Playlist create(PlaylistDTO playlistDTO) throws NotFoundException, AlreadyExistsException {
        // A linha abaixo foi adicionada para facilitar a inclusão de dados de testes no
        // DBConfig.java, mantendo o UID definido nele.
        String uid = (playlistDTO.getUid() != null ? playlistDTO.getUid() : UUID.randomUUID().toString());
        if (findByNomeAndUsuario(playlistDTO.getNome(), playlistDTO.getUsuario().getUid()) != null) {
            throw new AlreadyExistsException("Usuario " + playlistDTO.getUsuario().getUid() +
                    " já possui uma playlist com o nome " + playlistDTO.getNome());
        }
        Playlist playlist = Playlist.builder()
                .nome(playlistDTO.getNome())
                .usuario(usuarioService.findByUid(playlistDTO.getUsuario().getUid()))
                .uid(uid)
                .build();
        playlistRepository.persist(playlist);
        playlist = playlistRepository.findByUid(playlist.getUid());
        return playlist;
    }

    @Transactional
    public Playlist update(String uid, PlaylistDTO playlistDTO)
            throws AlreadyExistsException, NotFoundException {
        Playlist playlistOriginal = playlistRepository.findByUid(uid);
        if (playlistOriginal == null) {
            throw new NotFoundException("Playlist " + uid + " não foi encontrado.");
        }

        Playlist playlistWithNomeAndUsusario = findByNomeAndUsuario(playlistDTO.getNome(),
                playlistOriginal.getUsuario().getUid());
        if (playlistWithNomeAndUsusario != null) {
            if (!uid.equals(playlistWithNomeAndUsusario.getUid())) {
                throw new AlreadyExistsException("Playlist " + playlistDTO.getNome() + " já existe");
            }
        }
        playlistOriginal.setNome(playlistDTO.getNome());
        playlistRepository.persistAndFlush(playlistOriginal);
        return playlistRepository.findByUid(uid);
    }

    @Transactional
    public String delete(String uid) throws NotFoundException {
        Playlist playlistOriginal = playlistRepository.findByUid(uid);
        if (playlistOriginal == null) {
            throw new NotFoundException("Playlist " + uid + " não foi encontrado.");
        }
        playlistRepository.delete(playlistOriginal);
        return "Playlist " + uid + " foi excluído com sucesso";
    }

    @Transactional
    public Playlist addMusica(String musicaUid, String playlistUid)
            throws NotFoundException, AlreadyExistsException {
        Musica musica = musicaService.findByUid(musicaUid);
        Playlist playlist = findByUid(playlistUid);
        List<Musica> musicas = playlist.getMusicas();
        if (musicas == null) {
            musicas = new ArrayList<>();
        }
        if (!musicas.contains(musica)) {
            musicas.add(musica);
            playlist.setMusicas(musicas);
            playlistRepository.persistAndFlush(playlist);
            Playlist playlistAtualizada = playlistRepository.findByUid(playlist.getUid());
            return playlistAtualizada;
        } else {
            throw new AlreadyExistsException(
                    "Musica " + musica.getNome() + " já está adicionada na playlist " + playlist.getNome());
        }
    }

    @Transactional
    public Playlist removeMusica(String musicaUid, String playlistUid)
            throws NotFoundException {
        Musica musica = musicaService.findByUid(musicaUid);
        Playlist playlist = findByUid(playlistUid);
        if (playlist.getMusicas().contains(musica)) {
            playlist.getMusicas().remove(musica);
            playlistRepository.persist(playlist);
            return playlistRepository.findByUid(playlist.getUid());
        } else {
            throw new NotFoundException("Musica " + musica.getNome() +
                    " não está na playlist " + playlist.getNome());
        }
    }

}
