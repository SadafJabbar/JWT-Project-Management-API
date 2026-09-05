package project_management__api.entities;

import jakarta.persistence.*;
import lombok.*;
import project_management__api.dtos.TaskPriority;
import project_management__api.dtos.TaskStatus;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Tasks")
@Entity
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(nullable = false)
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;
    private LocalDate createdAt;

    @PrePersist
    public void date() {
        this.createdAt = LocalDate.now();
    }

    private Long projectId;


}
