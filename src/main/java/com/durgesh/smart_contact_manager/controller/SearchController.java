package com.durgesh.smart_contact_manager.controller;

import java.security.Principal;
import java.util.List;

import com.durgesh.smart_contact_manager.dao.ContactRepository;
import com.durgesh.smart_contact_manager.dao.UserRepository;
import com.durgesh.smart_contact_manager.entities.Contact;
import com.durgesh.smart_contact_manager.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class SearchController {

    @Autowired
    UserRepository ur;

    @Autowired
    ContactRepository cr;

    // search handler

    @GetMapping("/user/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query, Principal p) {

        System.out.println("inside search handler....");

        User user = this.ur.getUserByUserName(p.getName());
        List<Contact> contacts = this.cr.findByNameContainingAndUser(query, user);

        return ResponseEntity.ok(contacts);
    }
}
