package com.app.meatz.data.application

import android.app.Application
import com.app.meatz.presentation.exception.ExceptionActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import kotlin.system.exitProcess

/**
 * Ahmed Elmokadim
 * elmokadim@gmail.com
 * 01/01/2020
 */
class App : Application() {

  companion object {

    lateinit var instance: App
      private set
  }

  override fun onCreate() {
    super.onCreate()
    instance = this
    setUncaughtExceptionHandler()
  }

  private fun setUncaughtExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
      FirebaseCrashlytics.getInstance().recordException(throwable)
      startActivity(intentFor<ExceptionActivity>().clearTask().newTask())
      exitProcess(1)
    }
  }
}