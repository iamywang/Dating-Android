package cn.iamywang.mapchats.activity.user

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.*

import cn.iamywang.mapchats.R
import cn.iamywang.mapchats.activity.friend.ChatRoomActivity
import cn.iamywang.mapchats.activity.friend.FriendsListActivity
import cn.iamywang.mapchats.activity.friend.LocationShareActivity
import cn.iamywang.mapchats.activity.misc.AboutActivity
import cn.iamywang.mapchats.activity.misc.FeedBackActivity
import cn.iamywang.mapchats.activity.path.HistoryPathListActivity
import cn.iamywang.mapchats.activity.path.PathRecordActivity
import cn.iamywang.mapchats.util.network.JavaHttpKolley
import cn.iamywang.mapchats.util.list.UserItemAdapter
import cn.iamywang.mapchats.util.list.UserListItem
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    AdapterView.OnItemClickListener{
    private var USERID = ""
    private var exitTime = System.currentTimeMillis()
    val list = LinkedList<UserListItem>()
    val mAdapter = UserItemAdapter(list, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.app_name,
            R.string.app_name
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        this.startActivityForResult(Intent(this, LoginActivity::class.java), 109)
        val v = findViewById<ConstraintLayout>(R.id.user_item_view)
        v.visibility = View.INVISIBLE
    }

    fun setAdapter() {
        val list_view = findViewById<ListView>(R.id.friends_list)
        list_view.adapter = mAdapter
        list_view.setOnItemClickListener(this)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 在申请码和返回码都符合的时候，接收返回的数据
        if (requestCode == 109 && resultCode == 110) {
            val id = findViewById<TextView>(R.id.textView_id)
            val nick = findViewById<TextView>(R.id.textView_name)
            val head_img = findViewById<ImageView>(R.id.nav_head_image)
            if (data != null) {
                this.USERID = data.getStringExtra("id")
                id.text = this.USERID
                nick.text = data.getStringExtra("name")
                if (data.getStringExtra("sex") == "男") {
                    head_img.setImageResource(R.drawable.ic_user_color)
                } else if (data.getStringExtra("sex") == "女") {
                    head_img.setImageResource(R.drawable.ic_user_color_2)
                }
            }
            val jhk = JavaHttpKolley()
            jhk.getFakeUserMsgList(this)
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent(this, ChatRoomActivity::class.java)
        intent.putExtra("local_id", this.USERID)
        intent.putExtra("remote_id", list.get(position).user_id)
        intent.putExtra("remote_name", list.get(position).user_name)
        startActivity(intent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    fun exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(
                applicationContext, "再按一次退出程序",
                Toast.LENGTH_SHORT
            ).show()
            exitTime = System.currentTimeMillis()
        } else {
            val jhk = JavaHttpKolley()
            jhk.setOffline(this.USERID, this)
            finish()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.nav_home) {
            val intent = Intent(this, UserInfoActivity::class.java)
            intent.putExtra("id", this.USERID)
            intent.putExtra("loc", "中国山东省济南市")
            startActivity(intent)
        } else if (id == R.id.nav_map) {
            val intent = Intent(this, LocationShareActivity::class.java)
            intent.putExtra("id", this.USERID)
            startActivity(intent)
        } else if (id == R.id.nav_gps) {
            val intent = Intent(this, HistoryPathListActivity::class.java)
            intent.putExtra("id", this.USERID)
            startActivity(intent)
        } else if (id == R.id.nav_record) {
            val intent = Intent(this, PathRecordActivity::class.java)
            intent.putExtra("id", this.USERID)
            intent.putExtra("nick", findViewById<TextView>(R.id.textView_name).text)
            startActivity(intent)
        } else if (id == R.id.nav_friends) {
            val intent = Intent(this, FriendsListActivity::class.java)
            intent.putExtra("id", this.USERID)
            startActivity(intent)
        } else if (id == R.id.nav_chat) {
            val intent = Intent(this, ChatRoomActivity::class.java)
            intent.putExtra("local_id", this.USERID)
            intent.putExtra("remote_id", "0")
            intent.putExtra("remote_name", "聊天室")
            startActivity(intent)
        } else if (id == R.id.nav_feed) {
            val intent = Intent(this, FeedBackActivity::class.java)
            intent.putExtra("id", this.USERID)
            intent.putExtra("nick", findViewById<TextView>(R.id.textView_name).text)
            startActivity(intent)
        } else if (id == R.id.nav_share) {
            val intent = Intent(this, FeedBackActivity::class.java)
            intent.putExtra("id", this.USERID)
            intent.putExtra("nick", findViewById<TextView>(R.id.textView_name).text)
            startActivity(intent)
        } else if (id == R.id.nav_about) {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}