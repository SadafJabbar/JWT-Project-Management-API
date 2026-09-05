package project_management__api.entities;

import jakarta.persistence.*;
import lombok.*;
import project_management__api.dtos.Role;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Users")
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;
    private String password;
    private String email;
    private LocalDate createdAt;
    @Enumerated(EnumType.STRING)
    private Role systemRole;

    @PrePersist
    public void date(){
        this.createdAt=LocalDate.now();
    }
}
