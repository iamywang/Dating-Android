package cn.iamywang.mapchats.activity.path

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.MenuItem
import android.widget.ListView
import cn.iamywang.mapchats.R
import cn.iamywang.mapchats.util.list.HistoryPathItem
import cn.iamywang.mapchats.util.list.HistoryPathItemAdapter
import cn.iamywang.mapchats.util.network.JavaHttpKolley
import java.util.*
import android.widget.AdapterView
import android.view.View


class HistoryPathListActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    val list = LinkedList<HistoryPathItem>()
    val mAdapter = HistoryPathItemAdapter(list, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_path_list)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val v = findViewById<ConstraintLayout>(R.id.path_item_view)
        v.visibility = View.INVISIBLE

        val user_id = intent.getStringExtra("id").toString()
        val jhk = JavaHttpKolley()
        jhk.getHistoryPathList(user_id, this)
    }

    fun setAdapter() {
        val list_view = findViewById<ListView>(R.id.path_list)
        list_view.adapter = mAdapter
        list_view.setOnItemClickListener(this)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent(this, HisrotyLocationActivity::class.java)
        intent.putExtra("id", list.get(position).user_id)
        intent.putExtra("path", list.get(position).path_id)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
