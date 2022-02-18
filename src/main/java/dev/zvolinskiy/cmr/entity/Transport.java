package dev.zvolinskiy.cmr.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "transport")
public class Transport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "truck")
    String truck;

    @Column(name = "trailer")
    String trailer;
}
