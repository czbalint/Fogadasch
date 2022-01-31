package hu.bme.aut.fogadasch.data.item

import kotlinx.serialization.Serializable

@Serializable
data class NewBetItem (
    var teamName1: String,
    var teamName2: String,
    var selectedOdds: BetCategory,
    var selectedOddsValue: Double,
    var containBetItem: BetItem
) {
    @Serializable
    enum class BetCategory{
        HOME, X, AWAY;
        companion object {
            fun getNameByCat(category: BetCategory): String {
                return when (category) {
                    HOME -> {
                        "Hazai"
                    }
                    X -> {
                        "Döntetlen"
                    }
                    AWAY -> {
                        "Idegen"
                    }
                }
            }
        }
    }
}