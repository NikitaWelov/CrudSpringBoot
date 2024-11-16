package org.example.crudspringboot.service;

import jakarta.transaction.Transactional;
import org.example.crudspringboot.dao.RoleDao;
import org.example.crudspringboot.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Transactional
    public Role getRoleById(Long id) {
        return roleDao.findById(id);
    }

    @Transactional
    public Role getDefaultRole() {
        return roleDao.findByName(Role.RoleType.USER.name());
    }
}
