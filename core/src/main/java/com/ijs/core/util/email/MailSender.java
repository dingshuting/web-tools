package com.ijs.core.util.email;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ijs.core.base.Config;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * 
 * @author Tairong
 *
 */
public class MailSender {
	protected static final transient Log log = LogFactory.getLog(MailSender.class);
    private MailInfo mailInfo = null;

    /**
     * 
     */
    public MailSender() {
        mailInfo = new MailInfo();
    }
    
    /**
     */
    public MailSender(MailInfo mailInfo) {
        this.mailInfo = mailInfo;
    }

    /**
     * 发送email
     * @throws NotOrErrorMailInfoException  
     * @throws MessagingException 
     * @throws AddressException 
     * @throws MessagingException
     * @throws ErrorMailTypeException 
     * @throws UnsupportedEncodingException 
     */
    public void send() throws Exception{

        if (Config.SYS_PARAMETER_MAP.get("mail_smtp_server")==null || !checkMailInfo()) {
            new Exception("mail_smtp_server is null or its format is wrong");
        }

        Properties props = System.getProperties();
        
        /*���÷����ʼ�������*/
        props.put("mail.smtp.host", Config.SYS_PARAMETER_MAP.get("mail_smtp_server"));
        props.put("mail.smtp.auth", "true"); 
      //开启SSL加密
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());  
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";  
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        //new Auth(mailInfo.getUserName(), mailInfo.getPassword())
        Session session = Session.getDefaultInstance(props, null);
        
        Transport transport = session.getTransport("smtp");                 
        transport.connect(Config.SYS_PARAMETER_MAP.get("mail_smtp_server"), Config.SYS_PARAMETER_MAP.get("mail_user"), Config.SYS_PARAMETER_MAP.get("mail_pwd")); 
        
        session.setDebug(false);
        
        /*���÷����ʼ���Ϣ
         **/
        Message msg = new MimeMessage(session);  
        
        /*���÷����ʼ���ַ*/
        msg.setFrom(this.getAddress(Config.SYS_PARAMETER_MAP.get("mail_sender")));
        
        //msg.
        /*���ý����ʼ���ַ*/
        msg.setRecipients(Message.RecipientType.TO, 
                	this.getAddresses(mailInfo.getMailTo()));		
        
        /*���ó����ʼ���ַ*/
        if(mailInfo.getMailCC() != null){
            msg.setRecipients(Message.RecipientType.CC, 
                    this.getAddresses(mailInfo.getMailCC()));	
        }
        
        /*������ʽ�����ʼ���ַ*/
        if(mailInfo.getMailBCC() != null){
            msg.setRecipients(Message.RecipientType.BCC, 
                    this.getAddresses(mailInfo.getMailBCC()));	
        }           
        
        /*�����ʼ����ȼ�*/
        msg.addHeader("X-Priority",mailInfo.getMailPriority());
        msg.addHeader("Content-Transfer-Encoding", "Base64");  
        msg.addHeader("Content-Type", "text/plain;charset=gbk");  
        
        /*���÷�������*/
        msg.setSentDate(mailInfo.getMailSendDate());
           
        /*�����ʼ�����*/
        String subject = Base64.encode(mailInfo.getMailSubject().getBytes("gbk")); 
        msg.setSubject("=?gbk?B?" + subject + "?=");	        
        
        
    	Multipart mp = new MimeMultipart();
    	
    	/* ���� */
    	MimeBodyPart mbp = new MimeBodyPart();  
    	
    	/*�ж��ʼ�����text/html��Ȼ�����ʼ����������ʼ�����*/     
    	if(mailInfo.getMailType() == MailInfo.HTMLMAIL){  
    		mbp.setContent(mailInfo.getMailText(),"text/html;charset=gbk");
    	}else{
    		mbp.setContent(mailInfo.getMailText(),"text/plain;charset=gbk");
    	}
    	
    	mp.addBodyPart(mbp); 
    	
    	/* ���� */
    	if(mailInfo.getFiles() != null){
    		Iterator<String> keys = mailInfo.getFiles().keySet().iterator();
    		
    		while(keys.hasNext()){
    			String fileName = (String) keys.next();        			
    			File file = (File) mailInfo.getFiles().get(fileName);
    			
    			MimeBodyPart filembp = new MimeBodyPart();
    			filembp.setDataHandler(new DataHandler(new FileDataSource(file)));
    			filembp.setFileName(MimeUtility.encodeWord(fileName));
    			
    			mp.addBodyPart(filembp);
    		}
    	}
    	        	
