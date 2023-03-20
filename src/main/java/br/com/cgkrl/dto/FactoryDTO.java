package br.com.cgkrl.dto;

import java.util.stream.Collectors;

import br.com.cgkrl.models.GeneroMusical;
import br.com.cgkrl.models.Gostei;
import br.com.cgkrl.models.Musica;
import br.com.cgkrl.models.Playlist;
import br.com.cgkrl.models.Usuario;

public class FactoryDTO {

    public static GeneroMusicalDTO entityToDTO(GeneroMusical generoMusical) {
        return GeneroMusicalDTO.builder()
                .uid(generoMusical.getUid())
                .nome(generoMusical.getNome())
                .build();
    }

    public static MusicaDTO entityToDTO(Musica musica) {
        return MusicaDTO.builder()
                .uid(musica.getUid())
                .nome(musica.getNome())
                .generoMusical(entityToDTO(musica.getGeneroMusical()))
                .autor(musica.getAutor())
                .duracao(musica.getDuracao())
                .build();
    }

    public static UsuarioDTO entityToDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .uid(usuario.getUid())
                .nome(usuario.getNome())
                .build();
    }

    public static GosteiDTO entityToDTO(Gostei gostei) {
        return GosteiDTO.builder()
                .musica(entityToDTO(gostei.getMusica()))
                .uid(gostei.getUid())
                .usuario(entityToDTO(gostei.getUsuario()))
                .build();
    }

    public static PlaylistDTO entityToDTO(Playlist playlist) {
        return PlaylistDTO.builder()
                .uid(playlist.getUid())
                .nome(playlist.getNome())
                .usuario(entityToDTO(playlist.getUsuario()))
                .musicas(playlist.getMusicas().stream().map(m -> entityToDTO(m)).collect(Collectors.toList()))
                .build();
    }
}
