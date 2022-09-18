package com.ubt.auth.model;

import javax.validation.constraints.NotNull;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "token")
    private String token;

    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(name = "expire_date")
    private Date expireDate;


    public RefreshToken() {
    }

    public RefreshToken(String token, User user, Date expireDate) {
        this.token = token;
        this.user = user;
        this.expireDate = expireDate;
    }
}
