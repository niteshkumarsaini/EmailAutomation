package com.mail.sender.Services;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mail.sender.dto.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendJobInquiryEmail(Email request) throws MessagingException {

		String receiverEmail = request.getReceiverEmail();
		String companyName = request.getCompanyName();
		boolean simpleEmail = request.isSimpleEmail();
		String resumeType = request.getResumeType();

		boolean hasCompanyName = companyName != null && !companyName.isBlank();

		String subject = "Java Backend Developer | Immediate Joiner";

		String emailBody;

		/*
		 * Select email format
		 *
		 * Simple Email = Plain Text Normal Email = HTML
		 */
		if (simpleEmail) {

			emailBody = buildSimpleEmail(companyName);

		} else {

			if (hasCompanyName) {
				emailBody = buildCompanySpecificEmail(companyName);
			} else {
				emailBody = buildGenericEmail();
			}
		}

		// Create MIME message
		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		// Receiver
		helper.setTo(receiverEmail);

		// Subject
		helper.setSubject(subject);

		/*
		 * Set email body
		 *
		 * false = Plain Text true = HTML
		 */
		helper.setText(emailBody, !simpleEmail);

		/*
		 * Select the resume according to the resume selected in the frontend.
		 */
		Resource resume = getResume(resumeType);

		if (!resume.exists()) {
			throw new RuntimeException("Selected resume was not found: " + resumeType);
		}

		/*
		 * Get the filename that will be visible to the recruiter.
		 */
		String attachmentName = getAttachmentName(resumeType);

		/*
		 * Attach selected resume.
		 */
		helper.addAttachment(attachmentName, resume);

		// Send email
		mailSender.send(message);
	}

	/**
	 * Selects the actual PDF from src/main/resources/ based on the resume type
	 * received from frontend.
	 */
	private Resource getResume(String resumeType) {

		if (resumeType == null || resumeType.isBlank()) {
			throw new IllegalArgumentException("Resume type is required.");
		}

		return switch (resumeType) {

		case "FULL_STACK" -> new ClassPathResource("Full_Stack_Developer.pdf");

		case "BACKEND" -> new ClassPathResource("Backend_Developer.pdf");

		case "BACKEND_LONG" -> new ClassPathResource("Backend_Developer_Long.pdf");

		default -> throw new IllegalArgumentException("Invalid resume type: " + resumeType);
		};
	}

	/**
	 * Returns the filename that the recruiter will see in the email attachment.
	 */
	private String getAttachmentName(String resumeType) {

		return switch (resumeType) {

		case "FULL_STACK" -> "Nitesh_Kumar_Full_Stack_Developer.pdf";

		case "BACKEND" -> "Nitesh_Kumar_Backend_Developer.pdf";

		case "BACKEND_LONG" -> "Nitesh_Kumar_Backend_Developer_Long.pdf";

		default -> throw new IllegalArgumentException("Invalid resume type: " + resumeType);
		};
	}

	/**
	 * Simple plain-text recruiter email.
	 *
	 * No HTML. No CSS.
	 *
	 * Designed to sound more natural and personal.
	 */
	private String buildSimpleEmail(String companyName) {

		boolean hasCompanyName = companyName != null && !companyName.isBlank();

		if (hasCompanyName) {

			return """
					Hi,

					Hope you're doing well.

					I’m reaching out to check if there are any suitable openings for a Java Backend Developer or Software Engineer at %s.

					I have 2.7 years of professional experience in Java, Spring Boot, REST APIs, Microservices, Hibernate/JPA, Spring Security and MySQL. I have worked on fintech/payment platforms and enterprise applications.

					I’m currently available as an immediate joiner and actively looking for my next opportunity.

					I’ve attached my resume for your reference. If there is a relevant opening, I’d appreciate it if you could consider my profile or share it with the appropriate hiring team.

					Thank you for your time.
					Regards,
					Nitesh Kumar
					Software Engineer | Java Backend Developer
					New Delhi, India
					Niteshsaini1296@gmail.com
					+91 8077911512
					"""
					.formatted(companyName);
		}

		return """
				Hi,

				Hope you're doing well.

				I’m reaching out to check if there are any suitable openings for a Java Backend Developer or Software Engineer.

				I have 2.7 years of professional experience in Java, Spring Boot, REST APIs, Microservices, Hibernate/JPA, Spring Security and MySQL. I have worked on fintech/payment platforms and enterprise applications.

				I’m currently available as an immediate joiner and actively looking for my next opportunity.

				I’ve attached my resume for your reference. If there is a relevant opening, I’d appreciate it if you could consider my profile or share it with the appropriate hiring team.

				Thank you for your time.
				Regards,
				Nitesh Kumar
				Software Engineer | Java Backend Developer
				New Delhi, India
				Niteshsaini1296@gmail.com
				+91 8077911512
				""";
	}

	/**
	 * Company-specific recruiter email.
	 */
	private String buildCompanySpecificEmail(String companyName) {

		return """
				<!DOCTYPE html>

				<html>

				<head>

				    <meta charset="UTF-8">

				    <meta name="viewport"
				          content="width=device-width, initial-scale=1.0">

				    <title>Job Application</title>

				</head>

				<body style="
				    margin:0;
				    padding:0;
				    background-color:#f4f6f8;
				    font-family:Arial,Helvetica,sans-serif;
				    color:#333333;
				">

				    <table width="100%%"
				           cellpadding="0"
				           cellspacing="0"
				           border="0"
				           style="padding:30px 10px;">

				        <tr>

				            <td align="center">

				                <table width="650"
				                       cellpadding="0"
				                       cellspacing="0"
				                       border="0"
				                       style="
				                           max-width:650px;
				                           width:100%%;
				                           background-color:#ffffff;
				                           border:1px solid #e5e7eb;
				                           border-radius:10px;
				                       ">

				                    <!-- Header -->

				                    <tr>

				                        <td style="
				                            padding:30px 35px 20px 35px;
				                            border-bottom:1px solid #eeeeee;
				                        ">

				                            <p style="
				                                margin:0;
				                                font-size:22px;
				                                font-weight:600;
				                                color:#111827;
				                            ">

				                                Nitesh Kumar

				                            </p>

				                            <p style="
				                                margin:6px 0 0 0;
				                                font-size:14px;
				                                color:#2563eb;
				                                font-weight:600;
				                            ">

				                                Software Engineer | Java Backend Developer

				                            </p>

				                        </td>

				                    </tr>


				                    <!-- Content -->

				                    <tr>

				                        <td style="
				                            padding:30px 35px 35px 35px;
				                        ">

				                            <p style="
				                                margin:0 0 22px 0;
				                                font-size:15px;
				                                line-height:1.7;
				                            ">

				                                Hi,

				                            </p>

				                            <p style="
				                                margin:0 0 20px 0;
				                                font-size:15px;
				                                line-height:1.7;
				                            ">

				                                Hope you're doing well.

				                            </p>

				                            <p style="
				                                margin:0 0 20px 0;
				                                font-size:15px;
				                                line-height:1.7;
				                            ">

				                                I'm reaching out to check if there are any

				                                <strong>Java Backend Developer</strong> or

				                                <strong>Software Engineer</strong> opportunities

				                                available at

				                                <strong>%s</strong>.

				                            </p>

				                            <p style="
				                                margin:0 0 20px 0;
				                                font-size:15px;
				                                line-height:1.7;
				                            ">

				                                I'm a Software Engineer with

				                                <strong>2.7 years of professional experience</strong>

				                                in developing enterprise and backend applications.

				                                I've worked across

				                                <strong>HRMS and fintech/payment platforms</strong>,

				                                building backend services, REST APIs, integrations,

				                                and database-driven applications.

				                            </p>


				                            <!-- Skills -->

				                            <table width="100%%"
				                                   cellpadding="0"
				                                   cellspacing="0"
				                                   border="0"
				                                   style="
				                                       margin:24px 0;
				                                       background-color:#f8fafc;
				                                       border-left:4px solid #2563eb;
				                                   ">

				                                <tr>

				                                    <td style="padding:18px 20px;">

				                                        <p style="
				                                            margin:0 0 10px 0;
				                                            font-size:14px;
				                                            font-weight:600;
				                                            color:#111827;
				                                        ">

				                                            Key Technical Skills

				                                        </p>

				                                        <p style="
				                                            margin:0;
				                                            font-size:14px;
				                                            line-height:1.8;
				                                            color:#4b5563;
				                                        ">

				                                            <strong>Java</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>Spring Boot</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>REST APIs</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>Microservices</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>Spring Security</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>Hibernate/JPA</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>MySQL</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>JWT/OAuth2</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>Kafka</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>Docker</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>AWS</strong>

				                                        </p>

				                                    </td>

				                                </tr>

				                            </table>


				                            <p style="
				                                margin:0 0 20px 0;
				                                font-size:15px;
				                                line-height:1.7;
				                            ">

				                                My previous experience includes working on

				                                <strong>fintech and payment gateway platforms</strong>,

				                                where I worked on payment integrations, transaction

				                                processing, authentication, REST APIs, SQL optimization,

				                                and AWS deployments.

				                                I'm currently working on an

				                                <strong>enterprise HRMS platform</strong>, working on

				                                backend functionality, business logic, reporting,

				                                SQL optimization, and application improvements.

				                            </p>


				                            <!-- Availability -->

				                            <table width="100%%"
				                                   cellpadding="0"
				                                   cellspacing="0"
				                                   border="0"
				                                   style="margin:24px 0;">

				                                <tr>

				                                    <td style="
				                                        padding:14px 18px;
				                                        background-color:#eff6ff;
				                                        border:1px solid #dbeafe;
				                                        border-radius:6px;
				                                    ">

				                                        <p style="
				                                            margin:0;
				                                            font-size:14px;
				                                            color:#1e3a8a;
				                                        ">

				                                            <strong>Availability:</strong>

				                                            Immediate Joiner

				                                        </p>

				                                    </td>

				                                </tr>

				                            </table>


				                            <p style="
				                                margin:0 0 22px 0;
				                                font-size:15px;
				                                line-height:1.7;
				                            ">

				                                I've attached my

				                                <strong>resume</strong> for your reference.

				                                If there is a suitable opportunity at

				                                <strong>%s</strong>, I would appreciate it if you

				                                could consider my profile or forward it to the

				                                relevant hiring team.

				                            </p>


				                            <p style="
				                                margin:0 0 30px 0;
				                                font-size:15px;
				                                line-height:1.7;
				                            ">

				                                Thank you for your time. I would be happy to discuss

				                                my experience if there is a relevant opportunity.

				                            </p>


				                            <!-- Signature -->

				                            <p style="
				                                margin:0;
				                                font-size:15px;
				                                line-height:1.6;
				                            ">

				                                Regards,

				                            </p>

				                            <p style="
				                                margin:4px 0 0 0;
				                                font-size:17px;
				                                font-weight:600;
				                                color:#111827;
				                            ">

				                                Nitesh Kumar

				                            </p>

				                            <p style="
				                                margin:4px 0 0 0;
				                                font-size:14px;
				                                color:#2563eb;
				                                font-weight:600;
				                            ">

				                                Software Engineer | Java Backend Developer

				                            </p>

				                            <p style="
				                                margin:8px 0 0 0;
				                                font-size:13px;
				                                color:#6b7280;
				                            ">

				                                New Delhi, India

				                            </p>

				                            <p style="
				                                margin:4px 0 0 0;
				                                font-size:13px;
				                                color:#6b7280;
				                            ">

				                                📧 Niteshsaini1296@gmail.com

				                                &nbsp;&nbsp;|&nbsp;&nbsp;

				                                📱 +91 8077911512

				                            </p>

				                        </td>

				                    </tr>


				                    <!-- Footer -->

				                    <tr>

				                        <td style="
				                            padding:15px 35px;
				                            background-color:#f9fafb;
				                            border-top:1px solid #eeeeee;
				                            text-align:center;
				                        ">

				                            <p style="
				                                margin:0;
				                                font-size:12px;
				                                color:#9ca3af;
				                            ">

				                                Resume attached for your consideration

				                            </p>

				                        </td>

				                    </tr>

				                </table>

				            </td>

				        </tr>

				    </table>

				</body>

				</html>

				""".formatted(companyName, companyName);
	}

	/**
	 * Generic recruiter email.
	 */
	private String buildGenericEmail() {

		return """
				<!DOCTYPE html>

				<html>

				<head>

				    <meta charset="UTF-8">

				    <meta name="viewport"
				          content="width=device-width, initial-scale=1.0">

				    <title>Java Backend Developer</title>

				</head>

				<body style="
				    margin:0;
				    padding:0;
				    background-color:#f4f6f8;
				    font-family:Arial,Helvetica,sans-serif;
				    color:#333333;
				">

				    <table width="100%%"
				           cellpadding="0"
				           cellspacing="0"
				           border="0"
				           style="padding:30px 10px;">

				        <tr>

				            <td align="center">

				                <table width="650"
				                       cellpadding="0"
				                       cellspacing="0"
				                       border="0"
				                       style="
				                           max-width:650px;
				                           width:100%%;
				                           background-color:#ffffff;
				                           border:1px solid #e5e7eb;
				                           border-radius:10px;
				                       ">

				                    <!-- Header -->

				                    <tr>

				                        <td style="
				                            padding:30px 35px 20px 35px;
				                            border-bottom:1px solid #eeeeee;
				                        ">

				                            <p style="
				                                margin:0;
				                                font-size:22px;
				                                font-weight:600;
				                                color:#111827;
				                            ">

				                                Nitesh Kumar

				                            </p>

				                            <p style="
				                                margin:6px 0 0 0;
				                                font-size:14px;
				                                color:#2563eb;
				                                font-weight:600;
				                            ">

				                                Software Engineer | Java Backend Developer

				                            </p>

				                        </td>

				                    </tr>


				                    <!-- Content -->

				                    <tr>

				                        <td style="
				                            padding:30px 35px 35px 35px;
				                        ">

				                            <p style="
				                                margin:0 0 22px 0;
				                                font-size:15px;
				                                line-height:1.7;
				                            ">

				                                Hi,

				                            </p>

				                            <p style="
				                                margin:0 0 20px 0;
				                                font-size:15px;
				                                line-height:1.7;
				                            ">

				                                Hope you're doing well.

				                            </p>

				                            <p style="
				                                margin:0 0 20px 0;
				                                font-size:15px;
				                                line-height:1.7;
				                            ">

				                                I'm reaching out to check if you are currently

				                                hiring for any

				                                <strong>Java Backend Developer</strong> or

				                                <strong>Software Engineer</strong> positions.

				                            </p>

				                            <p style="
				                                margin:0 0 20px 0;
				                                font-size:15px;
				                                line-height:1.7;
				                            ">

				                                I'm a Software Engineer with

				                                <strong>2.7 years of professional experience</strong>

				                                in developing enterprise and backend applications.

				                                My experience includes working with

				                                <strong>Java, Spring Boot, REST APIs, Microservices,

				                                Hibernate/JPA, Spring Security, and MySQL</strong>.

				                            </p>


				                            <!-- Skills -->

				                            <table width="100%%"
				                                   cellpadding="0"
				                                   cellspacing="0"
				                                   border="0"
				                                   style="
				                                       margin:24px 0;
				                                       background-color:#f8fafc;
				                                       border-left:4px solid #2563eb;
				                                   ">

				                                <tr>

				                                    <td style="padding:18px 20px;">

				                                        <p style="
				                                            margin:0 0 10px 0;
				                                            font-size:14px;
				                                            font-weight:600;
				                                            color:#111827;
				                                        ">

				                                            Key Technical Skills

				                                        </p>

				                                        <p style="
				                                            margin:0;
				                                            font-size:14px;
				                                            line-height:1.8;
				                                            color:#4b5563;
				                                        ">

				                                            <strong>Java</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>Spring Boot</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>REST APIs</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>Microservices</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>Spring Security</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>Hibernate/JPA</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>MySQL</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>JWT/OAuth2</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>Kafka</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>Docker</strong>
				                                            &nbsp;•&nbsp;

				                                            <strong>AWS</strong>

				                                        </p>

				                                    </td>

				                                </tr>

				                            </table>


				                            <p style="
				                                margin:0 0 20px 0;
				                                font-size:15px;
				                                line-height:1.7;
				                            ">

				                                I've worked on both

				                                <strong>fintech/payment platforms</strong>

				                                and an

				                                <strong>enterprise HRMS platform</strong>,

				                                including backend development, payment integrations,

				                                REST API development, authentication, SQL optimization,

				                                reporting, and application improvements.

				                            </p>


				                            <!-- Availability -->

				                            <table width="100%%"
				                                   cellpadding="0"
				                                   cellspacing="0"
				                                   border="0"
				                                   style="margin:24px 0;">

				                                <tr>

				                                    <td style="
				                                        padding:14px 18px;
				                                        background-color:#eff6ff;
				                                        border:1px solid #dbeafe;
				                                        border-radius:6px;
				                                    ">

				                                        <p style="
				                                            margin:0;
				                                            font-size:14px;
				                                            color:#1e3a8a;
				                                        ">

				                                            <strong>Availability:</strong>

				                                            Immediate Joiner

				                                        </p>

				                                    </td>

				                                </tr>

				                            </table>


				                            <p style="
				                                margin:0 0 22px 0;
				                                font-size:15px;
				                                line-height:1.7;
				                            ">

				                                I've attached my

				                                <strong>resume</strong> for your reference.

				                                If you come across a suitable opening that matches

				                                my background, I would really appreciate it if you

				                                could consider my profile or share it with the

				                                relevant hiring team.

				                            </p>


				                            <p style="
				                                margin:0 0 30px 0;
				                                font-size:15px;
				                                line-height:1.7;
				                            ">

				                                Thank you for your time. I would be happy to discuss

				                                my experience if there is a relevant opportunity.

				                            </p>


				                            <!-- Signature -->

				                            <p style="
				                                margin:0;
				                                font-size:15px;
				                                line-height:1.6;
				                            ">

				                                Regards,

				                            </p>

				                            <p style="
				                                margin:4px 0 0 0;
				                                font-size:17px;
				                                font-weight:600;
				                                color:#111827;
				                            ">

				                                Nitesh Kumar

				                            </p>

				                            <p style="
				                                margin:4px 0 0 0;
				                                font-size:14px;
				                                color:#2563eb;
				                                font-weight:600;
				                            ">

				                                Software Engineer | Java Backend Developer

				                            </p>

				                            <p style="
				                                margin:8px 0 0 0;
				                                font-size:13px;
				                                color:#6b7280;
				                            ">

				                                New Delhi, India

				                            </p>

				                            <p style="
				                                margin:4px 0 0 0;
				                                font-size:13px;
				                                color:#6b7280;
				                            ">

				                                📧 Niteshsaini1296@gmail.com

				                                &nbsp;&nbsp;|&nbsp;&nbsp;

				                                📱 +91 8077911512

				                            </p>

				                        </td>

				                    </tr>


				                    <!-- Footer -->

				                    <tr>

				                        <td style="
				                            padding:15px 35px;
				                            background-color:#f9fafb;
				                            border-top:1px solid #eeeeee;
				                            text-align:center;
				                        ">

				                            <p style="
				                                margin:0;
				                                font-size:12px;
				                                color:#9ca3af;
				                            ">

				                                Resume attached for your consideration

				                            </p>

				                        </td>

				                    </tr>

				                </table>

				            </td>

				        </tr>

				    </table>

				</body>

				</html>

				""";
	}

}