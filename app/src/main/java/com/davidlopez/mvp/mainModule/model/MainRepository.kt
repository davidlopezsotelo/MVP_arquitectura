package com.davidlopez.mvp.mainModule.model

import com.davidlopez.mvp.common.EventBus
import com.davidlopez.mvp.common.SportEvent
import com.davidlopez.mvp.common.getAdEventsInRealtime
import com.davidlopez.mvp.common.getResultEventsInRealtime
import com.davidlopez.mvp.common.someTime
import kotlinx.coroutines.delay

class MainRepository {
    //VISTA
    suspend fun getEvent(){
        val events = getResultEventsInRealtime()
        events.forEach{ event ->
            delay(someTime())
            publishEvent(event)
        }
    }

    suspend fun saveResult(result: SportEvent.ResultSuccess){
        val response = if (result.isWarning)
            SportEvent.ResultError(30,"Error al guardar.")
        else SportEvent.SaveEvent
        publishEvent(response)
    }

    suspend fun registerAd(){
        val events = getAdEventsInRealtime()
        publishEvent(events.first())
    }

    suspend fun closeAd(){
        publishEvent(SportEvent.ClosedAdEvent)
    }

    private suspend fun publishEvent(event: SportEvent){
        EventBus.instance().publish(event)
    }

}