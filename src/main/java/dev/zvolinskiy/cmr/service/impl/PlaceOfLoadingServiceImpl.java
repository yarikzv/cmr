package dev.zvolinskiy.cmr.service.impl;

import dev.zvolinskiy.cmr.entity.PlaceOfLoading;
import dev.zvolinskiy.cmr.repo.PlaceOfLoadingRepo;
import dev.zvolinskiy.cmr.service.PlaceOfLoadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceOfLoadingServiceImpl implements PlaceOfLoadingService {

    private final PlaceOfLoadingRepo placeOfLoadingRepo;

    @Override
    public PlaceOfLoading save(PlaceOfLoading placeOfLoading) {
        return placeOfLoadingRepo.save(placeOfLoading);
    }

    @Override
    public PlaceOfLoading update(PlaceOfLoading pol) {
        PlaceOfLoading updatedPol = placeOfLoadingRepo.findById(pol.getId()).orElse(null);
        if (updatedPol != null){
            updatedPol.setAddress(pol.getAddress());
            updatedPol.setCountry(pol.getCountry());
            return placeOfLoadingRepo.save(updatedPol);
        } else {
            return pol;
        }
    }

    @Override
    public PlaceOfLoading findPlaceOfLoadingById(Integer id) {
        return placeOfLoadingRepo.findById(id).orElse(null);
    }

    @Override
    public PlaceOfLoading findPlaceOfLoadingByAddress(String address) {
        return placeOfLoadingRepo.findPlaceOfLoadingsByAddress(address);
    }

    @Override
    public List<PlaceOfLoading> findAllPlaceOfLoading() {
        return (List<PlaceOfLoading>) placeOfLoadingRepo.findAll();
    }

    @Override
    public void delete(PlaceOfLoading placeOfLoading) {
        placeOfLoadingRepo.delete(placeOfLoading);
    }
}
