package dev.zvolinskiy.cmr.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "place_of_delivery")
public class PlaceOfDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "address")
    String address;

    @ManyToOne
    @JoinColumn(name = "country_id")
    Country country;
}
