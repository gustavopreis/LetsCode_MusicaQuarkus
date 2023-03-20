package br.com.cgkrl.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

public class ControllerExceptionResponseHandler {

    private static final Logger LOGGER = Logger.getLogger(ControllerExceptionResponseHandler.class);

    public static Response response(Exception e, Status status, Logger.Level loggerLevel) {
        return response(e, status, loggerLevel, "");
    }

    public static Response response(Exception e, Status status, Logger.Level loggerLevel, String customMessage) {
        switch (loggerLevel.name()) {
            case "FATAL":
                LOGGER.fatal(e.getMessage());
                break;
            case "ERROR":
                LOGGER.error(e.getMessage());
                break;
            case "WARN":
                LOGGER.warn(e.getMessage());
                break;
            case "INFO":
                LOGGER.info(e.getMessage());
                break;
            case "DEBUG":
                LOGGER.debug(e.getMessage());
                break;
            case "TRACE":
                LOGGER.trace(e.getMessage());
                break;
        }
        String message = "";
        message = (!customMessage.isEmpty() ? customMessage + "\n" + e.getMessage() : e.getMessage());

        ConstraintViolationHandler constraintViolationVerify = new ConstraintViolationHandler(e);
        if (constraintViolationVerify.isPresent()) {
            message = "Erro ao realizar exclusão. O objeto possuí dados relacionados a ele. \n\n"
                    + constraintViolationVerify.message();
        }
        return Response.status(status)
                .entity(message)
                .build();
    }

}
