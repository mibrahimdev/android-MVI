package io.github.mohamedisoliman.mvi.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.github.mohamedisoliman.mvi.data.RepositoryItem
import io.reactivex.Single

/**
 *
 * Created by Mohamed Ibrahim on 11/14/18.
 */
@Dao
interface ReposDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(repositoryItem: RepositoryItem)

  @get:Query("SELECT * FROM RepositoryItem")
  val recipes: Single<List<RepositoryItem>>
}