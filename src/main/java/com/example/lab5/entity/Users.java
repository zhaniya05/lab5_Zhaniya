package com.example.lab5.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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

//    @Column(name="role")
//    @Enumerated(EnumType.STRING)
//    private Role role; // например, ROLE_USER или ROLE_ADMIN

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tasks> tasks;

//    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name="users_roles",
//            joinColumns = @JoinColumn(name="user_id", referencedColumnName = "userId"),
//            inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName = "roleId")
//    )
//    private Collection<Role> roles;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

}
