package com.example.socialforme.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "information")
public class Info {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "git")
    private String github;

    @Column(name = "twitter")
    private String twitter;

    @Column(name = "facebook")
    private String facebook;

    @Column(name = "instagram")
    private String instagram;

    @Column(name = "email")
    private String email;

    @Column(name = "tel")
    private  String telephone;

    @Column(name = "city")
    private String city;

    @Column(name = "image")
    private String image;
}
