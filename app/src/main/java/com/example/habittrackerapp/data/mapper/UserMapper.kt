package com.example.habittrackerapp.data.mapper

import com.example.habittrackerapp.data.local.entity.UserEntity
import com.example.habittrackerapp.domain.model.User

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email,
        profilePictureUrl = profilePictureUrl,
        streak = streak,
        totalCompletions = totalCompletions,
        memberSinceYear = memberSinceYear,
        isAnonymous = isAnonymous
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        email = email,
        profilePictureUrl = profilePictureUrl,
        streak = streak,
        totalCompletions = totalCompletions,
        memberSinceYear = memberSinceYear,
        isAnonymous = isAnonymous
    )
}
