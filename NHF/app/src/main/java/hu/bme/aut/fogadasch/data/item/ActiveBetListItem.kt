package hu.bme.aut.fogadasch.data.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activebets")
data class ActiveBetListItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "item")  var rowItem: String,
    @ColumnInfo(name = "oddsSum") var oddsSum: Double,
    @ColumnInfo(name = "basicBet") var basicBet: Double,
    @ColumnInfo(name = "maxPrize") var maxPrize: Double,
    @ColumnInfo(name = "kombiMin") var kombiMin: Int,
    @ColumnInfo(name = "kombiMax") var kombiMax: Int

)
