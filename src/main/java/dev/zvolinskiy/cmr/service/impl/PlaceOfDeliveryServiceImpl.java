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
    public PlaceOfDelivery update(PlaceOfDelivery pod) {
        PlaceOfDelivery updatedPod = placeOfDeliveryRepo.findById(pod.getId()).orElse(null);
        if (updatedPod != null){
            updatedPod.setAddress(pod.getAddress());
            updatedPod.setCountry(pod.getCountry());
            return  placeOfDeliveryRepo.save(updatedPod);
        } else {
            return pod;
        }
    }

    @Override
    public PlaceOfDelivery findPlaceOfDeliveryById(Integer id) {
        return placeOfDeliveryRepo.findById(id).orElse(null);
    }

    @Override
    public PlaceOfDelivery findPlaceOfDeliveryByAddress(String address) {
        return placeOfDeliveryRepo.findPlaceOfDeliveryByAddress(address);
    }

    @Override
    public List<PlaceOfDelivery> findAllPlaceOfDelivery() {
        return (List<PlaceOfDelivery>) placeOfDeliveryRepo.findAll();
    }

    @Override
    public void delete(PlaceOfDelivery placeOfDelivery) {
        placeOfDeliveryRepo.delete(placeOfDelivery);
    }
}
