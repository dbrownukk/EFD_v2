package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;



@Embeddable


@Table(name = "Transfer")

public class Transfer extends Asset{

	/* Note fk to resourcetype is in supertype Asset */

	@Column(name = "TransferredResourceName", length = 50)
	private String transferredResourceName;

	@Column(name = "LocalUnit", length = 45)
	private String localUnit;
	

	
	@Column(name = "QuantityReceived", nullable=false)
	@NotNull
	private Integer quantityReceived;
	
	@Column(name = "QuantitySold")
	private Integer quantitySold;
	
	@Column(name = "PricePerUnit" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	private BigDecimal pricePerUnit;
	
	@Column(name = "OtherUse", length = 255)
	private String otherUse;



	public String getTransferredResourceName() {
		return transferredResourceName;
	}

	public void setTransferredResourceName(String transferredResourceName) {
		this.transferredResourceName = transferredResourceName;
	}

	public String getLocalUnit() {
		return localUnit;
	}

	public void setLocalUnit(String localUnit) {
		this.localUnit = localUnit;
	}



	public Integer getQuantityReceived() {
		return quantityReceived;
	}

	public void setQuantityReceived(Integer quantityReceived) {
		this.quantityReceived = quantityReceived;
	}

	public Integer getQuantitySold() {
		return quantitySold;
	}

	public void setQuantitySold(Integer quantitySold) {
		this.quantitySold = quantitySold;
	}

	public BigDecimal getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(BigDecimal pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getOtherUse() {
		return otherUse;
	}

	public void setOtherUse(String otherUse) {
		this.otherUse = otherUse;
	}
	
	
	
	
}
