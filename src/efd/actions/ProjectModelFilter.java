package efd.actions;

import java.util.*;

import javax.management.relation.*;

import org.openxava.filters.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;
import com.openxava.naviox.model.Role;


public class ProjectModelFilter implements IFilter {

	private Collection<Role> roles;

	public Object filter(Object o) throws FilterException {

		/*
		 * Which Model OIHM or OHEA based on role containing either term
		 */

		Object[] r = new Object[1];

		String userName = Users.getCurrent();
		User user = User.find(userName);
	
		System.out.println("user = " + user.getName());
		
	    
		//Collection<Role> roles2 = user.getRoles();
		//Iterator<Role> iterator = roles2.iterator();
		//while(iterator.hasNext())
		//	System.out.println("role = "+iterator.next().getName());
		
		
		if (user.hasRole("oihm_user"))
			r[0] = "OIHM";
		else if(user.hasRole("ohea_user"))
			r[0] = "OHEA";

		System.out.println("role = " + r[0]);

		return r;
	}


}