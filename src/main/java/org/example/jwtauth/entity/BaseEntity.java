package org.example.jwtauth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.jwtauth.service.SecurityService;

import java.time.LocalDateTime;


@Getter
@Setter
@MappedSuperclass
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BaseEntity {
    @Id
    private Long id;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "CREATED_BY", nullable = false)
    private String createdBy;

    @Column(name = "UPDATED_BY", nullable = false)
    private String updatedBy;

    @Column(name = "DELETED_BY", nullable = false)
    private String deletedBy;


    @Transient
    private SecurityService securityService;


    @PrePersist
    public void prePersist() {
        this.createdBy = securityService.getUserWithEmailFromAuth();
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedBy = securityService.getUserWithEmailFromAuth();
        this.updatedAt = LocalDateTime.now();
    }


}
