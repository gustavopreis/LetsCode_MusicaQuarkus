package br.com.cgkrl.controllers;

import java.util.stream.Collectors;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import br.com.cgkrl.dto.GosteiDTO;
import br.com.cgkrl.exceptions.AlreadyExistsException;
import br.com.cgkrl.exceptions.ControllerExceptionResponseHandler;
import br.com.cgkrl.exceptions.NotFoundException;
import br.com.cgkrl.services.GosteiService;
import io.quarkus.arc.ArcUndeclaredThrowableException;
import lombok.AllArgsConstructor;

@Path("/gostei")
@AllArgsConstructor
public class GosteiController {

    private GosteiService gosteiService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/usuario/{usuarioUid}")
    @Operation(summary = "Obter Gostei pelo usuarioUid")
    @APIResponses({
            @APIResponse(responseCode = "200", description = ApiResponse.OK),
            @APIResponse(responseCode = "500", description = ApiResponse.INTERNAL_SERVER_ERROR),
    })
    public Response findByUsuarioUid(@PathParam("usuarioUid") String usuarioUid) {
        return Response.status(Status.OK)
                .entity(gosteiService.findByUsuario(usuarioUid).stream()
                        .map(FactoryDTO::entityToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Criar Gostei")
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
                    "\"usuario\": {\"uid\": \"UUID\"},"+
                    "\"musica\": {\"uid\": \"UUID\"}"+
                "}"))) GosteiDTO gosteiDTO) {
        try {

            return Response.status(Status.CREATED).entity(FactoryDTO.entityToDTO(
                    gosteiService.create(gosteiDTO)))
                    .build();
        } catch (NotFoundException e) {
            return ControllerExceptionResponseHandler.response(e, Status.NOT_FOUND, Logger.Level.WARN);
        } catch (AlreadyExistsException e) {
            return ControllerExceptionResponseHandler.response(e, Status.CONFLICT, Logger.Level.ERROR);
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{uid}")
    @Operation(summary = "Excluir Gostei")
    @Parameter(name = "uid", description = "Uid do Gostei")
    @APIResponses({
            @APIResponse(responseCode = "200", description = ApiResponse.OK),
            @APIResponse(responseCode = "404", description = ApiResponse.NOT_FOUND),
            @APIResponse(responseCode = "409", description = ApiResponse.CONFLICT),
            @APIResponse(responseCode = "500", description = ApiResponse.INTERNAL_SERVER_ERROR),
    })
    public Response delete(@PathParam("uid") String uid) {
        try {
            return Response.status(Status.OK).entity(gosteiService.delete(uid)).build();
        } catch (NotFoundException e) {
            return ControllerExceptionResponseHandler.response(e, Status.NOT_FOUND, Logger.Level.WARN);
        } catch (ArcUndeclaredThrowableException e) {
            return ControllerExceptionResponseHandler.response(e, Status.CONFLICT, Logger.Level.ERROR);
        }
    }
}