package cn.iamywang.mapchats

import android.graphics.Color
import android.icu.util.Calendar
import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.amap.api.maps.model.LatLng
import com.ohmerhe.kolley.request.Http
import java.lang.Double
import java.nio.charset.Charset
import kotlin.random.Random

class JavaHttpKolley {
    val root = "http://192.168.43.241"
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
            url = root + "/addLocation/"
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
        var res: String
        Http.post {
            url = root + "/getLocations/"
            params {
                "key" - id
            }
            onSuccess { bytes ->
                // handle data
                res = bytes.toString(Charset.defaultCharset())
                val array = JSON.parseArray(res)
                var index: Int
                for (i in array.indices) {
                    index = array.getJSONObject(i).getString("road").toInt()
                    if (index == 0) {
                        val lloc = array.getJSONObject(i).getString("location")
                        val str = array.getJSONObject(i).getString("time")
                        val uid = "ID：" + id
                        val loc1 = lloc.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                        val loc2 = lloc.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                        val l = LatLng(Double.parseDouble(loc1), Double.parseDouble(loc2))
                        act.addHisLoc(l, uid, str);
                    }
                }
            }
        }
    }

    fun getOnlineUser(id: String, act: MapDemoActivity) {
        var res: String
        Http.post {
            url = root + "/getLocations/"
            params {
                "key" - "web"
            }
            onSuccess { bytes ->
                // handle data
                res = bytes.toString(Charset.defaultCharset())
                val array = JSON.parseArray(res)
                for (i in array.indices) {
                    val lloc = array.getJSONObject(i).getString("location")
                    val str = "时间：" + array.getJSONObject(i).getString("time")
                    val uid = "ID：" + array.getJSONObject(i).getString("id")
                    val loc1 = lloc.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    val loc2 = lloc.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    val l = LatLng(Double.parseDouble(loc1), Double.parseDouble(loc2))
                    act.addShareLoc(l, uid, str);
                }
            }
        }
    }

    fun getHisRoadList(id: String, act: HisrotyLocationActivity) {
        val list = ArrayList<String>()
        Http.post {
            url = root + "/getHistoryRoadList/"
            params {
                "id" - id
            }
            onSuccess { bytes ->
                val res = bytes.toString(Charset.defaultCharset())
                val array = JSON.parseArray(res)
                for (i in array.indices) {
                    val road = array.getJSONObject(i).getString("road")
                    list.add(road)
                    getHisLine(id, road, act)
                }
            }
        }
    }

    fun getHisLine(id: String, road: String, act: HisrotyLocationActivity) {
        var res: String
        Http.post {
            url = root + "/getHistoryRoad/"
            params {
                "id" - id
                "road" - road
            }
            onSuccess { bytes ->
                // handle data
                res = bytes.toString(Charset.defaultCharset())
                val latlngList = ArrayList<LatLng>()
                val array = JSON.parseArray(res)
                for (i in array.indices) {
                    val lloc = array.getJSONObject(i).getString("location")
                    val loc1 = lloc.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    val loc2 = lloc.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    val l = LatLng(Double.parseDouble(loc1), Double.parseDouble(loc2))
                    latlngList.add(l)
                }
                val rand = Random(road.toInt())
                val color = Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255))
                act.addHisLine(latlngList, color)
            }
        }
    }

    fun addRoad(id: String, index: String) {
        Http.post {
            url = root + "/addRoad/"
            params {
                "id" - id
                "road" - index
            }
            onSuccess {

            }
        }
    }

    fun addCurLine(userid: String, index: String, loc: String) {
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
        val str = StringBuffer("")
        str.append(year).append(".").append(month).append(".").append(day).append(" ").append(hou).append(":")
            .append(min).append(":").append(sec)
        Http.post {
            url = root + "/addLocation/"
            params {
                "id" - userid
                "road" - index
                "time" - str.toString()
                "location" - loc
            }
            onSuccess {

            }
        }
    }
}