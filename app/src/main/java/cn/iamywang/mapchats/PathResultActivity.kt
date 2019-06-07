package cn.iamywang.mapchats

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast

class PathResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path_result)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        val path_intent = intent

        val info_id = path_intent.getStringExtra("id").toString()
        val info_nick = path_intent.getStringExtra("nick").toString()
        val info_num = path_intent.getStringExtra("num").toString()
        val info_time = path_intent.getStringExtra("time").toString()
        val info_length = path_intent.getStringExtra("length").toString()
        val info_start = path_intent.getStringExtra("start").toString()

        val id_View: TextView = findViewById(R.id.result_id);
        val nick_View: TextView = findViewById(R.id.result_nick);
        val start_View: TextView = findViewById(R.id.result_time);
        val length_View: TextView = findViewById(R.id.result_length);
        val num_View: TextView = findViewById(R.id.result_tag);
        val time_View: TextView = findViewById(R.id.result_run);
        val speed_View: TextView = findViewById(R.id.result_speed);

        val min = info_time.split(':')[0].toInt()
        val sec = info_time.split(':')[1].toInt()
        val speed = String.format("%.2f", (3.6 * info_length.toDouble() / (min * 60 + sec))) + "km/h"

        id_View.setText(info_id)
        nick_View.setText(info_nick)
        start_View.setText(info_start)
        length_View.setText(String.format("%.2f", info_length.toDouble()))
        num_View.setText(info_num)
        time_View.setText(info_time)
        speed_View.setText(speed)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun lookPathLine(view: View) {
        val intent = Intent(this, HisrotyLocationActivity::class.java)
        intent.putExtra("id", findViewById<TextView>(R.id.result_id).text)
        intent.putExtra("path", findViewById<TextView>(R.id.result_tag).text)
        startActivity(intent)
    }

    fun sharePath(view: View) {
        Toast.makeText(this, "分享功能正在开发...", Toast.LENGTH_LONG).show()
    }
}
