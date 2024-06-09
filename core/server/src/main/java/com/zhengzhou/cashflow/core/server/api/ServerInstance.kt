package com.zhengzhou.cashflow.core.server.api

import com.zhengzhou.cashflow.core.server.data.ServerEngineImplementation

class ServerInstance {

    companion object {

        private var INSTANCE: ServerEngineInterface? = null

        fun initialize() {
            if (INSTANCE == null) {
                INSTANCE = ServerEngineImplementation()
            }
        }

        /**
         * @return null if the server has not been initialized
         */
        fun getServer(): ServerEngineInterface? {
            return INSTANCE
        }
    }
}