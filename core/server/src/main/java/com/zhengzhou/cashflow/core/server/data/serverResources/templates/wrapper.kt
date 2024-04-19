package com.zhengzhou.cashflow.core.server.data.serverResources.templates

import kotlinx.html.DIV
import kotlinx.html.HtmlBlockTag
import kotlinx.html.div

fun HtmlBlockTag.wrapper(block: DIV.() -> Unit) {
    div(classes = "wrapper") {
        block()
    }
}