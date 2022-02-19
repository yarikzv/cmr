package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.PlaceOfLoading;
import dev.zvolinskiy.cmr.repo.PlaceOfLoadingRepo;
import dev.zvolinskiy.cmr.service.PlaceOfLoadingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class PlaceOfLoadingServiceImpl implements PlaceOfLoadingService {

    private final PlaceOfLoadingRepo placeOfLoadingRepo;

    @Override
    public PlaceOfLoading savePlaceOfLoading(PlaceOfLoading placeOfLoading) {
        return placeOfLoadingRepo.save(placeOfLoading);
    }

    @Override
    public PlaceOfLoading findPlaceOfLoadingById(Integer id) {
        return placeOfLoadingRepo.findById(id).orElse(null);
    }

    @Override
    public List<PlaceOfLoading> findPlaceOfLoadingByAddress(String address) {
        return placeOfLoadingRepo.findPlaceOfLoadingsByAddress(address);
    }

    @Override
    public void deletePlaceOfLoading(PlaceOfLoading placeOfLoading) {
        placeOfLoadingRepo.delete(placeOfLoading);
    }
}
