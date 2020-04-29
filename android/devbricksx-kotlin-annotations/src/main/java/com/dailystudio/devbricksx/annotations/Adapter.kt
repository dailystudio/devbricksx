package com.dailystudio.devbricksx.annotations

import kotlin.reflect.KClass

enum class ViewType {
    SingleLine,
    Customized
}

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Adapter(
        val viewHolder: KClass<*>,
        val viewType: ViewType = ViewType.Customized,
        val layout: Int = -1,
        val paged: Boolean = false
)