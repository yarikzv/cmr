package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.PlaceOfDelivery;

import java.util.List;

public interface PlaceOfDeliveryService {

    PlaceOfDelivery savePlaceOfDelivery(PlaceOfDelivery placeOfDelivery);

    PlaceOfDelivery findPlaceOfDeliveryById(Integer id);

    PlaceOfDelivery findPlaceOfDeliveryByAddress(String address);

    List<PlaceOfDelivery> findAllPlaceOfDelivery();

    void deletePlaceOfDelivery(PlaceOfDelivery placeOfDelivery);
}
