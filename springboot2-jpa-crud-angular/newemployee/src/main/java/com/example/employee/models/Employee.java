package com.example.employee.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "This model is used to create a user")
@Entity
@Table(name="employees")
public class Employee {
	
	@ApiModelProperty(notes = "Auto generated unique id", required = true, position = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ApiModelProperty(notes = "Employees First name as appears on their identification cards", example = "George", required = true, position = 2)
	@Column
	@NotEmpty(message = "Firstname is required")
	@Size(min = 2, max = 50, message = "Firstname should have atleast 2 chartacters")
	private String firstname;
	
	@ApiModelProperty(notes = "Employees Surname as appears on their identification cards", example ="Whyte", required = false, position = 3)
	@Column
	@Size(min = 2, max = 50, message = "Lastname should have atleast 2 chartacters")
	private String lastname;
	
	@ApiModelProperty(notes = "Email", example="example@exaample.com", required = true, position = 4)
	@Column
	@NotEmpty(message = "Email is required")
	private String email;
	
//	@Column(name="picByte", length = 3000)
//	private byte[] picByte;
//	
//	public byte[] getPicByte() {
//		return picByte;
//	}
//
//	public void setPicByte(byte[] picByte) {
//		this.picByte = picByte;
//	}
	@ApiModelProperty(notes = "Photo uploaded in the form of png,jpeg or jfif ", required = false, position = 5)
	@Column(name="photo_url")
	private String photo_url;

	public String getPhoto_url() {
		return photo_url;
	}

	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Employee(int id, String firstname, String lastname, String email, String photo_url) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.photo_url = photo_url;
	}
	

	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email
				+ ", photo_url=" + photo_url + "]";
	}
	
	
	
}
