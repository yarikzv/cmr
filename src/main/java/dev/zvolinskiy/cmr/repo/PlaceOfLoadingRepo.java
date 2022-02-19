package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.PlaceOfLoading;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlaceOfLoadingRepo extends CrudRepository<PlaceOfLoading, Integer> {
    List<PlaceOfLoading> findPlaceOfLoadingsByAddress(String address);
}
