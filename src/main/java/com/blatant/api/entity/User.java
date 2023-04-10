package com.blatant.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false,unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String hwid;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfRegistration = new Date();
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @OneToMany(mappedBy = "userId")
    private List<Subscription> userSubscription;

    public User(Long id, String login, String password, String hwid, Date dateOfRegistration, UserRole role, UserStatus status, List<Subscription> userSubscription) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.hwid = hwid;
        this.dateOfRegistration = dateOfRegistration;
        this.role = role;
        this.status = status;
        this.userSubscription = userSubscription;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHwid() {
        return hwid;
    }

    public void setHwid(String hwid) {
        this.hwid = hwid;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public List<Subscription> getUserSubscription() {
        return userSubscription;
    }

    public void setUserSubscription(List<Subscription> userSubscription) {
        this.userSubscription = userSubscription;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", hwid='" + hwid + '\'' +
                ", dateOfRegistration=" + dateOfRegistration +
                ", role=" + role +
                ", status=" + status +
                ", userSubscription=" + userSubscription +
                '}';
    }
}
