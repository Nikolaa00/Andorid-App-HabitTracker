package com.example.habittrackerapp.data.mapper

import com.example.habittrackerapp.data.local.entity.UserEntity
import com.example.habittrackerapp.domain.model.User

fun UserEntity.toDomain(): User {
    return User(
        uid = uid,
        displayName = displayName,
        email = email,
        photoUrl = photoUrl,
        bio = bio,
        totalPoints = totalPoints,
        joinedDate = joinedDate
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        uid = uid,
        displayName = displayName,
        email = email,
        photoUrl = photoUrl,
        bio = bio,
        totalPoints = totalPoints,
        joinedDate = joinedDate
    )
}
