package com.theapache64.botscript.model

import com.google.gson.annotations.SerializedName

data class Config(
    @SerializedName("dev_token")
    val devToken: String, // $devToken
    @SerializedName("dev_web_hook_url")
    val devWebHookUrl: String, // $devUrl
    @SerializedName("prod_token")
    val prodToken: String, // $prodToken
    @SerializedName("prod_web_hook_url")
    val prodWebHookUrl: String // $prodUrl
)