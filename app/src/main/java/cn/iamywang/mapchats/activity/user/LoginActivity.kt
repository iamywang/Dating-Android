package cn.iamywang.mapchats.activity.user

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import cn.iamywang.mapchats.MainActivity
import cn.iamywang.mapchats.R
import cn.iamywang.mapchats.util.JavaHttpKolley
import com.ohmerhe.kolley.request.Http
import java.nio.charset.Charset


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )
        requestPermissions(permissions, 1233)
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
        jhk.loginApp(user.text.toString(), pswd.text.toString(), this)
    }

    fun onButtonClick1(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun startMain(id: String, name: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("name", name)
        startActivity(intent)
        this.finish()
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show()

    }
}
