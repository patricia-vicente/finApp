package com.example.finapp

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ModelsActivity {
    var id: String? = null
    var note: String? = null
    var amount: String? = null
    var type: String? = null
    var date: String? = null

    fun String.toDate(): Date? {
        return try {
            SimpleDateFormat("dd MM yyyy_HH:mm", Locale.getDefault()).parse(this)
        } catch (e: Exception) {
            null
        }
    }

    constructor()

    constructor(id: String, note: String, amount: String, type: String, date: String) {
        this.id = id
        this.note = note
        this.amount = amount
        this.type = type
        this.date = date
    }
}

