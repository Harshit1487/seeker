package com.codeathon.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.cabin4j.suite.entity.platform.Item;

@Entity
@Table(name = "CATEGORY")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Category extends Item {
	
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, unique = true)
	private String code;	

	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}