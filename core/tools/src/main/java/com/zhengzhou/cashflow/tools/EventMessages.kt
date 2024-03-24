package com.zhengzhou.cashflow.tools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class EventMessages {

    companion object {

        private val statusMessageId = MutableLiveData<Event<Int>>()
        val messageId : LiveData<Event<Int>>
            get() = statusMessageId

        private val statusMessage = MutableLiveData<Event<String>>()
        val message : LiveData<Event<String>>
            get() = statusMessage

        fun sendMessage(message: String) {
            statusMessage.postValue(Event(message))
        }

        fun sendMessageId(message: Int) {
            statusMessageId.postValue(Event(message))
        }
    }
}

open class Event<out T>(private val content: T) {
    private var hasBeenHandled = false
    //  Returns the content and prevents its use again.
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}