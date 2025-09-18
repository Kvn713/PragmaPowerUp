package co.com.pragma.crediya.api.utils;

import co.com.pragma.crediya.api.Handler;
import co.com.pragma.crediya.api.dto.request.CreateUserDTO;
import co.com.pragma.crediya.api.dto.request.LoginRequestDTO;
import co.com.pragma.crediya.api.dto.response.ResponseLoginDTO;
import co.com.pragma.crediya.api.dto.response.ResponseUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SwaggerDocumentation {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "saveUser",
                    operation = @Operation(
                            operationId = "saveUser",
                            summary = "Crear un nuevo usuario",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            requestBody = @RequestBody(
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
                                            content = @Content(schema = @Schema(implementation = RequestValidator.class),
                                                    examples = @ExampleObject(
                                                            name = "Bad request",
                                                            value = "Bad request - name is empty"
                                                    )))
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/consult_by_document",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "getByDocument",
                    operation = @Operation(
                            operationId = "getByDocument",
                            summary = "Consultar usuario por documento",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            parameters = {
                                    @Parameter(name = "document", in = ParameterIn.QUERY, required = true,
                                            description = "Número de documento del usuario")
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Consulta exitosa"),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                                    @ApiResponse(responseCode = "401", description = "No autorizado")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/login",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "login",
                    operation = @Operation(
                            operationId = "login",
                            summary = "Autenticar usuario",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Credenciales de inicio de sesión",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = LoginRequestDTO.class),
                                            examples = @ExampleObject(value = """
                                                    {
                                                        "email": "example@email.com",
                                                        "password": "examplePassword"
                                                    }
                                                    """)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200",
                                            description = "Login exitoso",
                                            content = @Content(schema = @Schema(implementation = ResponseLoginDTO.class))),
                                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
                                    @ApiResponse(responseCode = "400", description = "Bad Credentials")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> swaggerDoc() {
        return route(RequestPredicates.GET("/__swagger_dummy__/"), req ->
                ServerResponse.ok().build());
    }

    @Bean
    public OpenAPI customOpenAPI() {
        String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList(securitySchemeName))  // aquí creamos instancia
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}
