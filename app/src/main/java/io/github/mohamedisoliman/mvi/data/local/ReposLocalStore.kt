package io.github.mohamedisoliman.mvi.data.local

import android.arch.persistence.room.Room
import android.content.Context
import io.github.mohamedisoliman.mvi.data.RepositoryItem
import io.reactivex.Completable
import io.reactivex.Observable

class ReposLocalStore(private val context: Context) {

  private val database: ReposDatabase by lazy {
    Room.databaseBuilder(context, ReposDatabase::class.java, "androiddev.db")
        .fallbackToDestructiveMigration()
        .build()
  }

  fun insert(repositoryItem: RepositoryItem): Completable = database.insert(repositoryItem)

  fun getSavedRepos(): Observable<List<RepositoryItem>> = database.getSavedRepos()

}
