package com.android.fundamentals.workshop01

import com.android.fundamentals.domain.login.LoginInteractor
import com.android.fundamentals.domain.login.LoginResult
import kotlinx.coroutines.*

class Workshop1Presenter(
    private val interactor: LoginInteractor,
    private val mainDispatcher: CoroutineDispatcher
) {

    private val presenterScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + mainDispatcher)

    private var view: Workshop1View? = null

    fun attachView(view: Workshop1View) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    fun onDestroy() {
        presenterScope.cancel()
    }

    fun login(userName: String, password: String) {
        presenterScope.launch {
            //TODO 06: On "view" instance, set loading to true
            view?.setLoading(loading = true)

            val loginResult = interactor.login(userName = userName, password = password)
            //TODO 07: Uncomment and Handle "loginResult".
            // On "view" instance, show success or errors depending on result.
            // To autocomplete, place cursor on "when" word, press "Alt+Enter", remaining branches.
            when (loginResult) {
                is LoginResult.Error.UserName -> view?.showUserNameError()
                is LoginResult.Error.Password -> view?.showPasswordError()
                is LoginResult.Success -> view?.showSuccess()
            }


            //TODO 08: On "view" instance, set loading to false

            view?.setLoading(false)
        }
    }
}