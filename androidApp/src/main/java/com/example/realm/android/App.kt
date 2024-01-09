package com.example.realm.android

import android.app.Application
import com.example.realm.Repo
import kotlinx.coroutines.runBlocking

class App : Application() {

    private val realmRepo = Repo()

    override fun onCreate() {
        super.onCreate()

        runBlocking {
            realmRepo.doSignIn()
        }
    }

}