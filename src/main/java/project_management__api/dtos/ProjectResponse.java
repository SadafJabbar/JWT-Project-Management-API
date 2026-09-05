package project_management__api.dtos;

import lombok.Builder;


@Builder
public record ProjectResponse(
        Long projectId,
        String name,
        String description
) {
}
