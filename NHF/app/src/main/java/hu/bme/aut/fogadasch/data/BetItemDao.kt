package hu.bme.aut.fogadasch.data

import androidx.room.*
import hu.bme.aut.fogadasch.activity.ui.home.Category
import hu.bme.aut.fogadasch.data.item.BetItem

@Dao
interface BetItemDao {
    @Query("SELECT * FROM betitem")
    fun getAll(): List<BetItem>

    @Query("SELECT * FROM betitem WHERE category = 'FOOTBALL'")
    fun getFootball(): List<BetItem>

    @Query("SELECT * FROM betitem WHERE category = 'BASKETBALL'")
    fun getBasketball(): List<BetItem>

    @Query("SELECT * FROM betitem WHERE suspect = 1")
    fun getFavourite(): List<BetItem>

    @Insert
    fun insert(betItem: BetItem): Long

    @Update
    fun update(betItem: BetItem)

    @Delete
    fun deleteItem(betItem: BetItem)
}