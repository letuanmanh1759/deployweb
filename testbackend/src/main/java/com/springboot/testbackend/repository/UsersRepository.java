package com.springboot.testbackend.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.springboot.testbackend.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

	
}