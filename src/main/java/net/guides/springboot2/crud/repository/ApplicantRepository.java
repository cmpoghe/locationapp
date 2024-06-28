package net.guides.springboot2.crud.repository;

import net.guides.springboot2.crud.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository  extends JpaRepository<Applicant,Long> {
    Applicant findByName(String name);

    Applicant findByApplicant(String applicant);
}
