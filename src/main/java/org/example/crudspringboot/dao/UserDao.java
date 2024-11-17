package org.example.crudspringboot.dao;

import org.example.crudspringboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    User findById(Long id);

    void deleteById(Long id);

    User findByUsername(String username);

    User findByLastname(String lastname);

    User findByEmail(String email);

    List<User> findByUsernameContainingIgnoreCase(String username);

}
