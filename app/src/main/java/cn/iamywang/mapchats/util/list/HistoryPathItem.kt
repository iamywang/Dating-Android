package cn.iamywang.mapchats.util.list

class HistoryPathItem constructor(user_id: String, path_id: String, start_time: String, start_loc: String) {
    var user_id: String = ""
    var path_id: String = ""
    var start_time: String = ""
    var start_loc: String = ""

    init {
        this.user_id = user_id
        this.path_id = path_id
        this.start_time = start_time
        this.start_loc = start_loc
    }

}