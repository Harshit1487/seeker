package com.codeathon.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.cabin4j.suite.entity.platform.Item;

@Entity
@Table(name = "NUMBER_SERIES")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class NumberSeries extends Item{

	private static final long serialVersionUID = 1L;

	@Column(nullable = false, unique = true)
	private String code;
	
	private Long value;
	
	private String prefix;
	
	private Integer digits;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Integer getDigits() {
		return digits;
	}

	public void setDigits(Integer digits) {
		this.digits = digits;
	}
}