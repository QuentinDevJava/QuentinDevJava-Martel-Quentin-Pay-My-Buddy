package com.openclassrooms.payMyBuddy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.payMyBuddy.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

}
