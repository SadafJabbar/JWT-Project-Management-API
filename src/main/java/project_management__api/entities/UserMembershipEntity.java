package project_management__api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "userMembership")
public class UserMembershipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userMembershipId;

    @ManyToOne
    @JoinColumn(name = "userEntity")
    private UserEntity userEntity;

    @OneToMany
    @JoinColumn(name = "task")
    private List<TaskEntity> taskEntities;
}
