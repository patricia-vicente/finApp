package com.example.finapp.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(): LocalDateTime {
    // Defina o formato da data aqui, se necessário
    // Exemplo: DateTimeFormatter.ISO_LOCAL_DATE_TIME para "2020-01-01T12:00:00"
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    return LocalDateTime.parse(this, formatter)
}
