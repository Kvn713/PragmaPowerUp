package co.com.pragma.crediya.api.dto.response;

import java.util.List;

public record SignUpDTO(
        String id,
        String email,
        String role,
        List<String> permissions
) {
}
