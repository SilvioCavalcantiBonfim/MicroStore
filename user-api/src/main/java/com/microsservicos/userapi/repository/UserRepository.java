package com.microsservicos.userapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microsservicos.userapi.model.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
  Optional<User> findByCpfAndKey(String cpf, String key);
  List<User> queryByNameLike(String name);
  boolean existsByCpf(String cpf);
}
