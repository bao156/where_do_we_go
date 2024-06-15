package com.main.where_do_we_go_back_end.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.main.where_do_we_go_back_end.entity.Role;
import com.main.where_do_we_go_back_end.entity.RoleName;

public interface RoleRepository extends JpaRepository<Role, String> {

  Optional<Role> findByName(RoleName roleName);

}
