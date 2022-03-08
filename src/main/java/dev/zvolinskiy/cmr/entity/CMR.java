package dev.zvolinskiy.cmr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.CascadeType.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cmr")
public class CMR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "number")
    private String number;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "order_number")
    private String orderNumber;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "sender_id")
    private Sender sender;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "recipient_id")
    private Recipient recipient;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "place_of_delivery_id")
    private PlaceOfDelivery placeOfDelivery;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "place_of_loading_id")
    private PlaceOfLoading placeOfLoading;

    @Column(name = "documents")
    private String documents;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "container_id")
    private Container container;

    @Column(name = "cargo_name")
    private String cargoName;

    @Column(name = "cargo_quantity")
    private Integer cargoQuantity;

    @Column(name = "cargo_weight")
    private String cargoWeight;

    @Column(name = "cargo_code")
    private Long cargoCode;

    @Column(name = "senders_instructions")
    private String sendersInstructions;

    @Column(name = "place_of_issue")
    private String placeOfIssue;

    @ManyToOne(cascade = {DETACH, MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "driver_id")
    private Driver driver;
}
