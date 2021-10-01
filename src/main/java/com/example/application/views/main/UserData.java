package com.example.application.views.main;

//import com.vaadin.flow.component.Component;


import com.vaadin.flow.component.textfield.TextArea;
import org.springframework.stereotype.Repository;

@Repository
public class UserData extends TextArea {

    public enum role {
        ADMIN,USER;
    }

    private String firstName;
    private String lastName;
    private String email;
    private int Contact;
    private int age;
    private String occupation;
    private String gender;
    private String password;
    private String isOffline;
    private String isAdmin;
    private byte[] cv;

    public byte[] getCertificate() {
        return certificate;
    }

    public void setCertificate(byte[] certificate) {
        this.certificate = certificate;
    }

    private byte[] certificate;
    private String Skills;
    private int programmeY;


    private String SecurityQ;
    private String Answer;
    private byte[] profile;

    public UserData(){}
    public UserData(String st){

        this.setLabel(st);
    }
    public String getSecurityQ() {
        return SecurityQ;
    }

    public void setSecurityQ(String securityQ) {
        SecurityQ = securityQ;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public byte[] getCv() {
        return cv;
    }

    public void setCv(byte[] cv) {
        this.cv = cv;
    }

    public String getIsOffline() {
        return isOffline;
    }

    public void setIsOffline(String isOffline) {
        this.isOffline = isOffline;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
    public String getSkills() {
        return Skills;
    }

    public void setSkills(String skills) {
        Skills = skills;
    }

    public byte[] getProfile() {
        return profile;
    }

    public void setProfile(byte[] profile) {
        this.profile = profile;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getProgrammeY() {
        return programmeY;
    }

    public void setProgrammeY(int programmeY) {
        this.programmeY = programmeY;
    }

    public String getProgName() {
        return progName;
    }

    public void setProgName(String progName) {
        this.progName = progName;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String town) {
        Town = town;
    }

    private String progName;
    private String Town;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getContact() {
        return Contact;
    }
    public void setContact(int phone) {
        this.Contact = phone;
    }

    public String getOccupation() {
        return occupation;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }


    public static boolean isImportant(com.example.application.data.entity.SamplePerson person) {
        return true;
    }

    public static <F> void setDateOfBirth(com.example.application.data.entity.SamplePerson person, F f) {
    }

    public static <F> void setImportant(com.example.application.data.entity.SamplePerson person, F f) {
    }
}

