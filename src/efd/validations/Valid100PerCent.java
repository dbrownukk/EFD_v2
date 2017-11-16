package efd.validations;

import org.openxava.util.*;
import org.openxava.validators.*;
import efd.model.*;
import java.math.*;
import org.openxava.actions.*;

public class Valid100PerCent 
	implements IPropertyValidator {
	
	public void validate(  // Required  because of IPropertyValidator (2)
	        Messages errors,  // Here you add the error messages (3)
	        Object object,  // The value to validate
	        String objectName,  // The entity name, usually to use in message
	        String propertyName)  // The property name, usually to use in message
	    {
	        if (object == null) return;
	        if (!(object instanceof Integer)) {
	            errors.add(  // If you add an error the validation fails
	                "expected_type",  // Message id in i18n file
	                propertyName,  // Arguments for i18n message
	                objectName,
	                "integer");
	            return;
	        }
	        System.out.println("in validation 101 ");
	    
	        Integer n = (Integer) object;
	        if (n.intValue() > 100) {
	            errors.add("not_greater_1000");  // Message id in i18n file
	        }
	        
	        
	    }

}
