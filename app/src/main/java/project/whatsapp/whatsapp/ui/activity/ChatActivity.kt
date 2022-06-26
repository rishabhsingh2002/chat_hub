package project.whatsapp.whatsapp.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import project.whatsapp.whatsapp.R
import project.whatsapp.whatsapp.adapter.MessageAdapter
import project.whatsapp.whatsapp.databinding.ActivityChatBinding
import project.whatsapp.whatsapp.model.MessageModel
import project.whatsapp.whatsapp.model.User
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var senderUid: String
    private lateinit var receiverUid: String
    private lateinit var auth: FirebaseAuth
    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String
    private lateinit var list: ArrayList<MessageModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Toolbar setup
        toolBarSetUp()

        // sender
        setSenderDetails()

        database = FirebaseDatabase.getInstance()
        list = ArrayList()



        senderUid = FirebaseAuth.getInstance().uid.toString()
        receiverUid = intent.getStringExtra("uid")!!

        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid

        binding.ivSend.setOnClickListener {

            if (binding.edtMessageBox.text.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Message", Toast.LENGTH_SHORT).show()
            } else {
                val message =
                    MessageModel(binding.edtMessageBox.text.toString(), senderUid, Date().time)

                val randomKey = database.reference.push().key

                database.reference.child("chats").child(senderRoom).child("message")
                    .child(randomKey!!)
                    .setValue(message).addOnSuccessListener {

                        database.reference.child("chats").child(receiverRoom).child("message")
                            .child(randomKey!!)
                            .setValue(message).addOnSuccessListener {
                                binding.edtMessageBox.text.clear()
                            }
                    }
            }
        }

        database.reference.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()

                    for (snapshot1 in snapshot.children) {
                        val data = snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)
                    }
                    val llm =LinearLayoutManager(this@ChatActivity)
                    llm.stackFromEnd = true     // items gravity sticks to bottom
                    llm.reverseLayout = false
                    binding.rvChat.layoutManager = llm
                    binding.rvChat.adapter = MessageAdapter(this@ChatActivity, list)

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, "Error : $error", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun setSenderDetails() {
        database = FirebaseDatabase.getInstance()
        receiverUid = intent.getStringExtra("uid")!!
        val name = binding.tvSenderName
        val image = binding.ivSenderImage
        database.reference.child("users").child(receiverUid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(User::class.java)
                    name.text = data?.name
                    Glide.with(applicationContext).load(data?.imageUrl).into(image)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

    private fun toolBarSetUp() {
        setSupportActionBar(binding.toolbarChatActivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        binding.toolbarChatActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}