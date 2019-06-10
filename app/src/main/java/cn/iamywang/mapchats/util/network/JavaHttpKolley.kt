package cn.iamywang.mapchats.util.network

import android.graphics.Color
import android.icu.util.Calendar
import android.widget.Toast
import cn.iamywang.mapchats.activity.friend.ChatRoomActivity
import cn.iamywang.mapchats.activity.user.MainActivity
import cn.iamywang.mapchats.activity.friend.FriendsListActivity
import cn.iamywang.mapchats.activity.friend.LocationShareActivity
import cn.iamywang.mapchats.activity.path.HisrotyLocationActivity
import cn.iamywang.mapchats.activity.path.HistoryPathListActivity
import cn.iamywang.mapchats.activity.user.LoginActivity
import cn.iamywang.mapchats.activity.user.RegisterActivity
import cn.iamywang.mapchats.util.list.HistoryPathItem
import cn.iamywang.mapchats.util.list.MessageListItem
import cn.iamywang.mapchats.util.list.UserListItem
import com.alibaba.fastjson.JSON
import com.amap.api.maps.model.LatLng
import com.ohmerhe.kolley.request.Http
import java.lang.Double
import java.nio.charset.Charset
import kotlin.collections.ArrayList
import kotlin.random.Random

class JavaHttpKolley {
    val root = "http://39.105.44.114:58000"

