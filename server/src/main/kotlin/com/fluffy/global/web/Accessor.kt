package com.fluffy.global.web

data class Accessor(val id: Long) {

    companion object {
        private const val GUEST_ID = -1L
        val GUEST: Accessor = Accessor(GUEST_ID)
    }

    fun isGuest(): Boolean {
        return id == GUEST_ID
    }
}