    	msg.setContent(mp);
    
        
        /*�����ʼ�*/
        transport.sendMessage(msg, msg.getAllRecipients());   
        transport.close();
        log.info("the mail send to "+mailInfo.getMailTo()+" were sended");
        //Transport.send(msg);
    }

    /**
     * �����ʼ�
     * @param info �ʼ���Ϣ����
     * @throws AddressException
     * @throws NotOrErrorMailInfoException
     * @throws MessagingException
     * @throws ErrorMailTypeException
     * @throws UnsupportedEncodingException 
     */
    public void send(MailInfo info) throws Exception{
        this.mailInfo = info;        
        this.send();
    }
    
    /**
     * ����ʼ���Ϣ
     * @return boolean
     * @throws NotOrErrorMailInfoException
     */
    private boolean checkMailInfo() throws NotOrErrorMailInfoException {
        boolean flag = false;

        /*
         * ����ʼ���Ϣ�����Ƿ�Ϊ��, ���Ϊ�մ���NotOrErrorMailInfoException�쳣
         */
        if (mailInfo == null) {
            flag = false;
            throw new NotOrErrorMailInfoException("mail content can not be null");
        }

        /*
         * ����ʼ���Ϣ�Ƿ�Ϊ�յ���, ����д���NotOrErrorMailInfoException�쳣
         */
        if (Config.SYS_PARAMETER_MAP.get("mail_sender")== null || mailInfo.getMailTo() == null
                || mailInfo.getMailSubject() == null
                || mailInfo.getMailText() == null) {
            flag = false;
            throw new NotOrErrorMailInfoException("please check the sender,mailTo,subject and mail text, all of them cannot be null");
        }

        flag = true;
        return flag;
    }

    /**
     * ����ʼ����յ�ַ�б�
     * 
     * @return �����ʼ���ַ�б�
     * @throws AddressException
     * @throws NotOrErrorMailInfoException 
     */
    private InternetAddress[] getAddresses(String mails) throws Exception {

        //��õ�ַ�б� 
    	if(mails==null){
    		throw new NotOrErrorMailInfoException("email address is null");
    	}
    	
        String[] emails = mails.split(";");
        InternetAddress[] address = new InternetAddress[emails.length];
        
        //���InternetAddress�б�
        for (int i = 0; i < emails.length; i++) {
            //System.out.println(mailTos[i]);
        	if(emails[i]==null || "".equals(emails[i].trim())){
        		throw new NotOrErrorMailInfoException("email address is null");
        	}
        	
        	String[] mailaddress = emails[i].split(",");
        	
        	if(mailaddress.length == 1){
        		address[i] = new InternetAddress(mailaddress[0].trim());
        	}else if(mailaddress.length == 2){
        		address[i] = new InternetAddress(mailaddress[0].trim(),mailaddress[1].trim(), "gbk");
        	}else{
        		throw new NotOrErrorMailInfoException("email address is illegal,for example: example@ex.com,IT;example1@ex.com");
        	}
        }               

        return address;
    }
    
    private InternetAddress getAddress(String mails) throws Exception {

        //��õ�ַ�б� 
    	if(mails==null){
    		throw new NotOrErrorMailInfoException("The sender is null");
    	}  
    	InternetAddress address = null;	
    	String[] mailaddress = mails.split(",");
    	
    	if(mailaddress.length == 1){
    		address = new InternetAddress(mailaddress[0].trim());
    	}else if(mailaddress.length == 2){
    		address = new InternetAddress(mailaddress[0].trim(),mailaddress[1].trim(), "gbk");
    	}else{
    		throw new NotOrErrorMailInfoException("mail address is invaluable");
    	}          

        return address;
    }
       
    
	/**
	 * @return the mailInfo
	 */
	public MailInfo getMailInfo() {
		return mailInfo;
	}

	/**
	 * @param mailInfo the mailInfo to set
	 */
	public void setMailInfo(MailInfo mailInfo) {
		this.mailInfo = mailInfo;
	}
}