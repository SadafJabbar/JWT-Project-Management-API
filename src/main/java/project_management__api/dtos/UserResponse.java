package project_management__api.dtos;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserResponse(
        Long userId,
        String username,
        String email,
        Role systemRole,
        LocalDate createdAt
) {
}
