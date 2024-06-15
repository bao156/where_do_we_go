package com.main.where_do_we_go_back_end.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.main.where_do_we_go_back_end.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

}
