package dev.zvolinskiy.cmr.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@Table(name = "passport")
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "series")
    String series;

    @Column(name = "number")
    String number;

    @Column(name = "date")
    Date date;

    @Column(name = "issue")
    String issue;
}
