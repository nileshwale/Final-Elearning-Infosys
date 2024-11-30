package com.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.application.model.Professor;
import com.application.model.User;
import com.application.services.ProfessorService;
import com.application.services.UserService;

@RestController
public class RegistrationController {

  @Autowired
  private UserService userService;

  @Autowired
  private ProfessorService professorService;

  @Autowired
  private JavaMailSender mailSender; // Inject JavaMailSender here

  @PostMapping("/registeruser")
  @CrossOrigin(origins = "http://localhost:4200")
  public User registerUser(@RequestBody User user) throws Exception {
    String currEmail = user.getEmail();
    String newID = getNewID();
    user.setUserid(newID);

    if (currEmail != null && !"".equals(currEmail)) {
      User userObj = userService.fetchUserByEmail(currEmail);
      if (userObj != null) {
        throw new Exception("User with " + currEmail + " already exists !!!");
      }
    }
    User userObj = userService.saveUser(user);

    // Send the welcome email after successful registration
    sendRegistrationEmail(user.getEmail());

    return userObj;
  }

  @PostMapping("/registerprofessor")
  @CrossOrigin(origins = "http://localhost:4200")
  public Professor registerDoctor(@RequestBody Professor professor) throws Exception {
    String currEmail = professor.getEmail();
    String newID = getNewID();
    professor.setProfessorid(newID);

    if (currEmail != null && !"".equals(currEmail)) {
      Professor professorObj = professorService.fetchProfessorByEmail(currEmail);
      if (professorObj != null) {
        throw new Exception("Professor with " + currEmail + " already exists !!!");
      }
    }
    Professor professorObj = professorService.saveProfessor(professor);

    // Send the welcome email after successful registration
    sendRegistrationEmail(professor.getEmail());

    return professorObj;
  }

  public String getNewID() {
    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 12; i++) {
      int index = (int) (AlphaNumericString.length() * Math.random());
      sb.append(AlphaNumericString.charAt(index));
    }
    return sb.toString();
  }

  // Method to send email
  private void sendRegistrationEmail(String email) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom("xpert0.mr.aniket@gmail.com"); // Use a professional sender email
      message.setTo(email);
      message.setSubject("Welcome to E-Learning - Empowering Your Learning Journey!");

      // Enhanced email content
      String emailContent = "Dear User,\n\n"
        + "Welcome to *E-Learning*, where learning meets innovation!\n\n"
        + "We are thrilled to have you as part of our growing community of learners and educators. "
        + "Whether you are here to acquire new skills, share your knowledge, or inspire others, "
        + "you are at the right place to achieve your goals.\n\n"
        + "Here are some next steps to get started:\n"
        + "1. Explore our platform and customize your profile.\n"
        + "2. Check out the latest courses and learning opportunities.\n"
        + "3. Engage with our community and stay connected.\n\n"
        + "Our team is committed to supporting you every step of the way. Should you have any questions or need assistance, "
        + "please feel free to reach out to us at support@elearning.com.\n\n"
        + "We look forward to seeing you thrive!\n\n"
        + "Warm regards,\n"
        + "The E-Learning Team\n"
        + "Empowering Your Learning Journey";

      message.setText(emailContent); // Set the improved email content

      mailSender.send(message);
    } catch (Exception e) {
      // Log error if email fails to send
      System.out.println("Error sending email: " + e.getMessage());
    }
  }

}
