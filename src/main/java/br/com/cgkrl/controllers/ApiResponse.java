package br.com.cgkrl.controllers;

public class ApiResponse {
    public static final String OK = "OK - A solicitação foi bem sucedida";
    public static final String CREATED = "CREATED - Item criado com sucesso";
    public static final String NO_CONTENT = "NO_CONTENT - A solicitação foi bem sucedida, mas não há conteúdo de retorno";
    public static final String BAD_REQUEST = "BAD_REQUEST - A solicitação não pôde ser atendida";
    public static final String UNAUTORIZED = "UNAUTORIZED - A solicitação requer autenticação do usuário";
    public static final String FORBIDDEN = "FORBIDDEN - Sem autorização para realizar a solicitação";
    public static final String NOT_FOUND = "NOT_FOUND - O item solicitado não foi encontrado";
    public static final String METHOD_NOT_ALLOWED = "METHOD_NOT_ALLOWED - Method Not Allowed";
    public static final String CONFLICT = "CONFLICT - A solicitação atendida devido um conflito";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR - Uma situação inesperada impediu a solcitação de ser atendida";

    /*
    @APIResponses({
        @APIResponse(responseCode = "200", description = ApiResponse.OK),
        @APIResponse(responseCode = "201", description = ApiResponse.CREATED),
        @APIResponse(responseCode = "204", description = ApiResponse.NO_CONTENT),
        @APIResponse(responseCode = "400", description = ApiResponse.BAD_REQUEST),
        @APIResponse(responseCode = "401", description = ApiResponse.UNAUTORIZED),
        @APIResponse(responseCode = "403", description = ApiResponse.FORBIDDEN),
        @APIResponse(responseCode = "404", description = ApiResponse.NOT_FOUND),
        @APIResponse(responseCode = "405", description = ApiResponse.METHOD_NOT_ALLOWED),
        @APIResponse(responseCode = "409", description = ApiResponse.CONFLICT),
        @APIResponse(responseCode = "500", description = ApiResponse.INTERNAL_SERVER_ERROR),
    })
     */

}
