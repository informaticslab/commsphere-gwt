/**
 * 
 */
package org.phiresearchlab.commsphere.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Joel M. Rives
 * Aug 19, 2011
 */

@Entity
public class Summary extends DomainObject {
	
	private static final long serialVersionUID = 1792656847483150585L;

	@Column(length = 64, nullable = false, unique = true)
	private String title;
	
	@ManyToOne
	private Summary parent;
	
	@OneToMany
	private List<Summary> children;
	
	@OneToMany
	private List<SummaryItem> items;
	
	public Summary() { }
	
	public Summary(String title) {
	    this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Summary getParent() {
		return parent;
	}

	public void setParent(Summary parent) {
		this.parent = parent;
	}

	public List<Summary> getChildren() {
		return children;
	}

	public void setChildren(List<Summary> children) {
		this.children = children;
	}

	public List<SummaryItem> getItems() {
		return items;
	}

	public void setItems(List<SummaryItem> items) {
		this.items = items;
	}
	
}
