package cn.iamywang.mapchats

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.ohmerhe.kolley.request.Http
import java.nio.charset.Charset


class LoginActivity : AppCompatActivity() {
    val root = "http://10.27.246.15"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun onButtonClick(view: View) {
        val user: EditText = findViewById(R.id.editText)
        val pswd: EditText = findViewById(R.id.editText2)
        Http.init(this)
        val act = this
        Http.post {
            url = root + "/login/"
            params {
                "id" - user.text.toString()
                "password" - pswd.text.toString()
            }
            onSuccess { bytes ->
                // handle data
                val str = bytes.toString(Charset.defaultCharset())
                val list = JSON.parseObject(str)
                val lintent = intent
                lintent.putExtra("id", user.text.toString())
                lintent.putExtra("name", list["name"].toString())
                act.setResult(2, lintent)
                Toast.makeText(act, "登录成功", Toast.LENGTH_LONG).show()
                act.finish()
            }
        }
    }

    fun onButtonClick1(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
