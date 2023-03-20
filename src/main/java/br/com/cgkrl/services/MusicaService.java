package br.com.cgkrl.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.oracle.svm.core.annotate.Inject;

import br.com.cgkrl.dto.MusicaDTO;
import br.com.cgkrl.exceptions.AlreadyExistsException;
import br.com.cgkrl.exceptions.NotFoundException;
import br.com.cgkrl.models.GeneroMusical;
import br.com.cgkrl.models.Musica;
import br.com.cgkrl.repositories.MusicaRepository;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class MusicaService {

    MusicaRepository musicaRepository;

    @Inject
    GeneroMusicalService generoMusicalService;

    @Inject
    PlaylistService playlistService;

    public List<Musica> all() {
        return musicaRepository.listAll();
    }

    public Musica findByUid(String uid) throws NotFoundException {
        return Optional.ofNullable(musicaRepository.findByUid(uid))
                .orElseThrow(() -> new NotFoundException("Musica " + uid + " não foi encontrado"));
    }

    public Musica findByNome(String nome) {
        return musicaRepository.findByNome(nome);
    }

    public List<Musica> findByPlaylistUid(String playlistUid) {
        return musicaRepository.findByPlaylistUid(playlistUid);
    }

    @Transactional
    public Musica create(MusicaDTO musicaDTO) throws AlreadyExistsException, NotFoundException {
        // A linha abaixo foi adicionada para facilitar a inclusão de dados de testes no
        // DBConfig.java, mantendo o UID definido nele.
        String uid = (musicaDTO.getUid() != null ? musicaDTO.getUid() : UUID.randomUUID().toString());

        if (findByNome(musicaDTO.getNome()) == null) {
            GeneroMusical generoMusical;
            try {
                generoMusical = generoMusicalService.findByUid(musicaDTO.getGeneroMusical().getUid());
            } catch (NotFoundException e) {
                throw new NotFoundException("GeneroMusical " + musicaDTO.getUid() + " não foi encontrado");
            }
            Musica musica = Musica.builder()
                    .autor(musicaDTO.getAutor())
                    .duracao(musicaDTO.getDuracao())
                    .generoMusical(generoMusical)
                    .nome(musicaDTO.getNome())
                    .uid(uid)
                    .build();
            musicaRepository.persist(musica);
            return musicaRepository.findByUid(musica.getUid());
        } else {
            throw new AlreadyExistsException("Musica " + musicaDTO.getNome() + " já existe");
        }
    }

    @Transactional
    public Musica update(String uid, MusicaDTO musicaDTO)
            throws AlreadyExistsException, NotFoundException {
        Musica musicaOriginal = musicaRepository.findByUid(uid);
        if (musicaOriginal == null) {
            throw new NotFoundException("Musica " + uid + " não foi encontrado.");
        } else {
            Musica musicaWithNome = findByNome(musicaDTO.getNome());
            if (musicaWithNome != null) {
                if (!uid.equals(musicaWithNome.getUid())) {
                    throw new AlreadyExistsException(
                            "Musica " + musicaDTO.getNome() + " já existe");
                }
            }
            GeneroMusical generoMusical;
            try {
                generoMusical = generoMusicalService.findByUid(musicaDTO.getGeneroMusical().getUid());
            } catch (NotFoundException e) {
                throw new NotFoundException(
                        "GeneroMusical " + musicaDTO.getGeneroMusical().getUid() + " não foi encontrado");
            }
            musicaOriginal.setAutor(musicaDTO.getAutor());
            musicaOriginal.setDuracao(musicaDTO.getDuracao());
            musicaOriginal.setGeneroMusical(generoMusical);
            musicaOriginal.setNome(musicaDTO.getNome());
            musicaRepository.persist(musicaOriginal);
        }
        return musicaRepository.findByUid(uid);
    }

    @Transactional
    public String delete(String uid) throws NotFoundException {
        Musica musicaOriginal = musicaRepository.findByUid(uid);
        if (musicaOriginal == null) {
            throw new NotFoundException("Musica " + uid + " não foi encontrado.");
        }
        musicaRepository.delete(musicaOriginal);
        return "Musica " + uid + " foi excluído com sucesso";
    }
}
