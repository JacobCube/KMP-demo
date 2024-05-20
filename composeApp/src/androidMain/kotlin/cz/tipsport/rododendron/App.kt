package cz.tipsport.rododendron

import android.app.Application

class App: Application() {

    companion object {
        /** current app instance */
        val instance: App
            get() = instanceLocal ?: App()

        private var instanceLocal: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instanceLocal = this
    }
}