package cn.iamywang.mapchats.activity.misc

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import cn.iamywang.mapchats.MainActivity
import cn.iamywang.mapchats.R
import cn.iamywang.mapchats.util.UIHandler

class SplashActivity : AppCompatActivity() {

    private val mHandler = SplashHandle(this)

    // 将常量放入这里
    companion object {

        // 正常跳转到登录界面 常量 防止以后增加业务逻辑
        val MSG_LAUNCH: Int = 0

        // 延时时间
        val SLEEP_TIME = 800
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

    }

    override fun onResume() {
        super.onResume()

        val start = System.currentTimeMillis()

        /*
        这里计算了两个时间
        两个时间间可以放入判断条件：是否需要自动登录等
         */

        var costTime = System.currentTimeMillis() - start

        val left = SLEEP_TIME - costTime

        // kotlin中取消了java中的三目运算，换成if...else...
        mHandler.postDelayed(runnable, if (left > 0) left else 0)
    }


    val runnable = Runnable {
        kotlin.run {
            val message = mHandler.obtainMessage(MSG_LAUNCH)
            mHandler.sendMessage(message)
        }
    }

    // 弱引用handler内部类
    private class SplashHandle(cls: SplashActivity) : UIHandler<SplashActivity>(cls) {

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            val activity = ref?.get()
            if (null != activity) {

                if (activity.isFinishing)
                    return

                when (msg?.what) {

                    // 正常跳转到登录界面
                    MSG_LAUNCH -> {
                        activity.startActivity(Intent(activity, MainActivity::class.java))
                        activity.finish()
                    }
                }
            }
        }
    }
}