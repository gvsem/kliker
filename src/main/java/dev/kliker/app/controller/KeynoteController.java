package dev.kliker.app.controller;

import com.fasterxml.jackson.annotation.JsonView;
import dev.kliker.app.model.Keynote;
import dev.kliker.app.model.View;
import dev.kliker.app.service.KeynoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static dev.kliker.app.WebConfiguration.API_PREFIX;
import static dev.kliker.app.WebConfiguration.MAX_FILE_SIZE;
import static org.springframework.http.HttpStatus.*;

@RestController
@Tag(name = "keynote", description = "API to upload keynotes")
@RequestMapping(API_PREFIX + "keynote")
@ControllerAdvice
public class KeynoteController {

    private KeynoteService keynoteService;

    @Autowired
    public KeynoteController(KeynoteService keynoteService) {
        this.keynoteService = keynoteService;
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<String> handleSizeLimit(
            SizeLimitExceededException exception
    ) {
        System.out.println(exception.getMessage());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(null);
    }

    @JsonView(View.OnCreate.class)
    @Operation(summary = "Upload keynote")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File has been uploaded"),
            @ApiResponse(responseCode = "413", description = "File is too large"),
            @ApiResponse(responseCode = "415", description = "File is malformed"),
    })
    @PostMapping(value = "upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Keynote> upload(@Parameter(description = "PDF file", required = true,
            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
                                          @RequestParam("file") MultipartFile file) {

        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(PAYLOAD_TOO_LARGE).body(null);
        }

        Keynote k = null;
        try {
            int count = PDDocument.load(file.getBytes()).getNumberOfPages();
            k = keynoteService.addKeynote(file.getBytes(), count);
        } catch (Exception e) {
            return ResponseEntity.status(UNSUPPORTED_MEDIA_TYPE).body(null);
        }

        if (k != null) {
            return ResponseEntity.status(CREATED).body(k);
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);

    }

}
