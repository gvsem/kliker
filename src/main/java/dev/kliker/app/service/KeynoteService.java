package dev.kliker.app.service;

import dev.kliker.app.model.Keynote;
import dev.kliker.app.repository.KeynoteRepository;
import dev.kliker.app.utils.PdfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;
import java.util.UUID;

@Component
public class KeynoteService {

    private KeynoteRepository repository;

    @Autowired
    KeynoteService(KeynoteRepository repository) {
        this.repository = repository;
    }

    public Keynote addKeynote(byte[] data) throws IllegalArgumentException {
        if (!PdfUtils.hasSupportedPdfHeader(data)) {
            throw new IllegalArgumentException("Not a supported pdf document.");
        }
        Keynote k = new Keynote();
        k.setFile(data);
        repository.saveAndFlush(k);
        return k;
    }

    public Optional<Keynote> getKeynote(UUID id) {
        return repository.findById(id);
    }

    public Optional<Keynote> getKeynoteByClickerId(UUID id) {
        return repository.findByClickerId(id);
    }

    public Optional<Keynote> getKeynoteByDisplayId(UUID id) {
        return repository.findByDisplayId(id);
    }

    public Keynote nextSlide(Keynote k) {
        return repository.saveAndFlush(k.nextSlide());
    }

    public Keynote prevSlide(Keynote k) {
        return repository.saveAndFlush(k.prevSlide());
    }



}