    fun addLocation(id: String, loc: String, act: LocationShareActivity) {
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

    fun getHisMarker(id: String, act: LocationShareActivity) {
        var res: String
        Http.post {
            url = root + "/getAllLocations/"
            params {
                "id" - id
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

    fun getOnlineUser(id: String, act: LocationShareActivity) {
        var res: String
        Http.post {
            url = root + "/getOnlineUsers/"
            params {
                "id" - id
            }
            onSuccess { bytes ->
                // handle data
                res = bytes.toString(Charset.defaultCharset())
                val array = JSON.parseArray(res)
                for (i in array.indices) {
                    if (array.getJSONObject(i).getString("id") != id) {
                        val lloc = array.getJSONObject(i).getString("location")
                        val str = array.getJSONObject(i).getString("time")
                        val uid = "ID：" + array.getJSONObject(i).getString("id")
                        val loc1 = lloc.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                        val loc2 = lloc.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                        val l = LatLng(Double.parseDouble(loc1), Double.parseDouble(loc2))
                        act.addShareLoc(l, uid, str);
                    }
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
                act.PATHID = road
                act.addHisLine(
                    latlngList,
                    color,
                    array.getJSONObject(0).getString("time"),
                    array.getJSONObject(array.size - 1).getString("time")
                )
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

    fun loginApp(id: String, passwd: String, act: LoginActivity) {
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
        val date = StringBuffer("")
        date.append(year).append(".").append(month).append(".").append(day).append(" ").append(hou).append(":")
            .append(min).append(":").append(sec)
        Http.post {
            url = root + "/login/"
            params {
                "id" - id
                "password" - passwd
                "date" - date.toString()
            }
            onSuccess { bytes ->
                // handle data
                val str = bytes.toString(Charset.defaultCharset())
                val list = JSON.parseObject(str)
                val lintent = act.intent
                lintent.putExtra("id", id)
                lintent.putExtra("name", list["name"].toString())
                lintent.putExtra("sex", list["sex"].toString())
                act.setResult(110, lintent)
                Toast.makeText(act, "登录成功", Toast.LENGTH_LONG).show()
                act.finish()
            }
        }
    }

    fun registerUser(
        id: String,
        nick: String,
        passwd: String,
        sex: String,
        birth: String,
        height: String,
        weight: String,
        regdate: String,
        act: RegisterActivity
    ) {
        Http.post {
            url = root + "/insertUser2/"
            params {
                "id" - id
                "name" - nick
                "passwd" - passwd
                "sex" - sex
                "birth" - birth
                "height" - height
                "weight" - weight
                "reg" - regdate
                "login" - regdate
            }
            onSuccess { bytes ->
                // handle data
                Toast.makeText(act, bytes.toString(Charset.defaultCharset()), Toast.LENGTH_LONG).show()
                act.finish()
            }
        }
    }

    fun setOffline(id: String, act: MainActivity) {
        Http.post {
            url = root + "/setOffline/"
            params {
                "id" - id
            }
            onSuccess {
                act.finish()
                System.exit(0)
            }
        }
    }

    fun getHistoryPathList(id: String, act: HistoryPathListActivity) {
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
                    Http.post {
                        url = root + "/getHistoryRoad/"
                        params {
                            "id" - id
                            "road" - road
                        }
                        onSuccess { bytes ->
                            // handle data
                            val array2 = JSON.parseArray(bytes.toString(Charset.defaultCharset()))
                            val path_time = array2.getJSONObject(0).getString("time")
                            val path_loc = array2.getJSONObject(0).getString("location")
                            act.list.add(HistoryPathItem(id, road, path_time, path_loc))
                            act.setAdapter()
                        }
                    }
                }
            }
        }
    }

    fun getUserList(act: FriendsListActivity) {
        Http.get {
            url = root + "/getUsers/?key=all"
            onSuccess { bytes ->
                val res = bytes.toString(Charset.defaultCharset())
                val array = JSON.parseArray(res)
                for (i in array.indices) {
                    val userid = array.getJSONObject(i).getString("id")
                    val username = array.getJSONObject(i).getString("name")
                    val online = array.getJSONObject(i).getString("online")
                    val sex = array.getJSONObject(i).getString("sex")
                    if (online == "online") {
                        act.list.add(
                            UserListItem(
                                userid,
                                username,
                                sex,
                                "[在线]",
                                "",
                                "0"
                            )
                        )
                    } else {
                        act.list.add(
                            UserListItem(
                                userid,
                                username,
                                sex,
                                "[离线]",
                                "",
                                "0"
                            )
                        )
                    }
                    act.setAdapter()
                }
            }
        }
    }

    fun getFakeUserMsgList(act: MainActivity) {
        Http.get {
            url = root + "/getUsers/?key=all"
            onSuccess { bytes ->
                val res = bytes.toString(Charset.defaultCharset())
                val array = JSON.parseArray(res)
                act.list.add(
                    UserListItem("0", "聊天室", "room", "点击进入聊天室", "", "0")
                )
                act.setAdapter()
                for (i in array.indices) {
                    val userid = array.getJSONObject(i).getString("id")
                    val username = array.getJSONObject(i).getString("name")
                    val sex = array.getJSONObject(i).getString("sex")
                    act.list.add(
                        UserListItem(
                            userid,
                            username,
                            sex,
                            "点击开始聊天",
                            "",
                            "0"
                        )
                    )
                    act.setAdapter()
                }
            }
        }
    }

    fun getChatMessages(local_id: String, remote_id: String, act: ChatRoomActivity) {
        Http.post {
            url = root + "/getUserMessages/"
            params {
                "id1" - local_id
                "id2" - remote_id
            }
            onSuccess { bytes ->
                val res = bytes.toString(Charset.defaultCharset())
                val array = JSON.parseArray(res)
                act.list.clear()
                act.setAdapter()
                for (i in array.indices) {
                    val id1 = array.getJSONObject(i).getString("id1")
                    val nick1 = array.getJSONObject(i).getString("nick1")
                    val sex = array.getJSONObject(i).getString("sex")
                    val msg = array.getJSONObject(i).getString("msg")
                    val time = array.getJSONObject(i).getString("time")
                    val read = array.getJSONObject(i).getString("read")
                    act.list.add(MessageListItem(id1, nick1, sex, msg, time, read))
                    act.setAdapter()
                }
            }
        }
    }

    fun addNewMessage(local_id: String, remote_id: String, msg: String, act: ChatRoomActivity) {
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
        val date = StringBuffer("")
        date.append(year).append(".").append(month).append(".").append(day).append(" ").append(hou).append(":")
            .append(min).append(":").append(sec)
        Http.post {
            url = root + "/addMessage/"
            params {
                "id1" - local_id
                "id2" - remote_id
                "time" - date.toString()
                "location" - "济南"
                "model" - "Android"
                "msg" - msg
            }
            onSuccess {
                getChatMessages(local_id, remote_id, act)
            }
        }
    }
}