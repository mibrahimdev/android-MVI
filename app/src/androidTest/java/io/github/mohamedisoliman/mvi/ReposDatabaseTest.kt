package io.github.mohamedisoliman.mvi

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import io.github.mohamedisoliman.mvi.data.RepositoryItem
import io.github.mohamedisoliman.mvi.data.RepositoryOwner
import io.github.mohamedisoliman.mvi.data.local.ReposDatabase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Mohamed Ibrahim on 8/7/18.
 */
@RunWith(AndroidJUnit4::class)
class ReposDatabaseTest {

  private lateinit var database: ReposDatabase

  @Before
  fun setUp() {
    database = Room.inMemoryDatabaseBuilder(
        InstrumentationRegistry.getContext(),
        ReposDatabase::class.java
    )
        .build()
  }

  @Test
  fun doNothing() {

  }

  @Test
  fun insert_item_should_retrieve_same_item() {

    val repoItem =
      RepositoryItem(100, "android-mvi", "mvi playground", RepositoryOwner("MohamedIbrahim", ""))
    val insertTest = database.insert(repoItem)
        .test()
    insertTest.assertSubscribed()
    insertTest.assertComplete()

    val queryTest = database.getSavedRepos()
        .test()
    queryTest.assertResult(listOf(repoItem))
  }
}