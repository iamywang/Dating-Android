package cn.iamywang.mapchats

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.ohmerhe.kolley.request.Http
import java.nio.charset.Charset

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onButtonClick(view: View){
        val user:EditText = findViewById(R.id.editText)
        val pswd:EditText = findViewById(R.id.editText2)
        Http.init(this)
        val act = this
        Http.post {
            url = "http://192.168.2.234:8000/login/"
            params{
                "id" - user.text.toString()
                "password" - pswd.text.toString()
            }
            onSuccess { bytes ->
                // handle data
                val str = bytes.toString(Charset.defaultCharset())
                data class A(
                    val id: Int,
                    val name: String,
                    val birth: String,
                    val sex: String,
                    val height: Int,
                    val weight: Int,
                    val online: String,
                    val friends: Int,
                    val reg: String,
                    val login: String
                ){
                    override fun toString(): String {
                        return this.name
                    }
                }
                val list = Gson().fromJson<A>(str, A::class.java)
                val lintent = intent
                lintent.putExtra("id", user.text.toString())
                lintent.putExtra("name", "user")
                act.setResult(2, lintent)
                Toast.makeText(act, "登录成功", Toast.LENGTH_LONG).show()
                act.finish()
            }
        }
    }

    fun onButtonClick1(view: View){
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
