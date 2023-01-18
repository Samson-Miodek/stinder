package com.example.demo.repository;

import com.example.demo.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Id;

public interface PublicationRepository extends JpaRepository<Publication, Id> {

}
