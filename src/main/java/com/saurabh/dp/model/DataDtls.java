package com.saurabh.dp.model;

import java.io.IOException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataDtls {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int id;
    
	public String patientName;
	
	public String gender;
	
	public String dateOfBirth;
	
	public String contactNumber;
	
	public String email;
	
	public String symptoms;
	
	public double temperature;
	
	 private boolean fileUploaded;
	 
	 private String uploadDate;
	 
	 private String fileName;
	 
	 private String filePath;
	
 	@Transient
	public MultipartFile file;
 	
 	
 	/* ===================== FOR METADATA ==========================*/
    
	private String mName;
	
	private String mid;
	
	private String mage;
	
	private String mgender;
	
	private String mstudyDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public boolean isFileUploaded() {
		return fileUploaded;
	}

	public void setFileUploaded(boolean fileUploaded) {
		this.fileUploaded = fileUploaded;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getMage() {
		return mage;
	}

	public void setMage(String mage) {
		this.mage = mage;
	}

	public String getMgender() {
		return mgender;
	}

	public void setMgender(String mgender) {
		this.mgender = mgender;
	}

	public String getMstudyDate() {
		return mstudyDate;
	}

	public void setMstudyDate(String mstudyDate) {
		this.mstudyDate = mstudyDate;
	}

	public DataDtls(int id, String patientName, String gender, String dateOfBirth, String contactNumber, String email,
			String symptoms, double temperature, boolean fileUploaded, String uploadDate, String fileName,
			String filePath, MultipartFile file, String mName, String mid, String mage, String mgender,
			String mstudyDate) {
		super();
		this.id = id;
		this.patientName = patientName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.contactNumber = contactNumber;
		this.email = email;
		this.symptoms = symptoms;
		this.temperature = temperature;
		this.fileUploaded = fileUploaded;
		this.uploadDate = uploadDate;
		this.fileName = fileName;
		this.filePath = filePath;
		this.file = file;
		this.mName = mName;
		this.mid = mid;
		this.mage = mage;
		this.mgender = mgender;
		this.mstudyDate = mstudyDate;
	}

	public DataDtls() {
		super();
		// TODO Auto-generated constructor stub
	}
	

 	  
}


