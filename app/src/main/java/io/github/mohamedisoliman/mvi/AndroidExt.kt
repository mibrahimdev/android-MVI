package io.github.mohamedisoliman.mvi

import android.content.Context
import android.widget.Toast

/**
 *
 * Created by Mohamed Ibrahim on 11/16/18.
 */

fun Context.longToast(message: String) {
  Toast.makeText(this, message, Toast.LENGTH_LONG)
      .show()
}