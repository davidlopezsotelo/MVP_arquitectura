package com.davidlopez.mvp.mainModule.view

import com.davidlopez.mvp.SportEvent

interface OnClickListener {
    fun onClick(result: SportEvent.ResultSuccess)
}