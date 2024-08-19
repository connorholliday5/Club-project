package com.example.finalcoursework2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class League(
    @PrimaryKey(autoGenerate = true) var idLeague: Int = 0,
    val strLeague: String?,
    val strSport: String?,
    val strLeagueAlternate: String?,
)

package com.example.finalcoursework2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LeagueDao {
    @Query("select * from League")
    suspend fun getAll(): List<League>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg league: League)

    // Insert one league without replacing an identical one
    @Insert
    suspend fun insertLeague(league: League)

    @Delete
    suspend fun deleteLeague(league: League)

    // Query to find a league by its name
    @Query("SELECT * FROM League WHERE STRleague LIKE :name")
    fun findByLeague(name: String): League?

    @Query("DELETE FROM League")
    suspend fun deleteAll()

    // Custom query to save data to database
    @Query("INSERT INTO League (strLeague) VALUES (:data)")
    suspend fun saveDataToDatabase(data: String)

    @Query("SELECT * FROM League WHERE strLeague LIKE '%' || :leagueName || '%'")
    suspend fun searchLeaguesByName(leagueName: String): List<League>
}
package com.example.finalcoursework2

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [League::class],version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun leagueDao(): LeagueDao
}
