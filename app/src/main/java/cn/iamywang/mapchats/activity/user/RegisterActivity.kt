package cn.iamywang.mapchats.activity.user

import android.icu.util.Calendar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import cn.iamywang.mapchats.R
import cn.iamywang.mapchats.util.network.JavaHttpKolley
import com.ohmerhe.kolley.request.Http
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
        val hou = String.format("%0" + 2 + "d", hour)
        val min = String.format("%0" + 2 + "d", minute)
        val sec = String.format("%0" + 2 + "d", second)
        val rand = Random(hour * minute * sec.toInt())
        val str = StringBuffer("")
        str.append(year).append(".").append(month).append(".").append(day).append(" ").append(hou).append(":")
            .append(min).append(":").append(sec)
        id.setText(rand.nextInt(100000).toString())
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
        val passwd: EditText = findViewById(R.id.register_e10)
        val sex: EditText = findViewById(R.id.register_e3)
        val birth: EditText = findViewById(R.id.register_e4)
        val height: EditText = findViewById(R.id.register_e5)
        val weight: EditText = findViewById(R.id.register_e6)
        val regdate: TextView = findViewById(R.id.register_e8)
        Http.init(this)
        val jhk = JavaHttpKolley()
        jhk.registerUser(
            id.text.toString(),
            nick.text.toString(),
            passwd.text.toString(),
            sex.text.toString(),
            birth.text.toString(),
            height.text.toString(),
            weight.text.toString(),
            regdate.text.toString(),
            this
        )
    }
}
