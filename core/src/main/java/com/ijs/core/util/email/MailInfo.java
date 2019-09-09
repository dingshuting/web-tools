package com.ijs.core.util.email;

import java.io.File;
import java.util.Date;
import java.util.Map;

/**
 * 
 * @author Tairong
 *
 */
public class MailInfo {    
    
    public static final int HTMLMAIL = 1;
    public static final int TEXTMAIL = 2;
    public static final String PRIORITYHIGH = "1";
    public static final String PRIORITYLOW = "-1";		
    public static final String PRIORITYDEFAULT = "3";
    
	
    private String mailTo = null;
    private String mailCC = null;
    private String mailBCC = null;
    
    private String mailPriority = null;
    private String mailSubject = null;
    private String mailText = null;
    private Date mailSendDate = null;
    
    private Map<String,File> files;
    
    private int mailType;
    public MailInfo(){
        this.mailType = HTMLMAIL; 		
        this.mailPriority = PRIORITYDEFAULT;
        this.mailSendDate = new Date();		
    }
    
    /**
     * @return ���� mailCC��
     */
    public String getMailCC() {
        return mailCC;
    }
    /**
     * @param mailCC Ҫ���õ� mailCC��
     */
    public void setMailCC(String mailCC) {
        this.mailCC = mailCC;
    }
    /**
     * @param mailSendDate Ҫ���õ� mailSendDate��
     */
    public void setMailSendDate(Date mailSendDate) {
        this.mailSendDate = mailSendDate;
    }

    /**
     * @return ���� mailSendDate��
     */
    public Date getMailSendDate() {
        return mailSendDate;
    }

    /**
     * @return ���� mailSubject��
     */
    public String getMailSubject() {
        return mailSubject;
    }
    /**
     * @param mailSubject Ҫ���õ� mailSubject��
     */
    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }
    /**
     * @return ���� mailText��
     */
    public String getMailText() {
        return mailText;
    }
    /**
     * @param mailText Ҫ���õ� mailText��
     */
    public void setMailText(String mailText) {
        this.mailText = mailText;
    }
    /**
     * @return ���� mailTo��
     */
    public String getMailTo() {
        return mailTo;
    }
    /**
     * @param mailTo Ҫ���õ� mailTo��
     */
    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }
    /**
     * @return ���� mailType��
     */
    public int getMailType() {
        return mailType;
    }
    /**
     * @param mailType Ҫ���õ� mailType��
     */
    public void setMailType(int mailType) {
        this.mailType = mailType;
    }
    /**
     * @return ���� mailBCC��
     */
    public String getMailBCC() {
        return mailBCC;
    }
    /**
     * @param mailBCC Ҫ���õ� mailBCC��
     */
    public void setMailBCC(String mailBCC) {
        this.mailBCC = mailBCC;
    }

	public String getMailPriority() {
		return mailPriority;
	}

	public void setMailPriority(String mailPriority) {
		this.mailPriority = mailPriority;
	}

	/**
	 * @return Returns the files.
	 */
	public Map<String,File> getFiles() {
		return files;
	}

	/**
	 * @param files The files to set.
	 */
	public void setFiles(Map<String,File> files) {
		this.files = files;
	}
}
