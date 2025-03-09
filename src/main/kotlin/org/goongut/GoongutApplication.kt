package org.goongut

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GoongutApplication

fun main(args: Array<String>) {
    runApplication<GoongutApplication>(*args)
}
