package fr.nate.natebot.controllers;

import fr.nate.natebot.controllers.domain.Status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public Status getStatus() {
        return new Status("ok");
    }
}
