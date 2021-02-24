package efd.rest.repositories;


import efd.model.Project;
import efd.model.Study;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jt on 9/24/17.
 */

public interface StudyRepository extends JpaRepository<Study, String> {

    Project findByStudyName(String name);

}
