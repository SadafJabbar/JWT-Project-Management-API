package project_management__api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "RefreshToken")
@Entity
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;


    @OneToOne
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private UserEntity user;

    @Column(nullable = false,unique = true)
    private String validAccessToken;

    @Column(nullable = false,unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked;

}
