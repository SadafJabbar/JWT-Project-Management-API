package project_management__api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Projects")
@Entity
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String name;
    private String description;
    private LocalDate createdAt;

    @PrePersist
    public void date(){
        this.createdAt=LocalDate.now();
    }

}
