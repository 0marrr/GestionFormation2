package com.esprit.forumhub.model;

import java.util.List;

public class User {
    private int id;
    private String email;
    private List<String> roles;
    private String password;
    private String name;
    private String prenom;
    private int tel;
    private String image;
    private String username;
    private boolean isBanned;

    // Constructors
    public User() {
    }

    public User(int id,String name, String email) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public User(int id, String email, List<String> roles, String password, String name, String prenom, int tel, String image, String username, boolean isBanned) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.name = name;
        this.prenom = prenom;
        this.tel = tel;
        this.image = image;
        this.username = username;
        this.isBanned = isBanned;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", prenom='" + prenom + '\'' +
                ", tel=" + tel +
                ", image='" + image + '\'' +
                ", username='" + username + '\'' +
                ", isBanned=" + isBanned +
                '}';
    }

}