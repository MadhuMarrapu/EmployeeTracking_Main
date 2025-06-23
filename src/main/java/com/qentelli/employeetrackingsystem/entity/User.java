package com.qentelli.employeetrackingsystem.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "User_Data")
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String firstName;
	private String lastName;

	@Column(unique = true)
	private String employeeId;
	private String email;
	private String password;
	private String confirmPassword;
	@Enumerated(EnumType.STRING)
	private Roles roles;

	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Project> projects;
	private String techStack;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@CreatedBy
	@Column(updatable = false)
	private String createdBy;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@LastModifiedBy
	private String updatedBy;

	/*
	public User(int id, String firstName, String lastName, String employeeId, String email, String password,
			String confirmPassword,List<Project> projects,String techStack) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.employeeId = employeeId;
		this.email = email;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.projects = projects;
		this.techStack = techStack;
	}
*/
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roles.name()));
	}

	@Override
	public String getUsername() {
		return email; // use email as username
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
