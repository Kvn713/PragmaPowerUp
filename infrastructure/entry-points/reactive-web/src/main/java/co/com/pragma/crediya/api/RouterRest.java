package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.dto.ResponseUserDTO;
import co.com.pragma.crediya.api.utils.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration

public class RouterRest {
    @Bean
    @RouterOperation(
            path = "/api/v1/usuarios",
            method = RequestMethod.POST,
            beanClass = Handler.class,
            beanMethod = "saveUser",
            operation = @Operation(
                    operationId = "saveUser",
                    summary = "Crear un nuevo usuario",
                    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Datos del usuario a crear",
                            required = true,
                            content = @Content(schema = @Schema(implementation = CreateUserDTO.class))
                    ),
                    responses = {
                            @ApiResponse(responseCode = "200",
                                    description = "Usuario creado exitosamente",
                                    content = @Content(schema = @Schema(implementation = ResponseUserDTO.class))),
                            @ApiResponse(responseCode = "400",
                                    description = "Datos inválidos",
                                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.class),
                                            examples = @ExampleObject(
                                                    name = "Bad request",
                                                    value = "Bad request - nombres is empty"
                                            )))
                    }
            )
    )
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::saveUser);
    }
}
