package com.android.fundamentals.workshop02

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.fundamentals.domain.login.LoginInteractor
import com.android.fundamentals.domain.login.LoginResult
import kotlinx.coroutines.launch

class Workshop2ViewModel(
    private val interactor: LoginInteractor
) : ViewModel() {

    private val mutableLiveData = MutableLiveData<State>(State.Default())

    val state : LiveData<State> get() = mutableLiveData

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            mutableLiveData.value = State.Loading()

            val loginResult = interactor.login(userName = userName, password = password)

            val newState = when (loginResult) {
                is LoginResult.Error.UserName -> State.UserNameError()
                is LoginResult.Error.Password -> State.PasswordError()
                is LoginResult.Success -> State.Success()
            }
            mutableLiveData.value = newState
        }
    }

    sealed class State {
        class Default : State()
        class Loading : State()
        class UserNameError : State()
        class PasswordError : State()
        class Success : State()
    }
}