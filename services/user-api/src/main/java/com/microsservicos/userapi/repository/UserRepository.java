package com.microsservicos.userapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microsservicos.userapi.model.User;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
  User findByCpf(String cpf);
  List<User> queryByNameLike(String name);
}
