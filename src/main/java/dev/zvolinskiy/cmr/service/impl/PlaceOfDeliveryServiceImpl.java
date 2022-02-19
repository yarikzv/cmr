package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.PlaceOfDelivery;
import dev.zvolinskiy.cmr.repo.PlaceOfDeliveryRepo;
import dev.zvolinskiy.cmr.service.PlaceOfDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceOfDeliveryServiceImpl implements PlaceOfDeliveryService {

    private final PlaceOfDeliveryRepo placeOfDeliveryRepo;

    @Override
    public PlaceOfDelivery savePlaceOfDelivery(PlaceOfDelivery placeOfDelivery) {
        return placeOfDeliveryRepo.save(placeOfDelivery);
    }

    @Override
    public PlaceOfDelivery findPlaceOfDeliveryById(Integer id) {
        return placeOfDeliveryRepo.findById(id).orElse(null);
    }

    @Override
    public List<PlaceOfDelivery> findPlaceOfDeliveryByAddress(String address) {
        return placeOfDeliveryRepo.findPlaceOfDeliveriesByAddress(address);
    }

    @Override
    public void deletePlaceOfDelivery(PlaceOfDelivery placeOfDelivery) {
        placeOfDeliveryRepo.delete(placeOfDelivery);
    }
}
