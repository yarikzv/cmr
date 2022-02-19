package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.CMR;
import dev.zvolinskiy.cmr.entity.Container;
import dev.zvolinskiy.cmr.entity.Recipient;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface CMRRepo extends CrudRepository<CMR, Integer> {

    List<CMR> findCMRSByContainerContaining(Container container);

    List<CMR> findCMRSByDate(Date date);

    List<CMR> findCMRSByRecipient(Recipient recipient);
}
