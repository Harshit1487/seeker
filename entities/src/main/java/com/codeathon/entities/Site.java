package com.codeathon.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cabin4j.suite.entity.platform.DRL;
import com.cabin4j.suite.entity.platform.Item;

@Entity
@Table(name = "SITE")
public class Site extends Item {
	
	private static final long serialVersionUID = 1L;
	
	String name;
	
	String url;
	
	@ManyToMany(cascade = CascadeType.REMOVE)
	List<Opportunity> opportunities;
	
	@OneToOne
	DRL drool;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Opportunity> getOpportunities() {
		return opportunities;
	}

	public void setOpportunities(List<Opportunity> opportunities) {
		this.opportunities = opportunities;
	}

	public DRL getDrool() {
		return drool;
	}

	public void setDrool(DRL drool) {
		this.drool = drool;
	}
	
	
}
