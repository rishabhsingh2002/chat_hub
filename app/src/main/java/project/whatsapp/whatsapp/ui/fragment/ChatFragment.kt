package project.whatsapp.whatsapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import project.whatsapp.whatsapp.adapter.ChatAdapter
import project.whatsapp.whatsapp.databinding.FragmentChatBinding
import project.whatsapp.whatsapp.model.User

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private var database: FirebaseDatabase? = null
    private lateinit var userList: ArrayList<User>
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater)

        shimmerFrameLayout = binding.shimmer
        shimmerFrameLayout.startShimmer()

        database = FirebaseDatabase.getInstance()
        userList = ArrayList()

        database!!.reference.child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()

                    for (snapshot1 in snapshot.children) {
                        val user = snapshot1.getValue(User::class.java)
                        if (user!!.uid != FirebaseAuth.getInstance().uid) {
                            userList.add(user)
                        }
                    }
                    // stop shimmering effect
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                    binding.rcvUserList.visibility = View.VISIBLE

                    binding.rcvUserList.adapter = ChatAdapter(requireContext(), userList)

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        return binding.root
    }


}