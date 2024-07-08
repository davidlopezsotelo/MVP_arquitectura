package com.davidlopez.mvp.mainModule.presenter

import com.davidlopez.mvp.common.EventBus
import com.davidlopez.mvp.common.SportEvent
import com.davidlopez.mvp.mainModule.model.MainRepository
import com.davidlopez.mvp.mainModule.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainPresenter(private val view:MainActivity) {
    private val repository=MainRepository()
    private lateinit var viewScope: CoroutineScope

    fun onCreate(){
        viewScope = CoroutineScope(Dispatchers.IO+ Job())
        onEvent()
    }

    fun onDestroy(){
        viewScope.cancel()
    }

    suspend fun refresh(){
        view.clearAdapter()
        view.showAdUI(true)
        getEvents()

    }

     suspend fun getEvents() {
        view.showProgress(true)
        repository.getEvent()
    }

    suspend fun registerAd(){
        repository.registerAd()
    }
     suspend fun closeAd(){
            repository.closeAd()
        }

    suspend fun saveResult(result: SportEvent.ResultSuccess){
        view.showProgress(true)
        repository.saveResult(result)
    }


    private fun onEvent() {

        viewScope.launch {

            // metemos el when dentro de un bloque this.launch (Lanza esta(corrutina), para mantener dentro del mismo contexto)
            EventBus.instance().susbcribe<SportEvent> { event ->
                this.launch {
                    when (event) {
                    is SportEvent.ResultSuccess -> {
                        view.add(event)
                        view.showProgress(false)
                    }
                    is SportEvent.ResultError ->{
                        view.showSnackBar("Code: ${event.code} Message: ${event.msg}")
                        view.showProgress(false)
                    }
                    is SportEvent.SaveEvent ->{
                        view.showToast("Guardado.")
                        view.showProgress(false)
                    }

                    is SportEvent.AdEvent ->
                        view.showToast("Ad click. Send data to server...")
                    is SportEvent.ClosedAdEvent ->
                        view.showAdUI(false)
                    else ->{}
                }  }

            }
        }
    }
}