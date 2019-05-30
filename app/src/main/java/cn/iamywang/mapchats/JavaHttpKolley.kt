package cn.iamywang.mapchats

import android.icu.util.Calendar
import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.amap.api.maps.model.LatLng
import com.ohmerhe.kolley.request.Http
import java.lang.Double
import java.nio.charset.Charset

class JavaHttpKolley {
    fun addLocation(id: String, loc: String, act: MapDemoActivity) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val days = String.format("%0" + 2 + "d", day)
        val hou = String.format("%0" + 2 + "d", hour)
        val min = String.format("%0" + 2 + "d", minute)
        val sec = String.format("%0" + 2 + "d", second)
        val str = StringBuffer("")
        str.append(year).append(".").append(month).append(".").append(days).append(" ").append(hou).append(":")
            .append(min).append(":").append(sec)
        Http.post {
            url = "http://10.27.246.15/addLocation/"
            params {
                "id" - id
                "road" - "0"
                "time" - str.toString()
                "location" - loc
            }
            onSuccess { bytes ->
                // handle data
                Toast.makeText(act, bytes.toString(Charset.defaultCharset()), Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getHisMarker(id: String, act: MapDemoActivity) {
        var res = ""
        Http.post {
            url = "http://10.27.246.15/getAllLocations/"
            params {
                "id" - id
            }
            onSuccess { bytes ->
                // handle data
                res = bytes.toString(Charset.defaultCharset())
                val array = JSON.parseArray(res)
                for (i in array.indices) {
                    val index = array.getJSONObject(i).getString("road")
                    if (index == "0") {
                        val lloc = array.getJSONObject(i).getString("location")
                        val loc1 = lloc.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                        val loc2 = lloc.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                        val l = LatLng(Double.parseDouble(loc1), Double.parseDouble(loc2))
                        act.addHisLoc(l, array.getJSONObject(i).getString("time"));
                    }
                }
            }
        }
    }

    fun getHisLine(id: String, act: HisrotyLocationActivity) {
        var res = ""
        Http.post {
            url = "http://10.27.246.15/getAllLocations/"
            params {
                "id" - id
            }
            onSuccess { bytes ->
                // handle data
                res = bytes.toString(Charset.defaultCharset())
                val latlngList = ArrayList<LatLng>()
                val array = JSON.parseArray(res)
                for (i in array.indices) {
                    val index = array.getJSONObject(i).getString("road")
                    if (index != "0") {
                        val lloc = array.getJSONObject(i).getString("location")
                        val loc1 = lloc.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                        val loc2 = lloc.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                        val l = LatLng(Double.parseDouble(loc1), Double.parseDouble(loc2))
                        latlngList.add(l)
                    }
                }
                act.addLocLine(latlngList)
            }
        }
    }

    fun addCurLine(userid: String, index: String, loc: String, act: HisrotyLocationActivity) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val days = String.format("%0" + 2 + "d", day)
        val hou = String.format("%0" + 2 + "d", hour)
        val min = String.format("%0" + 2 + "d", minute)
        val sec = String.format("%0" + 2 + "d", second)
        val str = StringBuffer("")
        str.append(year).append(".").append(month).append(".").append(days).append(" ").append(hou).append(":")
            .append(min).append(":").append(sec)
        Http.post {
            url = "http://10.27.246.15/addLocation/"
            params {
                "id" - userid
                "road" - index
                "time" - str.toString()
                "location" - loc
            }
            onSuccess { bytes ->

            }
        }
    }
}