package com.palomiklo.streampulse

import org.springframework.boot.SpringApplication

object App {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(App::class.java, *args)
    }
}