package com.ubt.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ubt.auth.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username cannot be null")
    @Column(name = "username")
    private String username;

    @NotBlank(message = "E-mail cannot be null")
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank(message = "Password cannot be null")
    private String password;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @NotNull
    private UserStatus status = UserStatus.UNCONFIRMED;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "resetToken")
    private String resetToken;

    @Column(name = "confirmToken")
    private String confirmToken;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<RefreshToken> refreshTokens;

    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
