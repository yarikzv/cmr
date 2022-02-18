package dev.zvolinskiy.cmr.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "firstname")
    String firstName;

    @Column(name = "middlename")
    String middleName;

    @Column(name = "lastname")
    String lastName;

    @OneToOne
    Passport passport;

    @OneToOne
    Transport transport;

}
