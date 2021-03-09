package efd.rest.dto;

import efd.model.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by David on 03/03/2021.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LivelihoodZoneDTO {
    private String lzid;
    private String lzname;
    private Country country;
   // private Collection<Site> site;

}
