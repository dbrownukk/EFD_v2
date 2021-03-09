package efd.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by david on 03/03/2021.
 */
@Data
@AllArgsConstructor
public class LivelihoodZoneListDTO {

    List<LivelihoodZoneDTO> livelihoodZones;

}
