package com.durgesh.smart_contact_manager.controller;

import java.text.DecimalFormat;
import java.util.Random;

import javax.servlet.http.HttpSession;

import com.durgesh.smart_contact_manager.dao.UserRepository;
import com.durgesh.smart_contact_manager.entities.User;
import com.durgesh.smart_contact_manager.helper.Message;
import com.durgesh.smart_contact_manager.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {

    @Autowired
    PasswordEncoder pE;

    @Autowired
    UserRepository uR;

    @Autowired
    EmailService eS;

    @GetMapping("/forgot_password")
    public String openForgotPasswordForm(Model model) {

        model.addAttribute("title", "Forgot Password - Smart contact manager");
        System.out.println("inside forgot password ....");

        return "forgot_password_form";
    }

    @PostMapping("/process_forgot_password")
    public String processForgotPassword(@RequestParam("username") String username, Model model, HttpSession session) {

        model.addAttribute("title", "Forgot Password - Smart contact manager");
        System.out.println("inside process_forgot_password ....");

        // generating otp of 4 digit

        String otp = new DecimalFormat("0000").format(new Random().nextInt(9999));
        System.out.println(otp);

        // code for sending otp to email...

        String subject = "OTP from smart contact manager";
        String to = username;
        String from = "dks660670@gmail.com";
        String message = "your otp is:" + otp;

        boolean f = eS.sendEmail(message, subject, to, from);
        if (f == false) {
            session.setAttribute("message", new Message("Somthing went wrong while sending email", "alert-danger"));
            return "forgot_password_form";
        }
        session.setAttribute("otp", otp);
        session.setAttribute("username", username);
        session.setAttribute("message", new Message("We have successfully sent otp in your email", "alert-success"));
        // System.out.println("otp .." + otp);

        return "verify_otp";
    }

    @PostMapping("/process_verify_forgot_password_otp")
    public String processVerifyForgotPasswordOtp(Model model, @RequestParam("otp") String otp,
            HttpSession session) {

        model.addAttribute("title", "processVerifyForgotPasswordOtp - Smart contact manager");
        System.out.println("inside processVerifyForgotPasswordOtp ....");

        String sessionOtp = session.getAttribute("otp").toString();
        String sessionUsername = session.getAttribute("username").toString();

        User user = this.uR.getUserByUserName(sessionUsername);

        if (user == null) {

            session.setAttribute("message", new Message("User does not exist with this Email.", "alert-danger"));
            // session.removeAttribute("otp");
            // session.removeAttribute("username");
            return "verify_otp";
        }

        if (!sessionOtp.equals(otp)) {

            session.setAttribute("message", new Message("OTP does not match", "alert-danger"));
            // session.removeAttribute("otp");
            // session.removeAttribute("username");

            return "verify_otp";

        }

        // session.removeAttribute("otp");
        // session.removeAttribute("username");

        return "change_password";

    }

    @PostMapping("/process_change_password")
    public String processChangePassword(Model model,
            @RequestParam("c-n-password") String c_n_password,
            @RequestParam("new-password") String new_password,
            HttpSession session) {

        model.addAttribute("title", "process_change_password - Smart contact manager");
        System.out.println("inside process_verify_forgot_password_otp....");

        if (!new_password.equals(c_n_password)) {

            session.setAttribute("message",
                    new Message("Password and confirm Password did not match!", "alert-danger"));
            return "change_password";
        }

        String sessionUsername = session.getAttribute("username").toString();

        // System.out.println(new_password);

        User user = this.uR.getUserByUserName(sessionUsername);
        session.removeAttribute("username");
        user.setPassword(this.pE.encode(new_password));

        User result = this.uR.save(user);
        if (result == null) {
            // System.out.println(result);
            session.setAttribute("message", new Message("Something went wrong...",
                    "alert-danger"));
            return "change_password";
        }

        session.setAttribute("message", new Message("Password Successfully changed!",
                "alert-success"));

        return "login";
    }

}
