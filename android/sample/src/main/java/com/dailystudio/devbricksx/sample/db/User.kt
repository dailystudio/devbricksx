package com.dailystudio.devbricksx.sample.db

import com.dailystudio.devbricksx.annotations.RoomCompanion
import java.util.*

@RoomCompanion(primaryKey = "id", database = "user",
        extension = UserDaoExtension::class,
        converters = [UUIDConverter::class, DateConverter::class])
class User (
        @JvmField val id: UUID,
        @JvmField val name: String) {
    @JvmField var firstName: String? = null
    @JvmField var lastName: String? = null
    @JvmField var age: Int = 0
    @JvmField var phoneNumber: String? = null
    @JvmField var paid: Boolean = false

    override fun toString(): String {
        return "id = $id, name = $name"
    }
}
