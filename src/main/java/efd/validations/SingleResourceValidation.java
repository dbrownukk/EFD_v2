package efd.validations;

import java.util.*;

import org.openxava.util.*;
import org.openxava.validators.*;

import efd.rest.domain.model.*;

public class SingleResourceValidation implements IValidator {

	private Collection<Category> category;
	private Collection<ResourceType> resourceType;
	private Collection<ResourceSubType> resourceSubType;

	@Override
	public void validate(Messages errors) throws Exception {

		System.out.println("in validate of CRS 1");
		
		if(category == null && resourceSubType == null && resourceType == null)
		{
		return;	
		}

		System.out.println("category = " + category.isEmpty());
		System.out.println("RT = " + resourceType.isEmpty());
		System.out.println("RST = " + resourceSubType.isEmpty());
		
		
		
		if ((((category.isEmpty()) ? 0:1)  +  ((resourceSubType.isEmpty()) ? 0:1) + ((resourceType.isEmpty())?0:1))>1)
		{
			System.out.println("in validate of CRS 1 all null");
			errors.add("Custom Report Specification can only have one of Category, Resource Type or Resource Subtype");
		}
			


	}

	public Collection<Category> getCategory() {
		return category;
	}

	public void setCategory(Collection<Category> category) {
		this.category = category;
	}

	public Collection<ResourceType> getResourceType() {
		return resourceType;
	}

	public void setResourceType(Collection<ResourceType> resourceType) {
		this.resourceType = resourceType;
	}

	public Collection<ResourceSubType> getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(Collection<ResourceSubType> resourceSubType) {
		this.resourceSubType = resourceSubType;
	}

}
