package com.zhengzhou.cashflow.transaction_edit.view_model.event

/**
 * Possible actions to perform on a tag in TransactionEditScreen
 */
internal sealed class TagEvent {
    /**
     * Add a new tag to the transaction
     * @param tagName: text of the tag
     */
    data class Add(val tagName: String): TagEvent()
    /**
     * @param tagIndex: index of the tag in the processed transaction
     */
    data class Disable(val tagIndex: Int?): TagEvent()
    /**
     * @param tagIndex: index of the tag in the processed transaction
     */
    data class Enable(val tagIndex: Int?): TagEvent()
}