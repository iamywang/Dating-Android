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

class UserInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        val infointent = intent
        val infoid = infointent.getStringExtra("id").toString()
        val infoloc = infointent.getStringExtra("loc").toString()
        Http.init(this)
        Http.post {
            url = "http://10.27.246.15/getUserInfo/"
            params {
                "id" - infoid
            }
            onSuccess { bytes ->
                val str = bytes.toString(Charset.defaultCharset())
                val list = JSON.parseObject(str)
                val id: TextView = findViewById(R.id.user_e1)
                val nick: EditText = findViewById(R.id.user_e2)
                val sex: EditText = findViewById(R.id.user_e3)
                val birth: EditText = findViewById(R.id.user_e4)
                val height: EditText = findViewById(R.id.user_e5)
                val weight: EditText = findViewById(R.id.user_e6)
                val online: TextView = findViewById(R.id.user_e7)
                val regdate: TextView = findViewById(R.id.user_e8)
                val loc: TextView = findViewById(R.id.user_e9)
                id.setText(list["id"].toString())
                nick.setText(list["name"].toString())
                sex.setText(list["sex"].toString())
                birth.setText(list["birth"].toString())
                height.setText(list["height"].toString())
                weight.setText(list["weight"].toString())
                online.setText(list["online"].toString())
                regdate.setText(list["reg"].toString())
                loc.setText(infoloc)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun Submit(view: View){
        val id: TextView = findViewById(R.id.user_e1)
        val nick: EditText = findViewById(R.id.user_e2)
        val sex: EditText = findViewById(R.id.user_e3)
        val birth: EditText = findViewById(R.id.user_e4)
        val height: EditText = findViewById(R.id.user_e5)
        val weight: EditText = findViewById(R.id.user_e6)
        val regdate: TextView = findViewById(R.id.user_e8)
        Http.init(this)
        val act = this
        Http.post {
            url = "http://10.27.246.15/updateUser/"
            params{
                "id" - id.text.toString()
                "name" - nick.text.toString()
                "sex" - sex.text.toString()
                "birth" - birth.text.toString()
                "height" - height.text.toString()
                "weight" - weight.text.toString()
                "reg" - regdate.text.toString()
                "login" - regdate.text.toString()
            }
            onSuccess { bytes ->
                // handle data
                Toast.makeText(act, bytes.toString(Charset.defaultCharset()), Toast.LENGTH_LONG).show()
                act.finish()
            }
        }
    }
}
