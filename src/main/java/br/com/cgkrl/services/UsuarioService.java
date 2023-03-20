package br.com.cgkrl.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.cgkrl.dto.UsuarioDTO;
import br.com.cgkrl.exceptions.AlreadyExistsException;
import br.com.cgkrl.exceptions.NotFoundException;
import br.com.cgkrl.models.Usuario;
import br.com.cgkrl.repositories.UsuarioRepository;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class UsuarioService {
    UsuarioRepository usuarioRepository;

    @Inject
    PlaylistService playlistService;

    public List<Usuario> all() {
        return usuarioRepository.listAll();
    }

    public Usuario findByUid(String uid) throws NotFoundException {
        return Optional.ofNullable(usuarioRepository.findByUid(uid))
                .orElseThrow(() -> new NotFoundException("Usuario " + uid + " não foi encontrado"));
    }

    public Usuario findByNome(String nome) {
        return usuarioRepository.findByNome(nome);
    }

    @Transactional
    public Usuario create(UsuarioDTO usuarioDTO) throws AlreadyExistsException {
        // A linha abaixo foi adicionada para facilitar a inclusão de dados de testes no
        // DBConfig.java, mantendo o UID definido nele.
        String uid = (usuarioDTO.getUid() != null ? usuarioDTO.getUid() : UUID.randomUUID().toString());

        if (findByNome(usuarioDTO.getNome()) == null) {
            Usuario usuario = Usuario.builder()
                    .nome(usuarioDTO.getNome())
                    .uid(uid)
                    .build();
            usuarioRepository.persist(usuario);
            return usuarioRepository.findByUid(usuario.getUid());
        } else {
            throw new AlreadyExistsException("Usuario " + usuarioDTO.getNome() + " já existe");
        }
    }

    @Transactional
    public Usuario update(String uid, UsuarioDTO usuarioDTO)
            throws AlreadyExistsException, NotFoundException {
        Usuario usuarioOriginal = usuarioRepository.findByUid(uid);
        if (usuarioOriginal == null) {
            throw new NotFoundException("Usuario " + uid + " não foi encontrado.");
        } else {
            Usuario usuarioWithNome = findByNome(usuarioDTO.getNome());
            if (usuarioWithNome != null) {
                if (!uid.equals(usuarioWithNome.getUid())) {
                    throw new AlreadyExistsException(
                            "Usuario " + usuarioDTO.getNome() + " já existe");
                }
            }
            usuarioOriginal.setNome(usuarioDTO.getNome());
            usuarioRepository.persist(usuarioOriginal);
        }
        return usuarioRepository.findByUid(uid);
    }

    @Transactional
    public String delete(String uid) throws NotFoundException {
        Usuario usuarioOriginal = usuarioRepository.findByUid(uid);
        if (usuarioOriginal == null) {
            throw new NotFoundException("Usuario " + uid + " não foi encontrado.");
        }
        usuarioRepository.delete(usuarioOriginal);
        return "Usuario " + uid + " foi excluído com sucesso";
    }
}
