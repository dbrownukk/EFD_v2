package efd.rest.repositories;


import efd.model.LivelihoodZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jt on 9/24/17.
 */

public interface LivelihoodZoneRepository extends JpaRepository<LivelihoodZone, String> {

    LivelihoodZone findByLzid(String id);
    LivelihoodZone findByLzname(String name);
    public List<LivelihoodZone> findByCountry(String country);

}
