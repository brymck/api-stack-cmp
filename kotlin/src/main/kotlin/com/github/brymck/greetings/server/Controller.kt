package com.github.brymck.greetings.server

import com.github.brymck.greetings.models.Request
import com.github.brymck.greetings.models.Response
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class Controller(private val service: GreetingsService) {
    @PostMapping("/greetings")
    fun createGreeting(@RequestBody request: Request): Response {
        val greeting = service.greet(request.name)
        return Response(greeting)
    }
}
