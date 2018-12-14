package com.scb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter @Setter @Builder @Entity @Table(name="persistantdata") @NoArgsConstructor @AllArgsConstructor @ToString @XmlRootElement
public class PersistanceData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	@EmbeddedId
//	private  PersistanceDataCompositeKey persistanceDataCompositeKey;
	@Id
	private long transactionID;
	@Column
	private String transactionType;
	@Column
	private String transactionSubType;
	@Column
	private String payloadFormat;
	@Column (length = 100000)
	private String payload;
	@Column
	private String createdOn;
	@Column
	private String updatedOn;
	
}
