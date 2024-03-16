package com.saurabh.dp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saurabh.dp.model.DataDtls;

/* ========= TO COMMUNICATE WITH DATABASE =============== */

public interface DataRepository extends JpaRepository<DataDtls, Integer> {
     
	public boolean existsByEmail(String email);
	
	public DataDtls findByEmail(String email);
	
	List<DataDtls> findByPatientName(String patientName);
}
