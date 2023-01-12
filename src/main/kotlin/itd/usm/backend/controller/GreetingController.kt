package itd.usm.backend.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class GreetingController {

    @GetMapping("/sayhello")
    fun greet(): ResponseEntity<String?>? {
        return ResponseEntity.ok("Hello, welcome to secured endpoint.")
    }
}