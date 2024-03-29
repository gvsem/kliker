package dev.kliker.app.controller;

import dev.kliker.app.model.KeynoteMetaDto;
import dev.kliker.app.service.KeynoteService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.websocket.server.PathParam;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.kliker.app.WebConfiguration.API_PREFIX;

@RestController(API_PREFIX + "display")
@Tag(name = "display", description = "API to stream keynotes")
@RequestMapping(API_PREFIX + "display")
public class DisplayController {

    private KeynoteService keynoteService;
    private DisplayEmitters emitters;

    @Autowired
    public DisplayController(KeynoteService keynoteService, DisplayEmitters emitters) {
        this.keynoteService = keynoteService;
        this.emitters = emitters;
    }

    @Operation(summary = "Get current keynote meta information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information about current keynote",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = KeynoteMetaDto.class))}),
            @ApiResponse(responseCode = "404", description = "Keynote has not been found by displayId"),
    })
    @GetMapping(path = "{displayId}/meta", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<KeynoteMetaDto> getMeta(@PathVariable UUID displayId) {
        var k = keynoteService.getKeynoteByDisplayId(displayId);
        if (k.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(KeynoteMetaDto.fromKeynote(k.get()));
    }

    @Operation(summary = "Get keynote data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Keynote file is returned",
                    content = {@Content(mediaType = "application/pdf")}),
            @ApiResponse(responseCode = "404", description = "Keynote has not been found by displayId"),
    })
    @GetMapping(path = "{displayId}/file", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<byte[]> getFile(@PathVariable UUID displayId) {
        var k = keynoteService.getKeynoteByDisplayId(displayId);
        if (k.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(k.get().getFile());
    }

    @GetMapping(path = "{displayId}/events", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    ResponseEntity<SseEmitter> createConnection(@PathVariable UUID displayId) {
        var k = keynoteService.getKeynoteByDisplayId(displayId);
        if (k.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        var emitter = new SseEmitter(-1L);
        emitters.add(emitter, displayId);
        return ResponseEntity.status(HttpStatus.OK).body(emitter);
    }


}
