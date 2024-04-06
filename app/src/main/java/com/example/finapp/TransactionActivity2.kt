package com.example.finapp

class TransactionActivity2 {
    var id: String? = null
        get() = field
        set(value) {
            field = value
        }
    var note: String? = null
        get() = field
        set(value) {
            field = value
        }
    var amount: String? = null
        get() = field
        set(value) {
            field = value
        }
    var type: String? = null
        get() = field
        set(value) {
            field = value
        }

    var date: String? = null
        get() = field
        set(value) {
            field = value
        }

    constructor() : super()
    constructor(id: String, note: String, amount: String, type: String) : this() {
        this.id = id
        this.note = note
        this.amount = amount
        this.type = type
        this.date = date
    }

    constructor(s: String, s1: String, s2: String, s3: String, s4: String)

}
