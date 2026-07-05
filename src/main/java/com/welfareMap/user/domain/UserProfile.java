package com.welfareMap.user.domain;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(nullable = false)
    private String region = "";

    @Column(nullable = false)
    private String gender = "none";

    @Column(name = "marital_status", nullable = false)
    private String maritalStatus = "single";

    @Column(name = "children_status", nullable = false)
    private String childrenStatus = "none";

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "life_stages", columnDefinition = "text[]", nullable = false)
    private String[] lifeStages = new String[0];

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]", nullable = false)
    private String[] interests = new String[0];

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public UserProfile(UserAccount user) {
        this.user = user;
        this.userId = user.getId();
    }

    public void update(
        LocalDate birthDate,
        String region,
        String gender,
        String maritalStatus,
        String childrenStatus,
        String[] lifeStages,
        String[] interests
    ) {
        this.birthDate = birthDate;
        this.region = valueOrEmpty(region);
        this.gender = valueOrDefault(gender, "none");
        this.maritalStatus = valueOrDefault(maritalStatus, "single");
        this.childrenStatus = valueOrDefault(childrenStatus, "none");
        this.lifeStages = lifeStages == null ? new String[0] : lifeStages;
        this.interests = interests == null ? new String[0] : interests;
    }

    @PrePersist
    void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private String valueOrDefault(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
