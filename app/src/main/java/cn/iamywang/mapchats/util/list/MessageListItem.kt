package cn.iamywang.mapchats.util.list

class MessageListItem constructor(id: String, name: String, sex: String, msg: String, time: String, read: String) {
    var message_id: String = ""
    var message_name: String = ""
    var message_sex: String = ""
    var message_msg: String = ""
    var message_time: String = ""
    var message_read: String = ""

    init {
        this.message_id = id
        this.message_name = name
        this.message_sex = sex
        this.message_msg = msg
        this.message_time = time
        this.message_read = read
    }
}