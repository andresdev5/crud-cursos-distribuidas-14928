package ec.edu.espe.msvc.users.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {
    @GetMapping("/public/test")
    public String test() {
        return "hello world";
    }
}
