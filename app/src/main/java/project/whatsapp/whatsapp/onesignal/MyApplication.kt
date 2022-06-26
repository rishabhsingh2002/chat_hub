package project.whatsapp.whatsapp.onesignal

import android.app.Application
import com.onesignal.OneSignal

class MyApplication:Application() {


    //One Signal
    private val ONESIGNAL_APP_ID = "837e4f50-6ac9-4c29-94d9-e54dd8fdf5b3"

    override fun onCreate() {
        super.onCreate()

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}