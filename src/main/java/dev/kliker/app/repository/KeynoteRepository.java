package dev.kliker.app.repository;


import dev.kliker.app.model.Keynote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface KeynoteRepository extends JpaRepository<Keynote, UUID> {

    Optional<Keynote> findByClickerId(UUID uuid);

    Optional<Keynote> findByDisplayId(UUID uuid);
}
