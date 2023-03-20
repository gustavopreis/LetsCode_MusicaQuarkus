package br.com.cgkrl;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@ApplicationPath("/")
@OpenAPIDefinition(
        info = @Info(
                title= "Musicas API",
                version = "1.0.0"
        )
)


public class APIDefinition extends Application {
    
}
