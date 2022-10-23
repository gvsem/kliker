package dev.kliker.app.repository;


import dev.kliker.app.model.Keynote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface KeynoteRepository extends JpaRepository<Keynote, UUID> {

    Optional<Keynote> findOneByClickerId(UUID uuid);

    Optional<Keynote> findOneByDisplayId(UUID uuid);
}
