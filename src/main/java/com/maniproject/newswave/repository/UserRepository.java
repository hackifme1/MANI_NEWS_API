package com.maniproject.newswave.repository;
import com.maniproject.newswave.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    List<User> findByIsSubscribedTrue();

    List<User> findByIsSubscribedTrueAndIsVerifiedTrue();

    @Query(value = "SELECT selected_country, COUNT(*) AS num_users\n" +
            "FROM users\n" +
            "WHERE selected_country IS NOT NULL\n" +
            "GROUP BY selected_country\n" +
            "ORDER BY num_users DESC\n" +
            "LIMIT 3\n", nativeQuery = true)
    List<Object[]> getTopThreeCountries();

    @Query(value = "SELECT selected_category, COUNT(*) AS num_users\n" +
            "FROM users\n" +
            "WHERE selected_category IS NOT NULL\n" +
            "GROUP BY selected_category\n" +
            "ORDER BY num_users DESC\n" +
            "LIMIT 3\n", nativeQuery = true)
    List<Object[]> getTopThreeCategories();
}
