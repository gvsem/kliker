package dev.kliker.app.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static dev.kliker.app.WebConfiguration.API_PREFIX;

@RestController
@Tag(name = "clicker", description = "Frontend API to change the slides")
@RequestMapping(API_PREFIX + "clicker")
public class ClickerController {

}
