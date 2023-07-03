package ru.stinder.repository;

import ru.stinder.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Id;

public interface PublicationRepository extends JpaRepository<Publication, Id> {

}
