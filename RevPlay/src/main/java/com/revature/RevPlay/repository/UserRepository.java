package com.revature.RevPlay.repository;

import com.revature.RevPlay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String admin);

    @Query("""
                SELECT COUNT(s)
                FROM User u
                JOIN u.favoriteSongs s
                WHERE u.id = :userId
            """)
    long countFavoriteSongs(@Param("userId") Long userId);
}
