package efd.rest.mapper;


import efd.model.LivelihoodZone;
import efd.rest.dto.LivelihoodZoneDTO;
import org.mapstruct.Mapper;

/*
    @Author david
    @Create 16/02/2021 15:52
*/
@Mapper(componentModel = "spring")

public interface LivelihoodZoneMapper {


    LivelihoodZoneDTO  lzTolzDto(LivelihoodZone livelihoodZone);

    LivelihoodZone lzDtoTolz(LivelihoodZoneDTO livelihoodZoneDTO);

}
