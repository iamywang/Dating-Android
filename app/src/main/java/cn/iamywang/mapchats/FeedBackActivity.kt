package cn.iamywang.mapchats

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.ohmerhe.kolley.request.Http
import java.nio.charset.Charset

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
        val id_view:TextView = findViewById(R.id.feed_e1)
        id_view.setText(userid)
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
