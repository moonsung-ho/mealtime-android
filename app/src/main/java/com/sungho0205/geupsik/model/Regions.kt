package com.sungho0205.geupsik.model

enum class Regions(val label: String, val value: String) {
    All("전체", ""),
    Seoul("서울", "서울특별시"),
    Gyeonggi("경기", "경기도"),
    Busan("부산", "부산광역시"),
    Daegu("대구", "대구광역시"),
    Incheon("인천", "인천광역시"),
    Gwangju("광주", "광주광역시"),
    Daejeon("대전", "대전광역시"),
    Ulsan("울산", "울산광역시"),
    Sejong("세종", "세종특별자치시"),
    Choongcheong("충북", "충청북도"),
    Choongnam("충남", "충청남도"),
    Jeonbuk("전북", "전라북도"),
    Jeonnam("전남", "전라남도"),
    Gyeongbuk("경북", "경상북도"),
    Gyeongnam("경남", "경상남도"),
    Kangwon("강원", "강원특별자치도"),
    Jeju("제주", "제주특별자치도"),
}