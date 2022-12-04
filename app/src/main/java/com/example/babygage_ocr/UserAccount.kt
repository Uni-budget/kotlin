package com.example.babygage_ocr

data class UserAccount(
    // 사용자 이름(닉네임)
    var userName : String? = null,
    // 사용자 아이디 (이메일)
    var userId : String? = null,
    // 사용자 비밀번호
    var userPassword : String? = null,
){}
