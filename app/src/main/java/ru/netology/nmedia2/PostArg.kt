package ru.netology.nmedia2

import android.os.Bundle
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object PostArg: ReadWriteProperty<Bundle, Post?> {

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Post?) {
        thisRef.putSerializable(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Post? =
        thisRef.getSerializable(property.name) as Post
}

