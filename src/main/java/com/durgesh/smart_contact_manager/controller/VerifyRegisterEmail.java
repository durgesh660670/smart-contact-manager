package com.durgesh.smart_contact_manager.controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import com.durgesh.smart_contact_manager.helper.Message;
import com.durgesh.smart_contact_manager.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerifyRegisterEmail {

    @Autowired
    EmailService eS;

    @PostMapping("send_email")
    public ResponseEntity sendEmail(@RequestBody Map<String, Object> data, HttpSession session) {
        Map<String, String> map = new HashMap<String, String>();

        String email = data.get("email").toString();

        if (email == null) {
            map.put("msg", "email is empty");
            return ResponseEntity.ok(map);

        }
        // generating otp of 4 digit

        String otp = new DecimalFormat("0000").format(new Random().nextInt(9999));
        System.out.println(otp);
        session.setAttribute("otp", otp);

        // code for sending otp to email...

        String subject = "OTP from smart contact manager";
        String to = email;
        String from = "dks660670@gmail.com";
        String message = "your otp is:" + otp;

        boolean f = eS.sendEmail(message, subject, to, from);
        if (f == false) {
            // session.setAttribute("message", new Message("Somthing went wrong while
            // sending email", "alert-danger"));
            map.put("msg", "Something Went wrong!");
            return ResponseEntity.ok(map);
        }

        map.put("msg", "ok");

        return ResponseEntity.ok(map);
    }

    @PostMapping("/process_verify_email_otp")
    public ResponseEntity<?> processVerifyEmailOtp(@RequestBody Map<String, Object> data,
            HttpSession session) {

        String otp = data.get("otp").toString();

        Map<String, String> map = new HashMap<String, String>();

        System.out.println("inside processVerifyEmailOtp ....");

        String sessionOtp = session.getAttribute("otp").toString();

        if(sessionOtp==null){

            map.put("msg", "otp is not in session...");
            return ResponseEntity.ok(map);
        }

        if (!sessionOtp.equals(otp)) {

            session.setAttribute("message", new Message("OTP does not match", "alert-danger"));
            session.removeAttribute("otp");

            map.put("msg", "OTP does not match");
            return ResponseEntity.ok(map);

        }

        session.removeAttribute("otp");
        // session.removeAttribute("username");
        map.put("msg", "success");
        return ResponseEntity.ok(map);

    }

}
