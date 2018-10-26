package io.github.mohamedisoliman.mvi

import android.app.Application

/**
 *
 * Created by Mohamed Ibrahim on 10/26/18.
 */
class MVIApp : Application() {


    override fun onCreate() {
        super.onCreate()
        appDependencies = AppDependencies(this)
    }

    companion object {
        @JvmStatic
        lateinit var appDependencies: AppDependencies
    }

}