package com.zhengzhou.cashflow.transaction_edit.view_model.event

internal sealed class TransactionEditEvent {
    /**
     * Choose which tab should be composed on the screen
     * @param tagEvent selects the kind of operation performed on the tag.
     */
    data class TagAction(val tagEvent: TagEvent): TransactionEditEvent()
}