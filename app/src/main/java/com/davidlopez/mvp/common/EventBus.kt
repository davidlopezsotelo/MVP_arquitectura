package com.davidlopez.mvp.common

import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlin.coroutines.coroutineContext

class EventBus {
    //#1
    private val _events = MutableSharedFlow<Any>()
    val events: SharedFlow<Any> = _events

    //#2
    suspend fun publish(event: Any){
        _events.emit(event)
    }

    //#3
    suspend inline fun <reified T> susbcribe(crossinline onEvent: (T) -> Unit){
        events.filterIsInstance<T>()
            .collectLatest { event->
                coroutineContext.ensureActive()
                onEvent(event)
            }
    }

    //Singleton
    companion object{
        private val _eventBusInstance: EventBus by lazy { EventBus() }

        fun instance()= _eventBusInstance
    }
}


