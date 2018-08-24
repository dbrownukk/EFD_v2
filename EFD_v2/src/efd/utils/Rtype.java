package efd.utils;

/* TO hold Resource Type and Sub Type */

public class Rtype {
		String rtype;
		String rsubtype;
	
	
	public Rtype(String type, String rsubtype){
			this.rtype = rtype;
			this.rsubtype = rsubtype;
		}


	public String getRtype() {
		return rtype;
	}


	public void setRtype(String rtype) {
		this.rtype = rtype;
	}


	public String getRsubtype() {
		return rsubtype;
	}


	public void setRsubtype(String rsubtype) {
		this.rsubtype = rsubtype;
	}


}
