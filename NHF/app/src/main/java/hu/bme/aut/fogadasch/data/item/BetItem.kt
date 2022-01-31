package hu.bme.aut.fogadasch.data.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "betitem")
data class BetItem (
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "suspect") var suspect: Boolean,
    @ColumnInfo(name = "category") var category: Category,
    @ColumnInfo(name = "name1") var name1: String,
    @ColumnInfo(name = "name2") var name2: String,
    @ColumnInfo(name = "odds1") var odds1: Double,
    @ColumnInfo(name = "odds2") var odds2: Double,
    @ColumnInfo(name = "score1") var score1: Int,
    @ColumnInfo(name = "score2") var score2: Int,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "oddsX") var oddsX: Double,
) {
    @Serializable
    enum class Category{
        FOOTBALL, BASKETBALL;
        companion object {
            @JvmStatic
            @TypeConverter
            fun getByOrdinal(ordinal: Int): Category? {
                var ret: Category? = null
                for (cat in values()) {
                    if (cat.ordinal == ordinal) {
                        ret = cat
                        break
                    }
                }
                return ret
            }

            @JvmStatic
            @TypeConverter
            fun toInt(category: Category): Int{
                return category.ordinal
            }

            fun getCategoryName(category: Category): String{
                return when (category){
                    FOOTBALL -> {
                        "Foci"
                    }
                    BASKETBALL -> {
                        "Kosárlabda"
                    }
                }
            }
        }
    }
}
