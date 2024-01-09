package com.example.realm

import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class Repo {
    private val schema= setOf(User::class)

    private val appDataService by lazy {
        val configuration= AppConfiguration.Builder("application-0-xdttr")
            .log(LogLevel.ALL)
            .build()
        App.create(configuration)
    }
    private val realm by lazy {
        val user =appDataService.currentUser!!
        val syncConfiguration = SyncConfiguration.Builder(user = user,schema)
            .name("realm-cloud")
            .schemaVersion(1)
            .initialSubscriptions{realm->
                add(realm.query<User>(),"userStream",updateExisting = true)
            }
            .build()
        Realm.open(syncConfiguration)
    }

    suspend fun doSignIn(){
        withContext(Dispatchers.Default){
            appDataService.login(Credentials.anonymous())
            //emailPassword("abdullaheladly","wct3840ZBChTeFjo"))
        }
    }
     suspend fun saveUserInfo(user: User){
        realm.write {
            copyToRealm(user,UpdatePolicy.ALL)
        }
    }

    suspend fun getUserAsList():List<User>{
        return withContext(Dispatchers.Default){
            realm.query(clazz = User::class).find()
        }
    }

     fun getUsersAsFlow():Flow<List<User>>{
        return realm.query<User>().asFlow().map {
               // it.list

                when (it){
                    is InitialResults -> it.list
                    is UpdatedResults -> it.list
                }
            }

    }
}