package com.saurabh.dp.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import java.nio.file.*;
import java.text.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.saurabh.dp.model.*;
import com.saurabh.dp.repository.DataRepository;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;

@Service
public class DataServiceImpl implements DataService {
    
	@Autowired
	private DataRepository dataRepo;
	
	@Autowired
	public DataServiceImpl(DataRepository dataRepo) {
		this.dataRepo = dataRepo;
	}
	
	/*  Directory to store the file */
	
	private final String UPLOAD_DIR = "/home/vishal/";
	
	/*   Creating a new record */
	@Override
	public DataDtls createData(DataDtls data) {
		
		return dataRepo.save(data);
	}
	
	/* Checking if email exist or not */
	  @Override
	  public boolean chkEmail(String email) {
		   
		  return dataRepo.existsByEmail(email);
	  }
	  
	  /* Deleting record using id */
		@Override
		public void deleteData(int id) {
		       dataRepo.deleteById(id);
		}
		
		/*  Updating the Data, using setter of lombok*/
		@Override
		@Transactional
		public void updateData(int id, DataDtls dataDtls) {
			DataDtls existData = dataRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Data not Found" + id));
			
			existData.setPatientName(dataDtls.getPatientName());
			existData.setGender(dataDtls.getGender());
			existData.setDateOfBirth(dataDtls.getDateOfBirth());
			existData.setContactNumber(dataDtls.getContactNumber());
			existData.setEmail(dataDtls.getEmail());
			existData.setSymptoms(dataDtls.getSymptoms());
			existData.setTemperature(dataDtls.getTemperature());
		//	existData.setDicomFile(dataDtls.getDicomFile());
		}
	
		@Override
		public DataDtls getDataById(int id) {
			return dataRepo.findById(id).orElse(null);
		}

		@Override
		public List<DataDtls> getAllData(){
			 return dataRepo.findAll();
		} 
		
		/*  Pagination Implementation */
		@Override
		public Page<DataDtls> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {

	        Sort sort = Sort.by(sortField);
	        sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? sort.ascending() : sort.descending();
			
			Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
			return this.dataRepo.findAll(pageable);
		}		

		/* For Uploading file */
	    @Override
	    public void uploadFile(int id, MultipartFile file, String uploadDate) {
	    	 if(!file.isEmpty()) {
	    		 try {
	    			 
	    			 DataDtls data = getDataById(id);
	    			 
	    			 if(data != null) {
	    				 
	    				 byte[] bytes = file.getBytes();
	    				 
	    				 Path filePath = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
	    				 Files.write(filePath, bytes);
	    				 
	    
	    			/*   Extracting details from  Dicom File */	 
	    				 DicomInputStream dis = new DicomInputStream(file.getInputStream());
	    				 
	    				Attributes res = dis.readDataset();
                       
                        
	    				
	    				data.setmName(res.getString(org.dcm4che3.data.Tag.PatientName));
	    				System.out.println(res.getString(org.dcm4che3.data.Tag.PatientName));
	    				
	    				data.setMage(res.getString(org.dcm4che3.data.Tag.PatientAge));
	    				System.out.println(res.getString(org.dcm4che3.data.Tag.PatientAge));
	    				
	    				data.setMid(res.getString(org.dcm4che3.data.Tag.PatientID));
	    				System.out.println(res.getString(org.dcm4che3.data.Tag.PatientID));
	    				
	    				data.setMgender(res.getString(org.dcm4che3.data.Tag.PatientSex));
	    				System.out.println(res.getString(org.dcm4che3.data.Tag.PatientSex));
	    				
	    				data.setMstudyDate(res.getString(org.dcm4che3.data.Tag.StudyDate));
	    				System.out.println(res.getString(org.dcm4che3.data.Tag.StudyDate));
	    					
	    				 
	    				// data.setFileData(file.getBytes());
	    				 data.setFileUploaded(true);
	    				 data.setFileName(file.getOriginalFilename());
	    				 data.setUploadDate(uploadDate);
	    				 data.setFilePath(filePath.toString());
	    				 
	    				 data.setFile(file);
	    				 /* ... */
	    				 
	    				 createData(data);
	    				     				 
	    			 }
	    		 }catch (IOException e) {
	    			 e.printStackTrace();
	    		 }
	    	 }
	    }
	    
	    
	    /* Getting Details By Name */
	    
	    @Override
	    public List<DataDtls> getDataByName(String patientName){
	    	return dataRepo.findByPatientName(patientName);
	    }

}
