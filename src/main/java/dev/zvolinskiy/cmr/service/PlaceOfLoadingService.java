package dev.zvolinskiy.cmr.service;

import dev.zvolinskiy.cmr.entity.PlaceOfLoading;

import java.util.List;

public interface PlaceOfLoadingService {

    PlaceOfLoading save(PlaceOfLoading placeOfLoading);

    PlaceOfLoading update(PlaceOfLoading placeOfLoading);

    PlaceOfLoading findPlaceOfLoadingById(Integer id);

    PlaceOfLoading findPlaceOfLoadingByAddress(String address);

    List<PlaceOfLoading> findAllPlaceOfLoading();

    void delete(PlaceOfLoading placeOfLoading);

}
