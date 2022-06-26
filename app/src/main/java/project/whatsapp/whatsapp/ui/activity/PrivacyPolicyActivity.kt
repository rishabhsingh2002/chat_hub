package project.whatsapp.whatsapp.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import project.whatsapp.whatsapp.R
import project.whatsapp.whatsapp.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyPolicyBinding

    //private val url = "file:///android_asset/chathubprivacy.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Toolbar
        toolBarSetUp()


        //WebView setup
      //  webViewSetup(url)


    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewSetup(webUrl: String) {
        //add the privacy policy file in asset folder
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(webUrl)
    }

    private fun toolBarSetUp() {
        setSupportActionBar(binding.toolbarPrivacyPolicy)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            binding.toolbarPrivacyPolicy.title = "Privacy Policy"
        }
        binding.toolbarPrivacyPolicy.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}