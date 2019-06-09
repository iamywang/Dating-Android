package cn.iamywang.mapchats.activity.friend

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import cn.iamywang.mapchats.R

class ChatRoomActivity : AppCompatActivity() {
    private var USERID = ""
    private var REMOTE_ID = ""
    private var REMOTE_NAME = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val chat_intent = intent
        this.USERID = chat_intent.getStringExtra("local_id").toString()
        this.REMOTE_ID = chat_intent.getStringExtra("remote_id").toString()
        this.REMOTE_NAME = chat_intent.getStringExtra("remote_name").toString()
        title = this.REMOTE_NAME
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
