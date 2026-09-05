package project_management__api.dtos;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TaskRequest(
        @Size(min = 3,max = 30,message = "title must be between 3 to 30 characters ")
         String title,
         String description,
          TaskStatus status,
          TaskPriority priority,
        Long projectId

) {
}
