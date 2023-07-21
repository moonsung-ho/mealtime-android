package com.sungho0205.geupsik.model

// 1.난류, 2.우유, 3.메밀, 4.땅콩, 5.대두, 6.밀, 7.고등어, 8.게, 9.새우, 10.돼지고기, 11.복숭아, 12.토마토,
// 13.아황산류, 14.호두, 15.닭고기, 16.쇠고기, 17.오징어, 18.조개류(굴, 전복, 홍합 포함), 19.잣
enum class EAlergy(val id: String, val label: String) {
    Egg("1", "난류"),
    Milk("2", "우유"),
    Buckwheat("3", "메밀"),
    Peanut("4", "땅콩"),
    Soybean("5", "대두"),
    Mill("6", "밀"),
    Mackerel("7", "고등어"),
    Crab("8", "게"),
    Shrimp("9", "새우"),
    Pork("10", "돼지고기"),
    Peach("11", "복숭아"),
    Tomato("12", "토마토"),
    Sulfites("13", "아황산류"),
    Walnut("14", "호두"),
    Chicken("15", "닭고기"),
    Beef("16", "쇠고기"),
    Squid("17", "오징어"),
    Clams("18", "조개류"),
    PineNut("19", "잣"),
}
