package project_management__api.dtos;

import lombok.Builder;

@Builder
public record ApiResponse<T>(
        String message,
        T data
) {
}
