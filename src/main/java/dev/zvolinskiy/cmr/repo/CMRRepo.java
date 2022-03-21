package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.CMR;
import dev.zvolinskiy.cmr.entity.Container;
import dev.zvolinskiy.cmr.entity.Recipient;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface CMRRepo extends CrudRepository<CMR, Integer> {

    List<CMR> findByContainer(Container container);

    List<CMR> findCMRSByDate(LocalDate date);

    List<CMR> findCMRSByRecipient(Recipient recipient);
}
