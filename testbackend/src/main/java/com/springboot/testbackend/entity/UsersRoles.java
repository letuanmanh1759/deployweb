package com.springboot.testbackend.entity;



import javax.persistence.*;

@Entity
@Table(name = "users_roles")
public class UsersRoles {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "user")
	private String user;
	
	@Column(name = "role")
	private Integer role;

	public UsersRoles() {
	}

	public UsersRoles(Integer id, String user, Integer role) {
		this.id = id;
		this.user = user;
		this.role = role;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

}
