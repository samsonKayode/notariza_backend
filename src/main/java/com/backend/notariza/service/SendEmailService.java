package com.backend.notariza.service;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.backend.notariza.dao.UserDao;

@Service
public class SendEmailService {
	
	@Value("${base_url}")
	private String base_url;

	@Autowired
	UserDao userDao;

	//Logger log = LoggerFactory.getLogger(this.getClass());
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JavaMailSender javaMailSender;

	@Async
	public String sendMail(String service, String fullname, String userEmail) throws MessagingException, Exception {

		String subject = "Your document is ready!";
		String response = "";

		log.info("Sending Email");

		try {

			//UserEntity userEntity = userDao.getOne(userID);

			//String userEmail = userEntity.getUsername();
			//String fullname = userEntity.getFirstname() + " " + userEntity.getLastname();

			javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			String message = "Hello <b>" + fullname + ",</b> <p> Your " + service
					+ " document is available to view and download.</p><br/><p> Notariza Team</p><hr/>";

			helper.setSubject(subject);
			helper.setText(message, true);
			helper.setTo(userEmail);
			helper.setFrom("notariza@theblpracticehub.com", "The Notariza Team");

			log.info("Sending email...");

			javaMailSender.send(mimeMessage);

			log.info("Email Sent");
			response = "email sent";

		} catch (MessagingException nn) {

			log.error("error sending email " + nn.getMessage());
			response = "email not sent " + nn.getMessage();
		}

		return response;
	}
	
	//Send verification needed emails..
	
	@Async
	public String sendVerificationMail(String service, String reference, String fullname, String userEmail) throws Exception {

		String subject = "Your request has been received!";
		String response = "";
		
		String message="";

		log.info("Sending Email");

		try {

			//UserEntity userEntity = userDao.getOne(currentUser.getUserId());

			//String userEmail = userEntity.getUsername();
			//String fullname = userEntity.getFirstname() + " " + userEntity.getLastname();

			javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			
			if(service.equalsIgnoreCase("Financial Gift Deed")) {
				
				message = "Hello <b>" + fullname + ",</b> <p> Your request for a notarized <b>Financial Gift Deed</b> has been received. " 
						+ " Our notary public will be in touch with you within an hour for video verification.</p>"
						+" <p> Please ensure you have your valid means of identification and you are in a quiet and well lit environment.</p>"+"<p>Reference No: <b>"+reference+"</b></p>"
						+"<br/><p> --The Notariza Team</p><hr/>"
						+"<p> <font color='gray'>* Requests made after 5pm(WAT) or during weekends will be responded to within the next business day</font></p>";
			}
			

			message = "Hello <b>" + fullname + ",</b> <p> Your request to notarize <b>"+service+"</b> has been received. " 
					+ " Our notary public will be in touch with you within an hour for video verification.</p>"
					+" <p> Please ensure you have your valid means of identification and you are in a quiet and well lit environment.</p>"+"<p>Reference No: <b>"+reference+"</b></p>"
					+"<br/><p> --The Notariza Team</p><hr/>"
					+"<p> <font color='gray'>* Requests made after 5pm(WAT) or during weekends will be responded to within the next business day</font></p>";

			helper.setSubject(subject);
			helper.setText(message, true);
			helper.setTo(userEmail);
			helper.setBcc("verification@theblpracticehub.com");
			helper.setFrom("notariza@theblpracticehub.com", "The Notariza Team");

			log.info("Sending verification email...");

			javaMailSender.send(mimeMessage);

			log.info("Verification email Sent");
			response = "email sent";

		} catch (MessagingException nn) {

			log.error("error sending email " + nn.getMessage());
			response = "email not sent " + nn.getMessage();
		}
		
		catch (Exception nn) {

			log.error("error sending email " + nn.getMessage());
			response = "email not sent " + nn.getMessage();
		}

		return response;
	}
	
	//verification done email..
	
	@Async
	public String verificationDone(String to, String status, String fullname) throws Exception {
		
		String response="";
		
		String subject="";
		String message="";
		
		if(status.equalsIgnoreCase("yes")) {
			
			subject="Your document has been notarized!";
			message = "Hello <b>" + fullname + ",</b> <p> Your request to notarize a document has been approved. " 
					+ " Your document is now available for download on your dashboard.</p>"
					+" <p> Please visit <a href='https://www.notariza.africa'> your dashboard</a> to view and download your document.</p>"
					+"<br/><p> --The Notariza Team</p><hr/>"
					+"<p> <font color='gray'>* Contact the <a href='mailto:notariza@theblpracticehub.com'>Notariza Team</a> if you have any questions or queries </font></p>";
			
		}else {
			
			subject = "Your document has not been notarized";
			message = "Hello <b>" + fullname + ",</b> <p> Your request to notarize a document has been denied. " 
					+ "The notary public was not duly satisfied with your responses during the video verification.</p>"
					+" <p> Please note that your refund will be scheduled and you should receive it within three (3) business days.</p>"
					+"<br/><p> --The Notariza Team</p><hr/>"
					+"<p> <font color='gray'>* Contact the <a href='mailto:notariza@theblpracticehub.com'>Notariza Team</a> if you have any questions or queries </font></p>";
		}
		
		javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			
			helper.setSubject(subject);
			helper.setText(message, true);
			helper.setTo(to);
			helper.setBcc("notariza_approve@theblpracticehub.com");
			helper.setFrom("notariza@theblpracticehub.com", "The Notariza Team");

			log.info("Sending approval email...");

			javaMailSender.send(mimeMessage);

			log.info("approval email Sent");
			response = "email sent";

		} catch (MessagingException e) {
			log.error("error sending verification done email "+e.getLocalizedMessage());
			response="error sending email";
		}

		return response;
	}
	
	//send login account verification email...
	
	@Async
	public String accountVerificationEmail(String to, String fullname, String verificationCode) throws Exception {
		
		String  response="";
		

		
		String subject = "Verify your account";
		String message = "Dear [[name]],<br>"
	            + "Please click the link below to verify your registration:<br>"
	            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY YOUR ACCOUNT</a></h3>"
	            + "Thank you,<br>"
	            + "The Notariza Team.";
		
		message = message.replace("[[name]]", fullname);
		String verifyURL = base_url + "/v1/user/verify?code=" + verificationCode;
	     
		message = message.replace("[[URL]]", verifyURL);
		
		javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		
		helper.setSubject(subject);
		helper.setText(message, true);
		helper.setTo(to);
		helper.setFrom("notariza@theblpracticehub.com", "The Notariza Team");

		log.info("Sending approval email...");

		javaMailSender.send(mimeMessage);

		log.info("account verification email sent");
		response = "email sent";

		return response;
		
		
	}
	

}
