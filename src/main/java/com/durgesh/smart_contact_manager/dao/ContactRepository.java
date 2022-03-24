package com.durgesh.smart_contact_manager.dao;

import java.util.List;

import com.durgesh.smart_contact_manager.entities.Contact;
import com.durgesh.smart_contact_manager.entities.User;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    // current page
    // contact per page =5
    @Query("from Contact as c where c.user.uId=:uId")
    public Page<Contact> findContactsByUser(@Param("uId") int uId, Pageable pageable);

    //search method
    public List<Contact> findByNameContainingAndUser(String keywords, User user);

}
