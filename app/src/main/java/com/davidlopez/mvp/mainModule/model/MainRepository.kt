package com.davidlopez.mvp.mainModule.model

import com.davidlopez.mvp.EventBus
import com.davidlopez.mvp.SportEvent
import com.davidlopez.mvp.getAdEventsInRealtime
import com.davidlopez.mvp.getResultEventsInRealtime
import com.davidlopez.mvp.someTime
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
        publishEvent(SportEvent.SaveEvent)
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