package com.example.realm.android

import androidx.lifecycle.*
import com.example.realm.Repo
import com.example.realm.User

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val realmRepo = Repo()

    val users: LiveData<List<User>> = liveData {
        emitSource(realmRepo.getUsersAsFlow().asLiveData(Dispatchers.Main))
    }

    //val users: MutableLiveData<List<User>> = MutableLiveData()


    fun saveUserInfo(user: User) {
        viewModelScope.launch {
            realmRepo.saveUserInfo(user)
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            val list = realmRepo.getUserAsList()
            withContext(Dispatchers.Main) {
                //  users.value = list
            }
        }
    }


}