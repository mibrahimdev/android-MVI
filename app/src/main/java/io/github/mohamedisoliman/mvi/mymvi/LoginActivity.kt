package io.github.mohamedisoliman.mvi.mymvi

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import io.github.mohamedisoliman.mvi.R
import io.github.mohamedisoliman.mvi.clickObservable
import io.github.mohamedisoliman.mvi.mymvi.base.MviViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*

/**
 *
 * Created by Mohamed Ibrahim on 9/27/18.
 */
class LoginActivity : AppCompatActivity(), MviLoginView {

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginViewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(LoginViewModel::class.java)

        loginViewModel.setupLogin()
    }


    override fun loginClick(): Observable<LoginAction> {
        return login_button.clickObservable()
                .flatMap {
                    Observable.just(LoginAction(username_edittext.text.toString(), password_edittext.text.toString()))
                }.subscribeOn(AndroidSchedulers.mainThread())
    }


    override fun render(viewState: MviViewState) {
        when (viewState) {
            is LoginViewState.Loading -> {
                progress_circular.visibility = if (viewState.isLoading) View.VISIBLE else View.GONE
            }
            is LoginViewState.Success -> {
                Toast.makeText(this, viewState.message, Toast.LENGTH_LONG).show()
                progress_circular.visibility = View.GONE
            }
            is LoginViewState.Error -> {
                Toast.makeText(this, viewState.message, Toast.LENGTH_LONG).show()
                progress_circular.visibility = View.GONE

            }
            is LoginViewState.InvalidPassword -> {
                Toast.makeText(this, viewState.message, Toast.LENGTH_LONG).show()
                progress_circular.visibility = View.GONE
            }
            is LoginViewState.InvalidUsername -> {
                Toast.makeText(this, viewState.message, Toast.LENGTH_LONG).show()
                progress_circular.visibility = View.GONE
            }
        }
    }


}