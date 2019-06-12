package cn.iamywang.mapchats.activity.friend

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.*
import cn.iamywang.mapchats.R
import cn.iamywang.mapchats.util.list.MessageListItem
import cn.iamywang.mapchats.util.list.MessageListItemAdapter
import cn.iamywang.mapchats.util.network.JavaHttpKolley
import java.util.*

class ChatRoomActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var USERID = ""
    private var REMOTE_ID = ""
    private var REMOTE_NAME = ""
    var list = LinkedList<MessageListItem>()
    var mAdapter = MessageListItemAdapter(list, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        val actionBar = findViewById<Toolbar>(R.id.chatbar)
        setSupportActionBar(actionBar)
        window.statusBarColor = getColor(R.color.colorPrimaryDark)
        val toolbar = supportActionBar
        if (toolbar != null) {
            toolbar.setHomeButtonEnabled(true)
            toolbar.setDisplayHomeAsUpEnabled(true)
        }

        val chat_intent = intent
        this.USERID = chat_intent.getStringExtra("local_id").toString()
        this.REMOTE_ID = chat_intent.getStringExtra("remote_id").toString()
        this.REMOTE_NAME = chat_intent.getStringExtra("remote_name").toString()
        title = this.REMOTE_NAME
        actionBar.subtitle = "连接中..."
        actionBar.setLogo(R.drawable.ic_user_color)
        actionBar.logo.setBounds(0, 0, 48, 48)

        val v = findViewById<ConstraintLayout>(R.id.msg_item_view)
        v.visibility = View.INVISIBLE

        val thread_this = this
        val thread = Thread(object : Runnable {
            override fun run() {
                val jhk = JavaHttpKolley()
                while (true) {
                    jhk.getChatMessages(USERID, REMOTE_ID, thread_this)
                    Thread.sleep(3000)
                }
            }
        })
        thread.start()
    }

    fun setAdapter() {
        val list_view = findViewById<ListView>(R.id.message_list)
        list_view.dividerHeight = 0
        list_view.adapter = mAdapter
        list_view.setOnItemClickListener(this)
        list_view.setSelection(mAdapter.count - 1)
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

    fun sendMsg(view: View) {
        val text_view = findViewById<EditText>(R.id.chat_text)
        val jhk = JavaHttpKolley()
        jhk.addNewMessage(this.USERID, this.REMOTE_ID, text_view.text.toString(), this)
        text_view.text.clear()
    }
}
