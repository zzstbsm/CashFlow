package com.zhengzhou.cashflow.wallet_edit

import androidx.annotation.StringRes
import java.util.UUID

internal enum class WalletEditOption(
    @StringRes
    val title: Int,
){
    ADD(
        title = R.string.nav_name_wallet_add
    ),
    EDIT(
        title = R.string.nav_name_wallet_edit
    );

    companion object {
        fun chooseOption(id: UUID) : WalletEditOption {
            return if(id == UUID(0L,0L)) {
                ADD
            } else {
                EDIT
            }
        }
    }

}