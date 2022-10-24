package dev.kliker.app.controller;

import dev.kliker.app.model.KeynoteMetaDto;
import dev.kliker.app.service.KeynoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.UUID;

import static dev.kliker.app.WebConfiguration.API_PREFIX;

@RestController
@Tag(name = "clicker", description = "Frontend API to change the slides")
@RequestMapping(API_PREFIX + "clicker")
public class ClickerController {

    private KeynoteService keynoteService;

    @Autowired
    public ClickerController(KeynoteService keynoteService){
        this.keynoteService = keynoteService;
    }

    @Operation(summary = "Turn current slide next")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Information about updated keynote",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = KeynoteMetaDto.class))}),
            @ApiResponse(responseCode = "404", description = "Keynote has not been found by clickerId"),
    })
    @GetMapping(path = "{clickerId}/next_slide")
    ResponseEntity<KeynoteMetaDto> nextSlide(@PathVariable UUID clickerId) {
        var k = keynoteService.getKeynoteByDisplayId(clickerId);
        if (k.isEmpty()) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); }
        return ResponseEntity.ok(KeynoteMetaDto.fromKeynote(keynoteService.nextSlide(k.get())));
    }

    @Operation(summary = "Turn current slide prev")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Information about updated keynote",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = KeynoteMetaDto.class))}),
            @ApiResponse(responseCode = "404", description = "Keynote has not been found by clickerId"),
    })
    @GetMapping(path = "{clickerId}/prev_slide")
    ResponseEntity<KeynoteMetaDto> prevSlide(@PathVariable UUID clickerId) {
        var k = keynoteService.getKeynoteByDisplayId(clickerId);
        if (k.isEmpty()) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); }
        return ResponseEntity.ok(KeynoteMetaDto.fromKeynote(keynoteService.prevSlide(k.get())));
    }


}
