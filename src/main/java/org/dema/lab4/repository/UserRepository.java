package org.dema.lab4.repository;

import org.dema.lab4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User getByUsername(String username);

    boolean existsByUsername(String username);
}
