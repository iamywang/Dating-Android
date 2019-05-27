package cn.iamywang.mapchats

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.ohmerhe.kolley.request.Http
import java.nio.charset.Charset

class UserInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val infointent = intent
        val infoid = infointent.getStringExtra("id").toString()
        Http.init(this)
        Http.post {
            url = "http://192.168.2.234:8000/getUserInfo/"
            params {
                "id" - infoid
            }
            onSuccess { bytes ->
                val str = bytes.toString(Charset.defaultCharset())
                data class A(
                    val id: Int,
                    val name: String,
                    val birth: String,
                    val sex: String,
                    val height: Int,
                    val weight: Int,
                    val online: String,
                    val reg: String
                )
                val list = Gson().fromJson(str, A::class.java)
                val id: EditText = findViewById(R.id.user_e1)
                id.setText(infoid)
//                val nick: EditText = findViewById(R.id.user_e2)
//                val sex: EditText = findViewById(R.id.user_e3)
//                val birth: EditText = findViewById(R.id.user_e4)
//                val height: EditText = findViewById(R.id.user_e5)
//                val weight: EditText = findViewById(R.id.user_e6)
//                val online: EditText = findViewById(R.id.user_e7)
//                val regdate: EditText = findViewById(R.id.user_e8)
            }
        }
    }

    fun Submit(view: View){
        val id: EditText = findViewById(R.id.user_e1)
        val nick: EditText = findViewById(R.id.user_e2)
        val sex: EditText = findViewById(R.id.user_e3)
        val birth: EditText = findViewById(R.id.user_e4)
        val height: EditText = findViewById(R.id.user_e5)
        val weight: EditText = findViewById(R.id.user_e6)
        val regdate: EditText = findViewById(R.id.user_e8)
        Http.init(this)
        val act = this
        Http.post {
            url = "http://192.168.2.234:8000/updateUser/"
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
