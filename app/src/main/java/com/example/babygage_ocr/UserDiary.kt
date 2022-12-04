package com.example.babygage_ocr

import java.sql.Timestamp

data class UserDiary(

    // 사용자 이름(닉네임)
//   var userName : String,
    // 사용자 아이디 (이메일)
   var useId : String? = null,
    // 현재 사용자(로그인한)
   var uid : String? = null,
    // 다이어리
    var diary : String? = null,
    // 날짜
    var date : String? = null
){}
