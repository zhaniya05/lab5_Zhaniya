package com.example.lab5.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Transactional
@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class Users{

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="MySequenceGenerator")
    @SequenceGenerator(allocationSize=1, schema="public",  name="MySequenceGenerator", sequenceName = "mysequence")
    private Long userId;


    @Column(name="username", unique = true)
    @NotEmpty(message = "Username is required")
    private String username;

    @Column(name="password")
    @NotEmpty(message = "Password is required")
    private String password;

    @Column(name="email")
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    private String email;

    @Column(name="createdAt")
    private Date createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tasks> tasks;

    @Column(name = "role")
    private String role;

    @Column(name = "profile_photo", nullable = true)
    private String profilePhoto = "/uploads/default-profile-picture.webp";

    @Column(name = "reset_token")
    private String resetToken;


    // MARK THE METHOD THAT EXECUTE BEFORE THE ENTITY IS PERSISTED
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
//        if (this.profilePhoto == null || this.profilePhoto.isEmpty()) {
//            this.profilePhoto = "/uploads/default-profile-picture.webp";
//        }
    }

}
