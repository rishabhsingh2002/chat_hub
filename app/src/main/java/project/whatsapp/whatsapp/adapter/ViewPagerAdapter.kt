package project.whatsapp.whatsapp.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(
    private val context: Context,
    fm: FragmentManager?,
    private val list: ArrayList<Fragment>
) : FragmentPagerAdapter(fm!!) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]

    }

    companion object {
        val TAB_TITLES = arrayListOf("Chats", "Status", "Call")
    }
}