package itd.usm.backend.repository

import itd.usm.backend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : JpaRepository<UserEntity, Int> {
    fun findByUsername(username: String): UserEntity
}