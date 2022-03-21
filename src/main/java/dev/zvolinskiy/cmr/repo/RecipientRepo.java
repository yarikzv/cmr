package dev.zvolinskiy.cmr.repo;

import dev.zvolinskiy.cmr.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipientRepo extends JpaRepository<Recipient, Integer> {
    Recipient findRecipientsByNameContainingIgnoreCase(String name);

    Recipient findByEdrpou(String code);
}
