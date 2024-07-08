package com.davidlopez.mvp.mainModule.view

import com.davidlopez.mvp.common.SportEvent

interface OnClickListener {
    fun onClick(result: SportEvent.ResultSuccess)
}