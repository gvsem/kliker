package dev.kliker.app.service;

import dev.kliker.app.controller.DisplayEmitters;
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

    private DisplayEmitters emitters;

    @Autowired
    KeynoteService(KeynoteRepository repository, DisplayEmitters emitters) {
        this.repository = repository;
        this.emitters = emitters;
    }

    public Keynote addKeynote(byte[] data) throws IllegalArgumentException {
        if (!PdfUtils.hasSupportedPdfHeader(data)) {
            //throw new IllegalArgumentException("Not a supported pdf document.");
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
        return repository.findOneByClickerId(id);
    }

    public Optional<Keynote> getKeynoteByDisplayId(UUID id) {
//        var a = repository.findAll();
//        for ( var t : a) {
//            System.out.println(t.getDisplayId());
//            if (id.equals(t.getDisplayId())) {
//                System.out.println("lol");
//            }
//        }
        return repository.findOneByDisplayId(id);
    }

    public Keynote nextSlide(Keynote k) {
        repository.saveAndFlush(k.nextSlide());
        emitters.sendSetSlide(k.getDisplayId(), k.getCurrentSlide());
        return k;
    }

    public Keynote prevSlide(Keynote k) {
        repository.saveAndFlush(k.prevSlide());
        emitters.sendSetSlide(k.getDisplayId(), k.getCurrentSlide());
        return k;
    }



}
