package com.saurabh.dp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import com.saurabh.dp.model.DataDtls;
import org.springframework.core.io.Resource;

public interface DataService {
    
	public DataDtls createData(DataDtls data);
	
	public boolean chkEmail(String email);
	
	public void deleteData(int id);
	
	public void updateData(int id, DataDtls dataDtls);
	
	public DataDtls getDataById(int id);
	
	List<DataDtls> getAllData();
	
	Page<DataDtls> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	
	void uploadFile(int id, MultipartFile file, String uploadDate);
	
//	ResponseEntity<Resource> downloadFile(int id);
	
	List<DataDtls> getDataByName(String patientName);
	
}
