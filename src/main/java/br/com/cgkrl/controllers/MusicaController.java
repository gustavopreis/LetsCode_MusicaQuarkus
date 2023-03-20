package br.com.cgkrl.controllers;

import java.util.stream.Collectors;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.logging.Logger;

import br.com.cgkrl.dto.FactoryDTO;
import br.com.cgkrl.dto.MusicaDTO;
import br.com.cgkrl.exceptions.AlreadyExistsException;
import br.com.cgkrl.exceptions.ControllerExceptionResponseHandler;
import br.com.cgkrl.exceptions.NotFoundException;
import br.com.cgkrl.services.MusicaService;
import io.quarkus.arc.ArcUndeclaredThrowableException;
import lombok.AllArgsConstructor;

@Path("/musica")
@AllArgsConstructor
public class MusicaController {

    private MusicaService musicaService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obter todos os Musica")
    @APIResponses({
            @APIResponse(responseCode = "200", description = ApiResponse.OK),
            @APIResponse(responseCode = "500", description = ApiResponse.INTERNAL_SERVER_ERROR),
    })
    public Response list() {
        return Response.status(Status.OK)
                .entity(musicaService.all().stream()
                        .map(FactoryDTO::entityToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{uid}")
    @Operation(summary = "Obter Musica pelo Uid")
    @Parameter(name = "uid", description = "Uid da Musica")
    @APIResponses({
            @APIResponse(responseCode = "200", description = ApiResponse.OK),
            @APIResponse(responseCode = "404", description = ApiResponse.NOT_FOUND),
            @APIResponse(responseCode = "500", description = ApiResponse.INTERNAL_SERVER_ERROR),
    })
    public Response findByUid(@PathParam("uid") String uid) {
        try {
            return Response.status(Status.OK).entity(
                    FactoryDTO.entityToDTO(musicaService.findByUid(uid)))
                    .build();
        } catch (NotFoundException e) {
            return ControllerExceptionResponseHandler.response(e, Status.OK, Logger.Level.WARN);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("playlist/{playlistUid}")
    @Operation(summary = "Obter Musica pelo Uid")
    @APIResponses({
            @APIResponse(responseCode = "200", description = ApiResponse.OK),
            @APIResponse(responseCode = "500", description = ApiResponse.INTERNAL_SERVER_ERROR),
    })
    public Response findByPlaylistUid(@PathParam("playlistUid") String playlistUid) {
        return Response.status(Status.OK)
                .entity(musicaService.findByPlaylistUid(playlistUid).stream()
                        .map(FactoryDTO::entityToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Criar Musica")
    @APIResponses({
            @APIResponse(responseCode = "200", description = ApiResponse.OK),
            @APIResponse(responseCode = "404", description = ApiResponse.NOT_FOUND),
            @APIResponse(responseCode = "409", description = ApiResponse.CONFLICT),
            @APIResponse(responseCode = "500", description = ApiResponse.INTERNAL_SERVER_ERROR),
    })
    public Response create(
        @RequestBody(
            content = @Content(mediaType = "application/json", 
            schema = @Schema(example = "{"+
                "\"nome\": \"string\","+
                "\"autor\": \"string\","+
                "\"duracao\": 0,"+
                "\"generoMusical\": {\"uid\": \"string\"}"+
            "}")))         
        MusicaDTO musicaDTO) {
        try {
            return Response.status(Status.CREATED).entity(
                    FactoryDTO.entityToDTO(
                            musicaService.create(musicaDTO)))
                    .build();
        } catch (NotFoundException e) {
            return ControllerExceptionResponseHandler.response(e, Status.NOT_FOUND, Logger.Level.WARN);
        } catch (AlreadyExistsException e) {
            return ControllerExceptionResponseHandler.response(e, Status.CONFLICT, Logger.Level.ERROR);
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{uid}")
    @Operation(summary = "Atualizar Musica")
    @Parameter(name = "uid", description = "Uid da Musica")
    @APIResponses({
            @APIResponse(responseCode = "200", description = ApiResponse.OK),
            @APIResponse(responseCode = "404", description = ApiResponse.NOT_FOUND),
            @APIResponse(responseCode = "409", description = ApiResponse.CONFLICT),
            @APIResponse(responseCode = "500", description = ApiResponse.INTERNAL_SERVER_ERROR),
    })
    public Response update(
        @PathParam("uid") String uid, 
        @RequestBody(
            content = @Content(mediaType = "application/json", 
            schema = @Schema(example = "{"+
                "\"nome\": \"string\","+
                "\"autor\": \"string\","+
                "\"duracao\": 0,"+
                "\"generoMusical\": {\"uid\": \"string\"}"+
            "}"))) 
            MusicaDTO musicaDTO) {
        try {
            return Response.status(Status.OK).entity(
                    FactoryDTO.entityToDTO(
                            musicaService.update(uid, musicaDTO)))
                    .build();
        } catch (NotFoundException e) {
            return ControllerExceptionResponseHandler.response(e, Status.NOT_FOUND, Logger.Level.WARN);
        } catch (AlreadyExistsException e) {
            return ControllerExceptionResponseHandler.response(e, Status.CONFLICT, Logger.Level.ERROR);
        }
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{uid}")
    @Operation(summary = "Excluir Musica")
    @Parameter(name = "uid", description = "Uid do Gostei")
    @APIResponses({
            @APIResponse(responseCode = "200", description = ApiResponse.OK),
            @APIResponse(responseCode = "404", description = ApiResponse.NOT_FOUND),
            @APIResponse(responseCode = "409", description = ApiResponse.CONFLICT),
            @APIResponse(responseCode = "500", description = ApiResponse.INTERNAL_SERVER_ERROR),
    })
    public Response delete(@PathParam("uid") String uid) {
        try {
            return Response.status(Status.OK).entity(musicaService.delete(uid)).build();
        } catch (NotFoundException e) {
            return ControllerExceptionResponseHandler.response(e, Status.NOT_FOUND, Logger.Level.WARN);
        } catch (ArcUndeclaredThrowableException e) {
            return ControllerExceptionResponseHandler.response(e, Status.CONFLICT, Logger.Level.ERROR);
        }
    }
}
