package efd.rest.domain;

import efd.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;

/*
    @Author david
    @Create 16/02/2021 15:41
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModellingScenarioDto {


    private String title;


    private String author;


    private Date date;


    private String description;


    private Study study;


    private Project project;


    private LivelihoodZone livelihoodZone;


    private Collection<PriceYieldVariation> priceYieldVariations;


    private Collection<FoodSubstitution> foodSubstitution;

    private String modelType;

}