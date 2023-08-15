package com.blatant.api.dto;

import java.util.Date;

public class UserResponse {

    private String login;

    private Date dateOfRegistration;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "login='" + login + '\'' +
                ", dateOfRegistration=" + dateOfRegistration +
                '}';
    }
}
