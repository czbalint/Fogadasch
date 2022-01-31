package hu.bme.aut.fogadasch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.bme.aut.fogadasch.data.item.BetItem
import hu.bme.aut.fogadasch.data.item.ActiveBetListItem

@Database(entities = [BetItem::class, ActiveBetListItem::class], version = 2)
abstract class BetItemDatabase : RoomDatabase() {
    abstract fun BetItemDao(): BetItemDao
    abstract fun ActiveBetDao(): ActiveBetDao

    companion object{
        fun getDatabase(applicationContext: Context): BetItemDatabase {
            return  Room.databaseBuilder(
                applicationContext,
                BetItemDatabase::class.java,
                "bet-list"
            ).build()
        }
    }
}