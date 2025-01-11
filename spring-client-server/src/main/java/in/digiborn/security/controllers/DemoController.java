package in.digiborn.security.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import in.digiborn.security.service.DemoService;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final DemoService demoService;

    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        String response = demoService.demo();
        return ResponseEntity.ok(response);
    }

}
