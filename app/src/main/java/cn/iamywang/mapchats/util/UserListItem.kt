package cn.iamywang.mapchats.util

class UserListItem constructor(id: String, name: String, msg: String, time: String, num: String) {
    var user_id: String = ""
    var user_name: String = ""
    var user_msg: String = ""
    var user_time: String = ""
    var user_num: String = ""

    init {
        this.user_id = id
        this.user_name = name
        this.user_msg = msg
        this.user_time = time
        this.user_num = num
    }
}