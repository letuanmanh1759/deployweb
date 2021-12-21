package com.springboot.testbackend.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.springboot.testbackend.entity.UsersRoles;

@Repository
public interface UsersRolesRepository extends JpaRepository<UsersRoles, Integer> {
	}