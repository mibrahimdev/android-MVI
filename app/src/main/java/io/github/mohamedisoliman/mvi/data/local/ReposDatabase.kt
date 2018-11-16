package io.github.mohamedisoliman.mvi.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import io.github.mohamedisoliman.mvi.data.RepositoryItem
import io.reactivex.Completable
import io.reactivex.Observable

/**
 *
 * Created by Mohamed Ibrahim on 11/13/18.
 */
@Database(entities = [RepositoryItem::class], version = 1, exportSchema = false)
public abstract class ReposDatabase : RoomDatabase() {

  abstract fun reposDao(): ReposDao

  fun insert(repositoryItem: RepositoryItem): Completable {
    return Completable.fromAction { reposDao().insert(repositoryItem) }
  }

  fun getSavedRepos(): Observable<List<RepositoryItem>> = reposDao().recipes.toObservable()
}