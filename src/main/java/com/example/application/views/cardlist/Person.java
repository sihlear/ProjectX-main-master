package com.example.application.views.cardlist;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.textfield.TextArea;

public class Person {


    private byte[] profilePic ;

    public void setImage(Image image) {
        this.image = image;
    }

    private Image image;
    private String name;
    private String date;
    private String post;
    private String likes;
    private String comments;
    private String shares;
    private String password;
    private  String username;

    public Person() {


    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public void setPassword(String string) { this.password = string;
    }

    public Object getPassword() { return this.password;
    }

    public String getImage() {
        return  new String();
    }

    public void setImage(String image) {
    }
}
