package br.com.cgkrl.exceptions;

import org.jboss.logging.Logger;

public class ConstraintViolationHandler {

    String message;
    Boolean isPresent = false;

    public ConstraintViolationHandler(Exception e){
        Logger LOGGER = Logger.getLogger(ConstraintViolationHandler.class);
        StringBuilder sbException = new StringBuilder();
        Throwable exception = e;
        while (exception.getCause() != null) {
            sbException.append(exception.getCause().getClass().getName())
                    .append(" - ")
                    .append(exception.getCause().getMessage())
                    .append("\n");
            exception = exception.getCause();
        }
        LOGGER.error(sbException.toString());
        if (sbException.indexOf("Referential integrity constraint violation") > 0){
            message = sbException.toString();
            isPresent = true;
        }        
    }

    public String message(){
        return this.message;
    }

    public Boolean isPresent(){
        return this.isPresent;
    }
}
