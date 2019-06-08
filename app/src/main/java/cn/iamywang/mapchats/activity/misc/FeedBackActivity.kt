package cn.iamywang.mapchats.activity.misc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import cn.iamywang.mapchats.R
import com.ohmerhe.kolley.request.Http

class FeedBackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        val feed_intent = intent
        val userid = feed_intent.getStringExtra("id").toString()
        val usernick = feed_intent.getStringExtra("nick").toString()
        val id_view:TextView = findViewById(R.id.feed_e1)
        val nick_view:TextView = findViewById(R.id.feed_e2)
        id_view.setText(userid)
        nick_view.setText(usernick)
        Http.init(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun OnButtonClick(view: View){
        this.finish()
        Toast.makeText(this, "反馈成功", Toast.LENGTH_LONG).show()
    }
}
