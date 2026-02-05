package com.example.quiz_app.model.service

import androidx.room.TypeConverter


class Converters {
    // Wandelt eine Liste von Strings in einen einzelnen String um,
    // damit Room sie in der Datenbank speichern kann
    @TypeConverter
    fun fromList(value: List<String>): String{
        return value.joinToString("|")
    }

    // Wandelt den gespeicherten String wieder zurück in eine Liste von Strings,
    // wenn Room die Daten aus der Datenbank liest
    @TypeConverter
    fun toList (value: String):List<String>{
        return value.split("|")
    }
}