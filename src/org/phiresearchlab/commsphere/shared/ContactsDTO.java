package org.phiresearchlab.commsphere.shared;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Nov 17, 2011
 *
 */
public class ContactsDTO extends BaseDTO
{
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
