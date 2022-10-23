package dev.kliker.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import static dev.kliker.app.WebConfiguration.API_PREFIX;

@RestController
@Tag(name = "keynote", description = "API to upload keynotes")
@RequestMapping(API_PREFIX + "keynote")
public class KeynoteController {

}
