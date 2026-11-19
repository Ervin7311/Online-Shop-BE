package com.example.onlineshop.repositories;

import com.example.onlineshop.entitetes.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("""
       SELECT u FROM User u
       WHERE (:name IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%')))
         AND (:role IS NULL OR u.role = :role)
         AND (:status IS NULL OR u.status = :status)
       """)
    List<User> searchUsers(
            @Param("name") String name,
            @Param("role") User.Role role,
            @Param("status") User.Status status
    );

}





