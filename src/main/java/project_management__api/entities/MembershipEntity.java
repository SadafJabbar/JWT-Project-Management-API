package project_management__api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Membership")
@Entity
public class MembershipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;

    @ManyToOne
    @JoinColumn(name = "Project Assigned")
    private ProjectEntity projectEntity;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    List<UserMembershipEntity> userMembershipEntities;

}
