package dev.zvolinskiy.cmr.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

import static javax.persistence.CascadeType.*;

@Entity
@Data
@Table(name = "cmr")
public class CMR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "number")
    String number;

    @Column(name = "date")
    Date date;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "sender_id")
    Sender sender;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "recipient_id")
    Recipient recipient;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "place_of_delivery_id")
    PlaceOfDelivery placeOfDelivery;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "place_of_loading_id")
    PlaceOfLoading placeOfLoading;

    @Column(name = "documents")
    String documents;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "container_id")
    Container container;

    @Column(name = "cargo_name")
    String cargoName;

    @Column(name = "cargo_quantity")
    Integer cargoQuantity;

    @Column(name = "cargo_weight")
    String cargoWeight;

    @Column(name = "cargo_code")
    Long cargoCode;

    @Column(name = "place_of_issue")
    String placeOfIssue;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "driver_id")
    Driver driver;
}
