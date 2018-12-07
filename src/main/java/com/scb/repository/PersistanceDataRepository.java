package com.scb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.scb.model.PersistanceData;
import com.scb.model.compositekey.PersistanceDataCompositeKey;
@RepositoryRestResource
public interface PersistanceDataRepository extends JpaRepository<PersistanceData, PersistanceDataCompositeKey>  {
	@Query(value="SELECT * FROM persistantdata sd WHERE sd.transactionType = ?1",nativeQuery=true)
	List<PersistanceData> findByType(String transactionType);
	
	@Query(value="SELECT * FROM persistantdata sd WHERE sd.transactionID = ?1",nativeQuery=true)
	List<PersistanceData> findById(long transactionID);
}
