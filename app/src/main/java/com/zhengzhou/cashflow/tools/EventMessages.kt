package com.zhengzhou.cashflow.tools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zhengzhou.cashflow.Event

class EventMessages {

    companion object {

        private val statusMessageId = MutableLiveData<Event<Int>>()
        val messageId : LiveData<Event<Int>>
            get() = statusMessageId

        private val statusMessage = MutableLiveData<Event<String>>()
        val message : LiveData<Event<String>>
            get() = statusMessage

        fun sendMessage(message: String) {
            statusMessage.value = Event(message)
        }

        fun sendMessageId(message: Int) {
            statusMessageId.value = Event(message)
        }
    }
}