package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.PlaceOfLoading;

import java.util.List;

public interface PlaceOfLoadingService {

    PlaceOfLoading savePlaceOfLoading(PlaceOfLoading placeOfLoading);

    PlaceOfLoading findPlaceOfLoadingById(Integer id);

    List<PlaceOfLoading> findPlaceOfLoadingByAddress(String address);

    void deletePlaceOfLoading(PlaceOfLoading placeOfLoading);
}