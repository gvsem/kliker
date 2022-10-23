package dev.kliker.app.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Tag(name = "static", description = "Static frontend")
public class SiteController {

    @RequestMapping("/")
    public String mainPage() {
        return "forward:/keynote.html";
    }

    @RequestMapping("/clicker/{clicker_id}")
    public String clickerPage() {
        return "forward:/clicker.html";
    }

    @RequestMapping("/display/{display_id}")
    public String displayPage() {
        return "forward:/display.html";
    }

}
