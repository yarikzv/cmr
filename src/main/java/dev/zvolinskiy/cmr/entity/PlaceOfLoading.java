package dev.zvolinskiy.cmr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "place_of_loading")
public class PlaceOfLoading {

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
