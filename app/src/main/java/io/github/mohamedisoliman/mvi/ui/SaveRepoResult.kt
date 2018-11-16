package io.github.mohamedisoliman.mvi.ui

import io.github.mohamedisoliman.mvi.mvibase.MviResult

/**
 *
 * Created by Mohamed Ibrahim on 11/12/18.
 */
sealed class SaveRepoResult : MviResult {
  data class Success(val message: String) : SaveRepoResult()
}