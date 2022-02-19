package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.PlaceOfDelivery;

import java.util.List;

public interface PlaceOfDeliveryService {

    PlaceOfDelivery savePlaceOfDelivery(PlaceOfDelivery placeOfDelivery);

    PlaceOfDelivery findPlaceOfDeliveryById(Integer id);

    List<PlaceOfDelivery> findPlaceOfDeliveryByAddress(String address);

    void deletePlaceOfDelivery(PlaceOfDelivery placeOfDelivery);
}
