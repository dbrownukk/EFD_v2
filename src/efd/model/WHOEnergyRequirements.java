package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.model.*;

import efd.actions.*;
import efd.validations.*;

/*
 * 
 * WHO Daily energy requirements by Age and Sex
 * 
 */

@Entity



public class WHOEnergyRequirements extends Identifiable{

private int age;
private int male;
private int female;
public int getAge() {
	return age;
}
public void setAge(int age) {
	this.age = age;
}
public int getMale() {
	return male;
}
public void setMale(int male) {
	this.male = male;
}
public int getFemale() {
	return female;
}
public void setFemale(int female) {
	this.female = female;
}

public static WHOEnergyRequirements findByAge(int age) throws NoResultException {
	 Query query = XPersistence.getManager().createQuery(
	 "from WHOEnergyRequirements where age = :age");
	 query.setParameter("age", age);
	 return (WHOEnergyRequirements) query.getSingleResult();
	}


	
	
	
}
