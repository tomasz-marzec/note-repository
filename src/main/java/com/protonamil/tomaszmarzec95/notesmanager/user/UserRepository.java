package com.protonamil.tomaszmarzec95.notesmanager.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);
    User findByUsername(String username);

}
