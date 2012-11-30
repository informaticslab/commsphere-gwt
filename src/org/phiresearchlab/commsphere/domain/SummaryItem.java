/**
 * 
 */
package org.phiresearchlab.commsphere.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Joel M. Rives
 * Aug 19, 2011
 */

@Entity
public class SummaryItem extends DomainObject {
	
	private static final long serialVersionUID = 1792656847483150585L;

	@ManyToOne 
	private SummaryItem parent;
	
	@OneToMany
	private List<SummaryItem> children;
	
	@ManyToOne
	private Summary summary;
	private String text;
	private Boolean enabled;
	
	public SummaryItem getParent() {
		return parent;
	}
	
	public void setParent(SummaryItem parent) {
		this.parent = parent;
	}
	
	public List<SummaryItem> getChildren() {
		return children;
	}
	
	public void setChildren(List<SummaryItem> children) {
		this.children = children;
	}
	
	public Summary getSummary() {
		return summary;
	}
	
	public void setSummary(Summary summary) {
		this.summary = summary;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
}
