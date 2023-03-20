package br.com.cgkrl.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import br.com.cgkrl.dto.GeneroMusicalDTO;
import br.com.cgkrl.exceptions.AlreadyExistsException;
import br.com.cgkrl.exceptions.NotFoundException;
import br.com.cgkrl.models.GeneroMusical;
import br.com.cgkrl.repositories.GeneroMusicalRepository;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class GeneroMusicalService {
    GeneroMusicalRepository generoMusicalRepository;

    public List<GeneroMusical> all() {
        return generoMusicalRepository.listAll();
    }

    public GeneroMusical findByUid(String uid) throws NotFoundException {
        return Optional.ofNullable(generoMusicalRepository.findByUid(uid))
                .orElseThrow(() -> new NotFoundException("GeneroMusical " + uid + " não foi encontrado"));
    }

    public GeneroMusical findByNome(String nome) {
        return generoMusicalRepository.findByNome(nome);
    }

    @Transactional
    public GeneroMusical create(GeneroMusicalDTO generoMusicalDTO) throws AlreadyExistsException {
        // A linha abaixo foi adicionada para facilitar a inclusão de dados de testes no
        // DBConfig.java, mantendo o UID definido nele.
        String uid = (generoMusicalDTO.getUid() != null ? generoMusicalDTO.getUid() : UUID.randomUUID().toString());
        if (findByNome(generoMusicalDTO.getNome()) == null) {
            GeneroMusical generoMusical = GeneroMusical.builder()
                    .nome(generoMusicalDTO.getNome())
                    .uid(uid)
                    .build();
            generoMusicalRepository.persist(generoMusical);
            return generoMusicalRepository.findByUid(generoMusical.getUid());
        } else {
            throw new AlreadyExistsException("GeneroMusical " + generoMusicalDTO.getNome() + " já existe");
        }
    }

    @Transactional
    public GeneroMusical update(String uid, GeneroMusicalDTO generoMusicalDTO)
            throws AlreadyExistsException, NotFoundException {
        GeneroMusical generoMusicalOriginal = generoMusicalRepository.findByUid(uid);
        if (generoMusicalOriginal == null) {
            throw new NotFoundException("GeneroMusical " + uid + " não foi encontrado.");
        } else {
            GeneroMusical generoMusicalWithNome = findByNome(generoMusicalDTO.getNome());
            if (generoMusicalWithNome != null) {
                if (!uid.equals(generoMusicalWithNome.getUid())) {
                    throw new AlreadyExistsException(
                            "GeneroMusical " + generoMusicalDTO.getNome() + " já existe");
                }
            }
            generoMusicalOriginal.setNome(generoMusicalDTO.getNome());
            generoMusicalRepository.persist(generoMusicalOriginal);
        }
        return generoMusicalRepository.findByUid(uid);
    }

    @Transactional
    public String delete(String uid) throws NotFoundException {
        GeneroMusical generoMusicalOriginal = generoMusicalRepository.findByUid(uid);
        if (generoMusicalOriginal == null) {
            throw new NotFoundException("GeneroMusical " + uid + " não foi encontrado.");
        }
        generoMusicalRepository.delete(generoMusicalOriginal);
        return "GeneroMusical " + uid + " foi excluído com sucesso";
    }
}
