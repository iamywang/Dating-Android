package cn.iamywang.mapchats

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.MenuItem

import android.widget.TextView
import android.widget.Toast
import cn.iamywang.mapchats.activity.friend.ChatRoomActivity
import cn.iamywang.mapchats.activity.friend.FriendsListActivity
import cn.iamywang.mapchats.activity.friend.LocationShareActivity
import cn.iamywang.mapchats.activity.misc.AboutActivity
import cn.iamywang.mapchats.activity.misc.FeedBackActivity
import cn.iamywang.mapchats.activity.path.HisrotyLocationActivity
import cn.iamywang.mapchats.activity.path.HistoryPathListActivity
import cn.iamywang.mapchats.activity.path.PathRecordActivity
import cn.iamywang.mapchats.activity.user.LoginActivity
import cn.iamywang.mapchats.activity.user.UserInfoActivity
import cn.iamywang.mapchats.util.JavaHttpKolley

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var USERID = ""
    private var exitTime = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.startActivityForResult(Intent(this, LoginActivity::class.java), 1)
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )
        requestPermissions(permissions, 1233)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 在申请码和返回码都符合的时候，接收返回的数据
        if (requestCode == 1 && resultCode == 2) {
            val id = findViewById<TextView>(R.id.textView_id)
            val nick = findViewById<TextView>(R.id.textView_name)
            if (data != null) {
                this.USERID = data.getStringExtra("id")
                val tmpid = this.USERID
                val tmpname = data.getStringExtra("name")
                id.text = tmpid
                nick.text = tmpname
            }
        }
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
            finish()
            val jhk = JavaHttpKolley()
            jhk.setOffline(this.USERID)
            System.exit(0)
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