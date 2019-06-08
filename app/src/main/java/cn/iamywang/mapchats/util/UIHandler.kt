package cn.iamywang.mapchats.util

import android.os.Handler
import java.lang.ref.WeakReference

open class UIHandler<T>(cls: T) : Handler() {

    protected var ref: WeakReference<T>? = null

    init {
        ref = WeakReference(cls)
    }

    fun getRef(): T? {
        return if (ref != null) ref!!.get() else null
    }

}
