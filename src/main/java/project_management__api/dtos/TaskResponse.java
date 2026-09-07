package project_management__api.dtos;

import lombok.Builder;
import project_management__api.enums.TaskPriority;
import project_management__api.enums.TaskStatus;

import java.time.LocalDate;

@Builder
public record TaskResponse(
         Long taskId,
         String title,
         String description,
         TaskStatus status,
         TaskPriority priority,
         LocalDate createdAt,
         Long projectId

) {
}
