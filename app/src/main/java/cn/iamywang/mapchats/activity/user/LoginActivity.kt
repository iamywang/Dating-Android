package cn.iamywang.mapchats.activity.user

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import cn.iamywang.mapchats.R
import cn.iamywang.mapchats.util.JavaHttpKolley
import com.ohmerhe.kolley.request.Http


class LoginActivity : AppCompatActivity() {

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
        val jhk = JavaHttpKolley()
        jhk.loginApp(user.text.toString(), pswd.text.toString(),this)
    }

    fun onButtonClick1(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
