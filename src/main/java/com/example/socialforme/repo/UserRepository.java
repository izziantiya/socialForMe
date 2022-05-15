package com.example.socialforme.repo;

import com.example.socialforme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   User findByUsername(String username);

   @Transactional
   Optional<User> findById(Long id);

}
