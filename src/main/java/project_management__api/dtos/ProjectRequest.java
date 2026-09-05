package project_management__api.dtos;


import lombok.Builder;
@Builder
public record ProjectRequest(
         String name,
         String description

) {
}
