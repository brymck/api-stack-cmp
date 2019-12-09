package com.github.brymck.greetings.server

import org.springframework.stereotype.Service

@Service
class Service {
    fun sayHello(name: String): String {
        return "Hello, $name!"
    }
}
