package com.thecatapi.cats.repository

import android.content.Context
import java.util.UUID

class UserStorage(context: Context) {

    companion object {
        private const val USER_PREFERENCES = "user_preferences"
        private const val KEY_SUB_ID = "sub_id"
    }

    private val preferences = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)

    fun subId(): String {
        val subId = preferences.getString(KEY_SUB_ID, null)

        if (subId == null) {
            val newSubId = UUID.randomUUID().toString()
            preferences.edit().putString(KEY_SUB_ID, newSubId).apply()
            return newSubId
        }
        return subId
    }

    fun clear() = preferences.edit().clear().apply()
}