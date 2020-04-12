package com.dailystudio.devbricksx.sample.db

import androidx.room.Query
import com.dailystudio.devbricksx.annotations.DaoExtension

@DaoExtension(entity = User::class)
abstract class UserDaoExtension {

    @Query("SELECT * FROM _User WHERE id IN (:ids)")
    abstract fun loadAllByIds(ids: IntArray): List<User>

    @Query("SELECT * FROM _User WHERE id == :id")
    abstract fun findById(id: String): User

}