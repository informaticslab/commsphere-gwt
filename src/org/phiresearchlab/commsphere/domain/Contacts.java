/**
 * 
 */
package org.phiresearchlab.commsphere.domain;

import javax.persistence.Entity;

/**
 *
 * @author Joel M. Rives
 * Aug 19, 2011
 */

@Entity
public class Contacts extends DomainObject {
	
	private static final long serialVersionUID = 1792656847483150585L;

	private String issue;
	
	private Double percentQuestions;
	
	private String representativeQuestion;

    public String getIssue()
    {
        return this.issue;
    }

    public Double getPercentQuestions()
    {
        return this.percentQuestions;
    }

    public String getRepresentativeQuestion()
    {
        return this.representativeQuestion;
    }

    public void setIssue(String issue)
    {
        this.issue = issue;
    }

    public void setPercentQuestions(Double percentQuestions)
    {
        this.percentQuestions = percentQuestions;
    }

    public void setRepresentativeQuestion(String representativeQuestion)
    {
        this.representativeQuestion = representativeQuestion;
    }
	
}
