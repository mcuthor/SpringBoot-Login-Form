package com.example.demo.Model;

import java.util.Optional;

//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyAppUserRepository extends MongoRepository<MyAppUser, Long> {
    
    Optional<MyAppUser> findByUsername(String username);
    
    MyAppUser findByEmail(String email);
    
}
