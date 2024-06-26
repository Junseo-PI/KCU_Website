package org.example.kcu_website.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_identify_id")
    private Long id;

    @Column(name = "user_id")
    private String name;
    @Column(name = "user_password")
    private String password;

    @Column(name = "authority")
    private String authority;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String username) {
        this.name = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
