package com.example.finapp

class TransactionActivity2 {
    var id: String? = null
    var note: String? = null
    var amount: String? = null
    var type: String? = null
    var date: String? = null

    constructor()

    constructor(id: String, note: String, amount: String, type: String, date: String) {
        this.id = id
        this.note = note
        this.amount = amount
        this.type = type
        this.date = date
    }
}

