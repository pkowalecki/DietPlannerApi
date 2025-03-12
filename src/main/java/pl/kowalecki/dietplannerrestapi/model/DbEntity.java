package pl.kowalecki.dietplannerrestapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class DbEntity {

    @Column(unique = true, nullable = false, updatable = false)
    private UUID publicId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime additionDate;

    @Column
    private LocalDateTime editDate;

    @PrePersist
    public void prePersist() {
        this.publicId = UUID.randomUUID();
        this.additionDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.editDate = LocalDateTime.now();
    }
}
