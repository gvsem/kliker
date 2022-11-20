package dev.kliker.app.controller;

import dev.kliker.app.service.KeynoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.UUID;

import static dev.kliker.app.WebConfiguration.API_PREFIX;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/")
@Tag(name = "static", description = "Static frontend")
public class SiteController {

    private KeynoteService keynoteService;

    @Value("classpath:static/keynote.html")
    Resource htmlKeynote;

    @Value("classpath:static/clicker.html")
    Resource htmlClicker;

    @Value("classpath:static/display.html")
    Resource htmlDisplay;

    @Autowired
    public SiteController(KeynoteService keynoteService) {
        this.keynoteService = keynoteService;
    }

    @RequestMapping("/")
    public ResponseEntity<Object> mainPage() {
        return new ResponseEntity<Object>(htmlKeynote, new HttpHeaders(), OK);
    }

    @RequestMapping("/clicker/{clickerId}")
    public ResponseEntity<Object> clickerPage(@PathVariable UUID clickerId) {
        if (keynoteService.getKeynoteByClickerId(clickerId).isEmpty()) {
            throw new NotFoundException("");
        }
        return new ResponseEntity<Object>(htmlClicker, new HttpHeaders(), OK);
    }

    @RequestMapping("/display/{displayId}")
    public ResponseEntity<Object> displayPage(@PathVariable UUID displayId) {
        if (keynoteService.getKeynoteByDisplayId(displayId).isEmpty()) {
            throw new NotFoundException("");
        }
        return new ResponseEntity<Object>(htmlDisplay, new HttpHeaders(), OK);
    }

}
