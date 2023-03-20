package br.com.cgkrl.configs;

import java.util.Random;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.cgkrl.dto.FactoryDTO;
import br.com.cgkrl.exceptions.AlreadyExistsException;
import br.com.cgkrl.exceptions.NotFoundException;
import br.com.cgkrl.models.GeneroMusical;
import br.com.cgkrl.models.Gostei;
import br.com.cgkrl.models.Musica;
import br.com.cgkrl.models.Playlist;
import br.com.cgkrl.models.Usuario;
import br.com.cgkrl.services.GeneroMusicalService;
import br.com.cgkrl.services.GosteiService;
import br.com.cgkrl.services.MusicaService;
import br.com.cgkrl.services.PlaylistService;
import br.com.cgkrl.services.UsuarioService;
import io.quarkus.runtime.StartupEvent;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class AppConfig {

        private GeneroMusicalService generoMusicalService;
        private MusicaService musicaService;
        private UsuarioService usuarioService;
        private PlaylistService playlistService;
        private GosteiService gosteiService;
        private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

        @Transactional
        @SuppressWarnings("unused")
        public void initApplication(@Observes StartupEvent event) {

                
                try {
                        // Aqui foram usandos UUIDs fixos a fim de facilitar a criação dos objetos
                        // iniciais e a realização de requests de teste.
                        // Entretanto, a criação de objetos a partir das requisições realizadas por meio
                        // dos Controllers, tem UUIDs randômicos.

                        LOGGER.info("----------------------------------------------");
                        LOGGER.info("Inicializando repositorios");
                        LOGGER.info("----------------------------------------------");

                        LOGGER.info("- Iniciando repositorio GeneroMusical");
                        GeneroMusical generoMusical1 = addGeneroMusical("83c1becc-2949-45d3-b78a-f2234e7dc145", "Rock");
                        GeneroMusical generoMusical2 = addGeneroMusical("38a27a9e-cf3c-4be1-9f1c-815a8fc952ab", "MPB");
                        GeneroMusical generoMusical3 = addGeneroMusical("782d7db1-d487-46e0-a507-5703d79444b3",
                                        "Eletronica");

                        LOGGER.info("- Iniciando repositorio Musica");
                        Musica musica1 = addMusica(generoMusical1, "42f1d2c2-51d2-4d3e-babe-1c16ef1cc7c9", "Yesterday", "The Beatles");
                        Musica musica2 = addMusica(generoMusical1, "bd4b4a16-8c4d-4b9c-9cf1-10b287d4b4a1", "Stairway to Heaven", "Led Zeppelin");
                        Musica musica3 = addMusica(generoMusical2, "7dcaac9a-05bb-4d52-8dd7-27e1f21a7f95", "Garota de Ipanema", "Tom Jobin");
                        Musica musica4 = addMusica(generoMusical2, "1d4f7f01-2a50-4a55-a20f-5a3ee3f0a48a", "Como nossos pais", "Belchior");

                        LOGGER.info("- Iniciando repositorio Usuario");
                        Usuario usuario1 = addUsuario("062425c5-ec13-486e-9d0a-4da7069a8475", "Calebe");
                        Usuario usuario2 = addUsuario("85b8d763-25fd-4ce0-8200-665dc881fb46", "Railson");
                        Usuario usuario3 = addUsuario("ec544b89-78aa-418e-b743-8f8600702826", "Lukas");

                        LOGGER.info("- Iniciando repositorio Playlist");
                        Playlist playlist1 = addPlaylist("f64d1bf3-3dc3-41f3-9d9e-89aa2c58cf09", "Super Top", usuario1);
                        Playlist playlist2 = addPlaylist("d49c5d8e-8125-432a-98f9-9d9f41c1e7b7", "Topzeiras", usuario1);
                        Playlist playlist3 = addPlaylist("cb4e68b2-5007-4890-ab67-69f151a59fb1", "Só o Ouro", usuario2);

                        LOGGER.info("- Adicionando música à playlist");
                        addMusicaToPlaylist(playlist1, new Musica[] { musica2, musica3 });
                        addMusicaToPlaylist(playlist3, new Musica[] { musica1, musica3 });

                        LOGGER.info("- Iniciando repositorio Gostei");
                        Gostei gostei1 = addGostei("6f665b6c-cda6-4d99-9f9e-fb874c9fb2c2", usuario1, musica1);
                        Gostei gostei2 = addGostei(UUID.randomUUID().toString(), usuario1, musica2);

                } catch (AlreadyExistsException | NotFoundException e) {
                        LOGGER.error("Erro ao iniciar repositórios \n" + e.getMessage(), e);
                }

                LOGGER.info("-------------------------------------------------");
                LOGGER.info("-------------------------------------------------");
                LOGGER.info("ENDEREÇOS ÚTEIS: ");
                LOGGER.info("-------------------------------------------------");
                LOGGER.info("- Swagger: http://localhost:8080/openapi/");
                LOGGER.info("- H2 Console: http://localhost:8080/h2-console/");
                LOGGER.info("-------------------------------------------------");
                LOGGER.info("-------------------------------------------------");
        }

        private GeneroMusical addGeneroMusical(String uid, String nome)
                        throws AlreadyExistsException, NotFoundException {
                generoMusicalService.create(FactoryDTO.entityToDTO(

                                GeneroMusical.builder()
                                                .uid(uid)
                                                .nome(nome)
                                                .build()));
                return generoMusicalService.findByUid(uid);
        }

        private Musica addMusica(GeneroMusical generoMusical, String uid, String nome, String autor)
                        throws AlreadyExistsException, NotFoundException {
                musicaService.create(FactoryDTO.entityToDTO(
                                Musica.builder()
                                                .uid(uid)
                                                .nome(nome)
                                                .generoMusical(generoMusical)
                                                .autor(autor)
                                                .duracao(Long.valueOf(new Random().nextInt(5000) + 2000))
                                                .build()));
                return musicaService.findByUid(uid);
        }

        private Usuario addUsuario(String uid, String nome)
                        throws AlreadyExistsException, NotFoundException {
                usuarioService.create(FactoryDTO.entityToDTO(
                                Usuario.builder()
                                                .uid(uid)
                                                .nome(nome)
                                                .build()));
                return usuarioService.findByUid(uid);
        }

        private Playlist addPlaylist(String uid, String nome, Usuario usuario)
                        throws AlreadyExistsException, NotFoundException {
                Playlist playlist = Playlist.builder()
                                .uid(uid)
                                .nome(nome)
                                .usuario(usuario)
                                .build();
                playlistService.create(FactoryDTO.entityToDTO(playlist));
                return playlistService.findByUid(uid);
        }

        private Gostei addGostei(String uid, Usuario usuario, Musica musica)
                        throws AlreadyExistsException, NotFoundException {
                gosteiService.create(FactoryDTO.entityToDTO(
                                Gostei.builder()
                                                .uid(uid)
                                                .usuario(usuario)
                                                .musica(musica)
                                                .build()));
                return gosteiService.findByUid(uid);
        }

        public void addMusicaToPlaylist(Playlist playlist, Musica[] musicas)
                        throws NotFoundException, AlreadyExistsException {
                for (Musica musica : musicas) {
                        playlistService.addMusica(musica.getUid(), playlist.getUid());
                }
        }
}
