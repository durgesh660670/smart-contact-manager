package com.durgesh.smart_contact_manager.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.durgesh.smart_contact_manager.dao.UserRepository;
import com.durgesh.smart_contact_manager.entities.User;
import com.durgesh.smart_contact_manager.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private PasswordEncoder pE;

    public PasswordEncoder getPE() {
        return this.pE;
    }

    public void setPE(PasswordEncoder pE) {
        this.pE = pE;
    }

    @Autowired
    private UserRepository ur;

    public UserRepository getUr() {
        return this.ur;
    }

    public void setUr(UserRepository ur) {
        this.ur = ur;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("title", "Home - Smart contact manager");
        System.out.println("inside home....");

        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {

        model.addAttribute("title", "About - Smart contact manager");
        System.out.println("inside home....");

        return "About";
    }

    @GetMapping("/register")
    public String register(Model model) {

        model.addAttribute("title", "Registration - Smart contact manager");
        model.addAttribute("user", new User());
        System.out.println("inside Registration....");

        return "register";
    }

    @PostMapping("/process_register")
    public String process_register(@Valid @ModelAttribute("user") User user, BindingResult bResult,
            @RequestParam(value = "agreement", defaultValue = "false") Boolean agreement, Model model,
            HttpSession session) {

        System.out.println("inside process_register...." + agreement);

        try {

            if (!agreement) {
                throw new Exception("You have not agreed the terms and conditions.");

            }

            if (bResult.hasErrors()) {
                System.out.println("error:");
                model.addAttribute("user", user);
                return "register";
            }
            user.setRole("ROLE_NORMAL");
            user.setActive(true);
            user.setImage("default.png");
            System.out.println(user.getPassword());
            user.setPassword(pE.encode(user.getPassword()));

            User result = ur.save(user);

            session.setAttribute("message", new Message("Successfully Registered!", "alert-success"));

            model.addAttribute("user", new User());

        } catch (Exception e) {

            model.addAttribute("user", user);
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong" + e.getMessage(), "alert-danger"));
            return "register";
        }
        return "register";

    }

    @GetMapping("/signin")
    public String login(Model model) {

        model.addAttribute("title", "Login - Smart contact manager");
        System.out.println("inside login....");
        return "login";
    }

}
