package com.example.realm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform