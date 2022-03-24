package com.durgesh.smart_contact_manager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.durgesh.smart_contact_manager.dao.ContactRepository;
import com.durgesh.smart_contact_manager.dao.UserRepository;
import com.durgesh.smart_contact_manager.entities.Contact;
import com.durgesh.smart_contact_manager.entities.User;
import com.durgesh.smart_contact_manager.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository ur;

    @Autowired
    ContactRepository cr;

    private List<Contact> findContactsByUser;

    public UserRepository getUr() {
        return this.ur;
    }

    public void setUr(UserRepository ur) {
        this.ur = ur;
    }

    public ContactRepository getCr() {
        return this.cr;
    }

    public void setCr(ContactRepository cr) {
        this.cr = cr;
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userName = principal.getName();

        User user = this.ur.getUserByUserName(userName);

        model.addAttribute("user", user);

    }

    @GetMapping("/index")
    public String index(Model model) {

        model.addAttribute("title", "Dashboard - Smart contact manager");
        System.out.println("inside index....");

        return "normal/index";
    }

    @GetMapping("/add_contact_form")
    public String openAddContactForm(Model model) {

        model.addAttribute("title", "Add Contact Form - Smart contact manager");
        System.out.println("openAddContactForm....");

        model.addAttribute("contact", new Contact());

        return "normal/add_contact_form";
    }

    @PostMapping("/process_add_contact_form")
    public String process_add_contact_form(@Valid @ModelAttribute("contact") Contact contact, BindingResult bResult,
            Model model, Principal p, @RequestParam("img") MultipartFile file, HttpSession session) {

        try {
            model.addAttribute("title", "Add Contact Form - Smart contact manager");
            System.out.println("process_add_contact_form....");

            if (bResult.hasErrors()) {
                System.out.println("process_add_contact_form_" + bResult.toString());
                model.addAttribute("contact", contact);
                return "normal/add_contact_form";
            }
            String name = p.getName();

            User user = this.ur.getUserByUserName(name);

            // processing and uploading file..

            if (file.isEmpty()) {
                System.out.println("image is empty...");
                contact.setImage("default.png");

            } else {

                System.out.println("image .." + file.getOriginalFilename());
                contact.setImage(file.getOriginalFilename());

                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("image is uploaded...");

            }

            contact.setUser(user);
            user.getContact().add(contact);

            User result = this.ur.save(user);
            if (result == null) {
                session.setAttribute("message", new Message("Something went wrong!", "alert-danger"));
                return "normal/add_contact_form";
            }

            session.setAttribute("message", new Message("Contact Added Successfully.", "alert-success"));

        } catch (Exception e) {
            session.setAttribute("message", new Message("Something went wrong!", "alert-danger"));

            e.printStackTrace();

        }

        model.addAttribute("contact", new Contact());

        return "normal/add_contact_form";
    }

    // show contacts handler
    // per page =5[n]

    // current page=0 [page]
    @GetMapping("/show_contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model model, Principal p) {

        model.addAttribute("title", "Show Contacts Form - Smart contact manager");
        System.out.println("show_contacts....");

        String name = p.getName();

        User user = this.ur.getUserByUserName(name);

        // pagination

        Pageable pageRequest = PageRequest.of(page, 2);

        Page<Contact> contacts = this.cr.findContactsByUser(user.getUId(), pageRequest);

        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contacts.getTotalPages());

        return "normal/show_contacts";
    }

    @GetMapping("/show_contacts/contact/{cId}")
    public String showContact(@PathVariable("cId") Integer cId, Model model, Principal p) {

        model.addAttribute("title", "Show Contact Form - Smart contact manager");
        System.out.println("showContact....");

        String name = p.getName();

        User user = this.ur.getUserByUserName(name);

        Optional<Contact> contact = this.cr.findById(cId);

        System.out.println("contact...." + contact);
        Contact contact2 = contact.get();

        model.addAttribute("contact", contact2);

        return "normal/show_contact";
    }

    // delet contacts
    @GetMapping("/show_contacts/delete/{cId}")
    public String deleteContact(@PathVariable("cId") Integer cId, Model model, Principal p, HttpSession session) {

        model.addAttribute("title", "Show Contact Form - Smart contact manager");
        System.out.println("inside deletContact....");

        try {

            String name = p.getName();

            User user = this.ur.getUserByUserName(name);

            Optional<Contact> contact = this.cr.findById(cId);
            System.out.println("contact...." + contact);
            Contact contact2 = contact.get();

            this.cr.delete(contact2);

            session.setAttribute("message", new Message("Contact deleted Successfully.", "alert-success"));

            model.addAttribute("contact", contact2);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong.", "alert-danger"));
        }

        return "redirect:/user/show_contacts/0";
    }

    // open update form

    @GetMapping("/show_contacts/update/{cId}")
    public String openUpdaeContactForm(@PathVariable("cId") Integer cId, Model model) {

        model.addAttribute("title", "Update Contact Form - Smart contact manager");
        System.out.println("openUpdaeContactForm....");

        Optional<Contact> contact = this.cr.findById(cId);

        System.out.println("contact...." + contact);
        Contact contact2 = contact.get();

        model.addAttribute("contact", contact2);

        return "normal/update_contact_form";
    }

    // process update contact form

    @PostMapping("/process_update_contact_form")
    public String process_update_contact_form(@Valid @ModelAttribute("contact") Contact contact, BindingResult bResult,
            Model model, Principal p, @RequestParam("img") MultipartFile file, HttpSession session,
            @RequestParam("cId") Integer cId) {

        try {
            model.addAttribute("title", "update Contact Form - Smart contact manager");
            System.out.println("process_update_contact_form....");

            if (bResult.hasErrors()) {
                System.out.println("process_add_contact_form_" + bResult.toString());
                model.addAttribute("contact", contact);
                return "normal/add_contact_form";
            }
            // processing and uploading file..

            if (file.isEmpty()) {
                System.out.println("image is empty...");
                contact.setImage("default.png");

            } else {

                System.out.println("image .." + file.getOriginalFilename());
                contact.setImage(file.getOriginalFilename());

                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("image is uploaded...");

            }

            String userName = p.getName();

            User user = this.ur.getUserByUserName(userName);
            contact.setUser(user);
            this.cr.save(contact);

            session.setAttribute("message", new Message("Contact updated Successfully.", "alert-success"));

        } catch (Exception e) {
            session.setAttribute("message", new Message("Something went wrong!", "alert-danger"));

            e.printStackTrace();

        }

        model.addAttribute("contact", new Contact());

        return "normal/update_contact_form";
    }


    // your_profile

    @GetMapping("/your_profile")
    public String yourProfile(Model model) {

        model.addAttribute("title", "Your profile Form - Smart contact manager");
        System.out.println("show_contacts....");

       

        return "normal/your_profile";
    }


}
