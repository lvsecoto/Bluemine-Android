package com.lvsecoto.bluemine.data.vo

const val DEFAULT_PORT = 80

data class RetrofitSetting(
    var hostName: String,
    var port: Int = DEFAULT_PORT,
    var userName: String,
    var password: String
)
