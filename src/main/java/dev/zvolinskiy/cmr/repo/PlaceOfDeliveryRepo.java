package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.PlaceOfDelivery;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlaceOfDeliveryRepo extends CrudRepository<PlaceOfDelivery, Integer> {
    List<PlaceOfDelivery> findPlaceOfDeliveriesByAddress(String address);
}
