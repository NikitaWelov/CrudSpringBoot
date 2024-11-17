package org.example.crudspringboot.dao;

import org.example.crudspringboot.model.Role;
import org.example.crudspringboot.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer> {
    Role findById(Long id);

    Role findByName(RoleType name);
}
