package cn.iamywang.mapchats.activity.user

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cn.iamywang.mapchats.R
import com.alibaba.fastjson.JSON
import com.ohmerhe.kolley.request.Http
import java.nio.charset.Charset

class UserInfoActivity : AppCompatActivity() {

    val root = "http://39.105.44.114:58000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        val info_intent = intent
        val info_id = info_intent.getStringExtra("id").toString()
        val info_loc = info_intent.getStringExtra("loc").toString()
        val id: TextView = findViewById(R.id.user_e1)
        val loc: TextView = findViewById(R.id.user_e9)
        id.setText(info_id)
        loc.setText(info_loc)
        getInfo(info_id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun getInfo(info_id: String) {
        Http.init(this)
        Http.post {
            url = root + "/getUserInfo/"
            params {
                "id" - info_id
            }
            onSuccess { bytes ->
                val str = bytes.toString(Charset.defaultCharset())
                val list = JSON.parseObject(str)
                val head_name: TextView = findViewById(R.id.user_t_name)
                val head_img: ImageView = findViewById(R.id.user_head)
                val nick: EditText = findViewById(R.id.user_e2)
                val sex: EditText = findViewById(R.id.user_e3)
                val birth: EditText = findViewById(R.id.user_e4)
                val height: EditText = findViewById(R.id.user_e5)
                val weight: EditText = findViewById(R.id.user_e6)
                val regdate: TextView = findViewById(R.id.user_e7)
                val logdate: TextView = findViewById(R.id.user_e8)
                head_name.setText(list["name"].toString())
                nick.setText(list["name"].toString())
                sex.setText(list["sex"].toString())
                if (info_id == "1") {
                    head_img.setImageResource(R.drawable.ic_user_admin)
                } else if (list["sex"].toString() == "男") {
                    head_img.setImageResource(R.drawable.ic_user_color)
                } else if (list["sex"].toString() == "女") {
                    head_img.setImageResource(R.drawable.ic_user_color_2)
                }
                birth.setText(list["birth"].toString())
                height.setText(list["height"].toString())
                weight.setText(list["weight"].toString())
                regdate.setText(list["reg"].toString())
                logdate.setText(list["login"].toString())
            }
        }
    }

    fun Submit(view: View) {
        Http.init(this)
        val act = this
        val id: TextView = findViewById(R.id.user_e1)
        val nick: EditText = findViewById(R.id.user_e2)
        val sex: EditText = findViewById(R.id.user_e3)
        val birth: EditText = findViewById(R.id.user_e4)
        val height: EditText = findViewById(R.id.user_e5)
        val weight: EditText = findViewById(R.id.user_e6)
        val regdate: TextView = findViewById(R.id.user_e7)
        val logdate: TextView = findViewById(R.id.user_e8)
        Http.post {
            url = root + "/updateUser/"
            params {
                "id" - id.text.toString()
                "name" - nick.text.toString()
                "sex" - sex.text.toString()
                "birth" - birth.text.toString()
                "height" - height.text.toString()
                "weight" - weight.text.toString()
                "reg" - regdate.text.toString()
                "login" - logdate.text.toString()
            }
            onSuccess { bytes ->
                // handle data
                Toast.makeText(act, bytes.toString(Charset.defaultCharset()), Toast.LENGTH_LONG).show()
                getInfo(id.text.toString())
            }
        }
    }
}
