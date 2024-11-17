package org.example.crudspringboot.model

import jakarta.persistence.*

@Entity
@Table(name = "roles")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    val name: RoleType
) {
    constructor() : this(null, RoleType.USER)
}