package project.whatsapp.whatsapp.ui.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import project.whatsapp.whatsapp.BuildConfig
import project.whatsapp.whatsapp.R
import project.whatsapp.whatsapp.databinding.ActivityMainBinding
import project.whatsapp.whatsapp.model.User

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var list: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        list = ArrayList()

        if (auth.currentUser == null) {
            startActivity(Intent(this, NumberActivity::class.java))
            finish()
        }


        val navController = findNavController(R.id.fragmentContainerView)

        //Drawer Layout setup
        drawerLayoutSetup()
    }

    private fun drawerLayoutSetup() {
        binding.btnMenu.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                binding.drawerLayout.closeDrawer(Gravity.LEFT)
            } else {
                binding.drawerLayout.openDrawer(Gravity.LEFT)
            }
        }
        //Set up the current user name and image
        setUserData()
        //Navigation Item Click listener
        navigationItemClickListener()

    }

    private fun setUserData() {

        if (auth.currentUser == null) {

        } else {
            database = FirebaseDatabase.getInstance()
            val userName =
                binding.navigationDrawer.getHeaderView(0).findViewById<TextView>(R.id.tv_user_name)

            val userImage =
                binding.navigationDrawer.getHeaderView(0).findViewById<ImageView>(R.id.iv_user)

            database.reference.child("users").child(auth.currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val data = snapshot.getValue(User::class.java)
                        Glide.with(applicationContext).load(data!!.imageUrl).into(userImage)
                        userName.text = data.name
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        }


    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
        } else {
            super.onBackPressed()
        }


    }

    private fun navigationItemClickListener() {

        binding.navigationDrawer
        binding.navigationDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.mi_share -> {
                    try {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                        var shareMessage = "\nShare with you loved ones\n\n"
                        shareMessage = """
                    ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}""".trimIndent()
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                        startActivity(Intent.createChooser(shareIntent, "choose one"))
                    } catch (e: Exception) {
                    }
                    true
                }
                R.id.mi_rate_us -> {
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + applicationContext.packageName)
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=" + applicationContext.packageName)
                            )
                        )
                    }
                    true
                }
                R.id.mi_privacy -> {
                    startActivity(Intent(this@MainActivity, PrivacyPolicyActivity::class.java))
                    true
                }
                R.id.mi_feedback -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    val recipients = arrayOf("rishabh1112131415@gmail.com")
                    intent.putExtra(Intent.EXTRA_EMAIL, recipients)
                    intent.putExtra(Intent.EXTRA_SUBJECT, "")
                    intent.putExtra(Intent.EXTRA_TEXT, "")
                    intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com")
                    intent.type = "text/html"
                    intent.setPackage("com.google.android.gm")
                    startActivity(Intent.createChooser(intent, "Send mail"))
                    true
                }
                else -> {
                    true
                }
            }
        }
    }
}