package org.example.jwtauth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.jwtauth.entity.enums.RefreshedTokenStatus;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
public class RefreshedToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Instant expiringDate;

    private boolean isActive = true;

    private boolean isValid;

    @Enumerated(EnumType.STRING)
    private RefreshedTokenStatus refreshedTokenStatus;


}
