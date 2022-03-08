package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.PlaceOfLoading;
import org.springframework.data.repository.CrudRepository;

public interface PlaceOfLoadingRepo extends CrudRepository<PlaceOfLoading, Integer> {
    PlaceOfLoading findPlaceOfLoadingsByAddress(String address);
}
