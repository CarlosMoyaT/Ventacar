package com.concesionario.ventacar;


import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Profile("test")
public class AdminTestController {

    @GetMapping("/some-resource")
    public ResponseEntity<String> someResource() {
        return ResponseEntity.ok("OK - admin resource for test ");
    }


}
