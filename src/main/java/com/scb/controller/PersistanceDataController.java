package com.scb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.scb.model.AuditLog;
import com.scb.model.PersistanceData;
import com.scb.model.ResponseMessage;
import com.scb.persistence.util.ReceiverConstants;
import com.scb.persistence.util.ServiceUtil;
import com.scb.service.MainService;
import com.scb.serviceImpl.InternalApiInvoker;

import lombok.extern.log4j.Log4j2;

@Component
@RestController
@Log4j2
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(ReceiverConstants.PERSISTENCE_URL)
public class PersistanceDataController {
	@Autowired
	private MainService mainservice;
	
	@Autowired
	private ServiceUtil commonMethods;
	
	@Autowired
	private InternalApiInvoker internalApiInvoker;
	
	@PostMapping("/addPayload")
	public ResponseEntity<ResponseMessage> saveTransaction(@RequestBody PersistanceData persistancedata,
			UriComponentsBuilder builder) {
		log.info("Received PersistanceData - TransactionID : " + persistancedata.getTransactionID() + ", TransactionType : " + persistancedata.getTransactionType() 
			+ ", TransactionSubType : " + persistancedata.getTransactionSubType() + ", payloadFormat : " + persistancedata.getPayloadFormat());
		AuditLog auditLog = commonMethods.getAuditLog(persistancedata, "INITIATED", "Request persistence initiated");
		ResponseEntity<AuditLog> responseAuditLog = internalApiInvoker.auditLogApiCall(auditLog);
		
		boolean flag = mainservice.saveTrancation(persistancedata);
		/*if (flag == false) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.CONFLICT);
		}*/
		//HttpHeaders headers = new HttpHeaders();
		//headers.setLocation(builder.path("/PersistanceData/{id}").buildAndExpand(persistancedata).toUri());
		if (!flag) {
			auditLog = commonMethods.getAuditLog(persistancedata, "FAILED", "Failed to store request data");
		} else {
			auditLog = commonMethods.getAuditLog(persistancedata, "COMPLETED", "Request data stored successfully");
		}
		
		responseAuditLog = internalApiInvoker.auditLogApiCall(auditLog);
		ResponseMessage rm = new ResponseMessage().builder().responseCode(201).responseMessage("Successfully created").build();
		
		return new ResponseEntity<ResponseMessage>(rm, HttpStatus.CREATED);
	}

	@GetMapping("/getAllTransaction")
	public ResponseEntity<List<PersistanceData>> getAllTransaction() {
		log.info(" Get All Transaction received: ");
		List<PersistanceData> list = mainservice.getAllTransations();
		log.info("Transaction Recieved " + list);
		return new ResponseEntity<List<PersistanceData>>(list, HttpStatus.OK);
	}

	@GetMapping("/getPayloadByTransId/{transactionId}")
	public ResponseEntity<PersistanceData> getPayloadByTransId(@PathVariable("transactionId") long transactionId) {
		log.info(" Get Transaction By ID received: " + transactionId);
		PersistanceData transactionById = mainservice.getPayloadByTransId(transactionId);
		log.info("Transaction Recieved With Id" + transactionId + " received: " + transactionById);
		return new ResponseEntity<PersistanceData>(transactionById,HttpStatus.OK);
	}

	@GetMapping("/getPayloadByTransString/{transactionType}")
	public ResponseEntity<List<PersistanceData>> getProcessFlowSequenceById(
			@PathVariable("transactionType") String transactionType) {
		log.info(" Get Transaction By ID received: " + transactionType);
		List<PersistanceData> transactionByType = mainservice.getPayloadByTransString(transactionType);
		log.info("Transaction Recieved With Type" + transactionType + " received: " + transactionByType);
		return new ResponseEntity<List<PersistanceData>>(transactionByType, HttpStatus.OK);
	}

}
