package dev.zvolinskiy.cmr.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "sender")
public class Sender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "name")
    String name;

    @Column(name = "address")
    String address;

    @ManyToOne
    @JoinColumn(name = "country_id")
    Country country;
}
