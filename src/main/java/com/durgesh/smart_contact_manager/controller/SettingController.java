package com.durgesh.smart_contact_manager.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import com.durgesh.smart_contact_manager.dao.ContactRepository;
import com.durgesh.smart_contact_manager.dao.UserRepository;
import com.durgesh.smart_contact_manager.entities.User;
import com.durgesh.smart_contact_manager.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class SettingController {

    @Autowired
    UserRepository ur;

    @Autowired
    ContactRepository cr;

    @Autowired
    PasswordEncoder pE;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userName = principal.getName();

        User user = this.ur.getUserByUserName(userName);

        model.addAttribute("user", user);

    }

    @GetMapping("/settings")
    public String openSetting(Model model) {

        model.addAttribute("title", "Setting - Smart contact manager");
        System.out.println("inside setting....");

        return "normal/setting";
    }

    @PostMapping("/process_change_password")
    public String process_change_password(Model model, @RequestParam("o-password") String o_password,
            @RequestParam("c-n-password") String c_n_password, @RequestParam("new-password") String new_password,
            Principal principal, HttpSession session) {

        model.addAttribute("title", "process_change_password - Smart contact manager");
        System.out.println("inside process_change_password....");
        
        User user = this.ur.getUserByUserName(principal.getName());

        boolean matches = this.pE.matches(o_password, user.getPassword());

        if (!new_password.equals(c_n_password)) {

            session.setAttribute("message", new Message("Password and confirm Password did not match!", "alert-danger"));
            return "normal/setting";
        }
        else if(matches==false){
            session.setAttribute("message", new Message("Old Password did not match!", "alert-danger"));
            return "normal/setting";

        }


        // System.out.println(new_password);

        user.setPassword(this.pE.encode(new_password));

        User result = this.ur.save(user);
        if (result != null) {
            // System.out.println(result);
            session.setAttribute("message", new Message("Password Successfully changed!", "alert-success"));
        }

        return "normal/setting";
    }

}
