package hu.bme.aut.fogadasch.data

import androidx.room.*
import hu.bme.aut.fogadasch.data.item.ActiveBetListItem

@Dao
interface ActiveBetDao {
    @Query("SELECT * FROM activebets")
    fun getAll(): List<ActiveBetListItem>

    @Insert
    fun insert(activeBetListItem: ActiveBetListItem): Long

    @Update
    fun update(activeBetListItem: ActiveBetListItem)

    @Delete
    fun delete(activeBetListItem: ActiveBetListItem)
}