package efd.validations;



import org.openxava.util.*;
import org.openxava.validators.*;
import efd.model.*;
import java.math.*;
import org.openxava.actions.*;
import org.openxava.*;

public class Valid100PerCent implements IPropertyValidator {
	
	public void validate( // Required because of IPropertyValidator (2)
			Messages errors, // Here you add the error messages (3)
			Object value, // The value to validate
			String objectName, // The entity name, usually to use in message
			String propertyName, // The property name, usually to use in message
			String communityid
			)

			throws Exception {
		System.out.println("In validator");
		if (value == null)
			return;
	}

	@Override
	public void validate(Messages errors, Object value, String propertyName, String modelName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
		
	}

