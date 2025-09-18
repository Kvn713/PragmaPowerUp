package co.com.pragma.crediya.api.dto.response;

public record ResponseLoginDTO(
        String accessToken,
        String tokenType,
        long expire,
        SignUpDTO user
) {
}
