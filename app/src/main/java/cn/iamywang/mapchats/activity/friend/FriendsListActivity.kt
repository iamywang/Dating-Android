package cn.iamywang.mapchats.activity.friend

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import cn.iamywang.mapchats.R
import cn.iamywang.mapchats.util.network.JavaHttpKolley
import cn.iamywang.mapchats.util.list.UserItemAdapter
import cn.iamywang.mapchats.util.list.UserListItem
import java.util.*

class FriendsListActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    val list = LinkedList<UserListItem>()
    val mAdapter = UserItemAdapter(list, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_list)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val v = findViewById<ConstraintLayout>(R.id.user_item_view)
        v.visibility = View.INVISIBLE

        val jhk = JavaHttpKolley()
        jhk.getUserList(this)
    }

    fun setAdapter() {
        val list_view = findViewById<ListView>(R.id.friends_list)
        list_view.adapter = mAdapter
        list_view.setOnItemClickListener(this)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
