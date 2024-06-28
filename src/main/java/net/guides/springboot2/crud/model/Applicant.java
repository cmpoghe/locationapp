package net.guides.springboot2.crud.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    @Column(unique = true)
    private String applicant;
}
