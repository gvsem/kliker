package dev.kliker.app.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.webjars.NotFoundException;

@Controller
public class AppErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {

        System.out.println("error-handle");
        throw new NotFoundException("");
    }

}