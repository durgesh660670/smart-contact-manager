package com.durgesh.smart_contact_manager.entities;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int uId;
    @NotBlank(message = "Name field is required !! ")
    @Size(min = 2,max = 20,message ="Name can have character beetween 2-20 only")
    private String name;
    @Column(unique = true)
    @NotBlank(message = "Email field is required !! ")
    private String email;
    @NotBlank(message = "Password field is required !! ")
    private String password;
    private String role;
    private boolean active;
    private String image;
    @Column(length = 500)
    private String about;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    private List<Contact> contact=new ArrayList<>();

    public int getUId() {
        return this.uId;
    }

    public void setUId(int uId) {
        this.uId = uId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAbout() {
        return this.about;
    }

    public void setAbout(String about) {
        this.about = about;
    }


    public List<Contact> getContact() {
        return this.contact;
    }

    public void setContact(List<Contact> contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "{" +
            " uId='" + getUId() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", role='" + getRole() + "'" +
            ", active='" + isActive() + "'" +
            ", image='" + getImage() + "'" +
            ", about='" + getAbout() + "'" +
            ", contact='" + getContact() + "'" +
            "}";
    }
    


}
