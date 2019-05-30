package cn.iamywang.mapchats

import android.icu.util.Calendar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ohmerhe.kolley.request.Http
import java.nio.charset.Charset
import kotlin.random.Random

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        val id: TextView = findViewById(R.id.register_e1)
        val regdate: TextView = findViewById(R.id.register_e8)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val mon = String.format("%0" + 2 + "d", month)
        val days = String.format("%0" + 2 + "d", day)
        val hou = String.format("%0" + 2 + "d", hour)
        val min = String.format("%0" + 2 + "d", minute)
        val sec = String.format("%0" + 2 + "d", second)
        val rand = Random(Calendar.MINUTE * Calendar.SECOND)
        val str = StringBuffer("")
        str.append(year).append(".").append(mon).append(".").append(days).append(" ").append(hou).append(":")
            .append(min).append(":").append(sec)
        id.setText(rand.nextInt(10000).toString())
        regdate.setText(str.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun Submit(view: View) {
        val id: TextView = findViewById(R.id.register_e1)
        val nick: EditText = findViewById(R.id.register_e2)
        val sex: EditText = findViewById(R.id.register_e3)
        val birth: EditText = findViewById(R.id.register_e4)
        val height: EditText = findViewById(R.id.register_e5)
        val weight: EditText = findViewById(R.id.register_e6)
        val regdate: TextView = findViewById(R.id.register_e8)
        Http.init(this)
        val act = this
        Http.post {
            url = "http://10.27.246.15/insertUser/"
            params {
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
