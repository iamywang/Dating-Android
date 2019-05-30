package cn.iamywang.mapchats

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



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var USERID = "1"
    private var exitTime=System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.startActivityForResult(Intent(this, LoginActivity::class.java), 1)
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
            this.USERID = data!!.getStringExtra("id")
            val tmpid = "ID：" + this.USERID
            val tmpname = "昵称：" + data.getStringExtra("name")
            id.text = tmpid
            nick.text = tmpname
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
            System.exit(0)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.nav_home) {
            val intent = Intent(this, UserInfoActivity::class.java)
            intent.putExtra("id", this.USERID)
            intent.putExtra("loc", "济南")
            startActivity(intent)
        } else if (id == R.id.nav_map) {
            val intent = Intent(this, MapDemoActivity::class.java)
            intent.putExtra("id", this.USERID)
            startActivity(intent)
        } else if (id == R.id.nav_gps) {
            val intent = Intent(this, HisrotyLocationActivity::class.java)
            intent.putExtra("id", this.USERID)
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
            startActivity(intent)
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "分享功能正在开发...", Toast.LENGTH_LONG).show()
        } else if (id == R.id.nav_about) {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}