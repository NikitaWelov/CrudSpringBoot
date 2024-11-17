package org.example.crudspringboot.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var username: String,

    var lastname: String,

    var email: String,

    @ManyToMany
    @JoinTable(
        name = "userroles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: Set<Role> = emptySet()
) {
    constructor() : this(null, "", "", "", emptySet())
}