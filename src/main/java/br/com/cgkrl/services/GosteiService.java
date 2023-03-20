package br.com.cgkrl.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.oracle.svm.core.annotate.Inject;

import br.com.cgkrl.dto.GosteiDTO;
import br.com.cgkrl.exceptions.AlreadyExistsException;
import br.com.cgkrl.exceptions.NotFoundException;
import br.com.cgkrl.models.Gostei;
import br.com.cgkrl.models.Musica;
import br.com.cgkrl.models.Usuario;
import br.com.cgkrl.repositories.GosteiRepository;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class GosteiService {

    GosteiRepository gosteiRepository;

    @Inject
    UsuarioService usuarioService;

    @Inject
    MusicaService musicaService;

    public List<Gostei> all() {
        return gosteiRepository.listAll();
    }

    public Gostei findByUid(String userUid) throws NotFoundException {
        return Optional.ofNullable(gosteiRepository.findByUid(userUid))
                .orElseThrow(() -> new NotFoundException("Gostei " + userUid + " não foi encontrado"));
    }

    public List<Gostei> findByUsuario(String usuarioUid) {
        return gosteiRepository.findByUsuario(usuarioUid);
    }

    public List<Gostei> findByUsuarioAndMusica(String usuarioUid, String musicaUid) throws NotFoundException {
        Usuario usuario = usuarioService.findByUid(usuarioUid);
        Musica musica = musicaService.findByUid(musicaUid);
        return Optional.ofNullable(gosteiRepository.findByUsuarioAndMusica(usuario, musica))
                .orElseThrow(() -> new NotFoundException("Gostei do " + usuario.getNome() + " não foi encontrado"));
    }

    @Transactional
    public Gostei create(GosteiDTO gosteiDTO) throws NotFoundException, AlreadyExistsException {
        // A linha abaixo foi adicionada para facilitar a inclusão de dados de testes no
        // DBConfig.java, mantendo o UID definido nele.
        String uid = (gosteiDTO.getUid() != null ? gosteiDTO.getUid() : UUID.randomUUID().toString());

        Usuario usuario = usuarioService.findByUid(gosteiDTO.getUsuario().getUid());
        Musica musica = musicaService.findByUid(gosteiDTO.getMusica().getUid());

        Optional<Gostei> gosteiDB = findByUsuarioAndMusica(
                usuario.getUid(),
                musica.getUid()).stream()
                .findFirst();

        if (!gosteiDB.isPresent()) {
            Gostei gostei = Gostei.builder()
                    .musica(musicaService.findByUid(gosteiDTO.getMusica().getUid()))
                    .usuario(usuarioService.findByUid(gosteiDTO.getUsuario().getUid()))
                    .uid(uid)
                    .build();
            gosteiRepository.persist(gostei);
            return gosteiRepository.findByUid(gostei.getUid());
        } else {
            throw new AlreadyExistsException("Gostei '" + gosteiDB.get().getUsuario().getNome() + " -> "
                    + gosteiDB.get().getMusica().getNome() + "' já existe");
        }
    }

    @Transactional
    public String delete(String uid) throws NotFoundException {
        Gostei gosteiOriginal = gosteiRepository.findByUid(uid);
        if (gosteiOriginal == null) {
            throw new NotFoundException("Gostei " + uid + " não foi encontrado.");
        }
        gosteiRepository.delete(gosteiOriginal);
        return "Gostei " + uid + " foi excluído com sucesso";
    }
}
