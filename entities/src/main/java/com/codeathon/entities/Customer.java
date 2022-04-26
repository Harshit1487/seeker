package com.codeathon.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cabin4j.suite.entity.platform.Image;
import com.cabin4j.suite.entity.platform.Principal;

@Entity
@Table(name = "CUSTOMER")
public class Customer extends Principal {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false, unique = true)
	private String email;
	
	private boolean receiveUpdates;
	
	private String cellphone;
	
	private Boolean loginDisabled;
	
	private Date lastLogin;

	private Date currentLogin;
	
	private Date dob;
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn
	private Image image;
	
	@ElementCollection
	private Set<String> categories;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isReceiveUpdates() {
		return receiveUpdates;
	}

	public void setReceiveUpdates(boolean receiveUpdates) {
		this.receiveUpdates = receiveUpdates;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public Boolean getLoginDisabled() {
		return loginDisabled;
	}

	public void setLoginDisabled(Boolean loginDisabled) {
		this.loginDisabled = loginDisabled;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getCurrentLogin() {
		return currentLogin;
	}

	public void setCurrentLogin(Date currentLogin) {
		this.currentLogin = currentLogin;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Set<String> getCategories() {
		return categories;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	
}