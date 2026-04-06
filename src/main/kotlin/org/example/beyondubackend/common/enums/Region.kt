package org.example.beyondubackend.common.enums

enum class Region(
    val displayName: String,
) {
    EUROPE("유럽"),
    ASIA("아시아"),
    NORTH_AMERICA("북미"),
    SOUTH_AMERICA("남미"),
    OCEANIA("오세아니아"),
    AFRICA("아프리카"),
    ;

    companion object {
        fun fromDisplayName(name: String): Region? = entries.find { it.displayName == name }
    }
}
