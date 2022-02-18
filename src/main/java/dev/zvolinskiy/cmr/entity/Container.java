package dev.zvolinskiy.cmr.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "container")
public class Container {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "number")
    String number;

    @Column(name = "type")
    String type;
}
