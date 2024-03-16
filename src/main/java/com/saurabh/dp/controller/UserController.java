package com.saurabh.dp.controller;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.data.Tag;
import com.saurabh.dp.model.DataDtls;
import com.saurabh.dp.service.DataService;
import com.saurabh.dp.service.UserService;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Handling the user-related actions for /user   url
@Controller
@RequestMapping("/user") // mapping url starting with /user to controller
public class UserController {
    
	@Autowired
	private UserService userService;
	
	@Autowired
	private DataService dataService;
	
 
	
	@GetMapping("/patient")
	public String patient() {
		
		return  "patient";
	}
	
	/* .... Patient Details Creation .... */
	@PostMapping("/createData")
	public String createdata(@ModelAttribute DataDtls data, HttpSession session) throws IOException{
		
	  /* ======== GETTING THE CURRENT DATE ============= */
		LocalDate currentDate = LocalDate.now();
		
		LocalDate dateOfBirth = LocalDate.parse(data.getDateOfBirth());
		
		if(dateOfBirth.isAfter(currentDate)) {
            session.setAttribute("msg", "Invalid Date of Birth. Please choose a valid date.");
            return "redirect:/user/patient";
		}
		
		boolean f = dataService.chkEmail(data.getEmail());		
		
		if(f) {
			session.setAttribute("msg", "Email id already exists");
		}
		else {
			
	        if (data.getContactNumber().length() != 10) {
	            session.setAttribute("msg", "Phone number must have 10 digits.");
	            return "redirect:/user/patient";
	        }

	        // Validate temperature range (assuming a reasonable range)
	        double temperature = data.getTemperature();
	        if (temperature < 95.0 || temperature > 105.0) {
	            session.setAttribute("msg", "Invalid temperature. Please enter a temperature within the range of 95.0°F to 105.0°F.");
	            return "redirect:/user/patient";
	        }
			
			DataDtls dataDtls = dataService.createData(data);
			
			if(dataDtls != null) {
				//session.setAttribute("msg", "Details Stored Successfully");
			}
			else {
				session.setAttribute("msg", "Something wrong on server");
			}
		}
		
		return "redirect:/user/";
	}

	
	/*  Deleting Data */
	@GetMapping("/delete/{id}")
	public String deleteData(@PathVariable("id") int id) {
		dataService.deleteData(id);
		
		return "redirect:/user/";
	}
	
	/* Updating Data */
	@GetMapping("/update/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
		 DataDtls data = dataService.getDataById(id);
		 model.addAttribute("data", data);
		 
		 return "update";
	}
	
	/*  ==================== FOR UPDATION ====================== */
	@PostMapping("/update/{id}")
	public String saveUpdatedData(@PathVariable int id, @ModelAttribute DataDtls data, @RequestParam(name = "action", required = false) String action, HttpSession session) {
		
		
		 
		if("save".equals(action)) {
			
			LocalDate currentDate = LocalDate.now();
			
			LocalDate dateOfBirth = LocalDate.parse(data.getDateOfBirth());
			
			if(dateOfBirth.isAfter(currentDate)) {
	            session.setAttribute("msg", "Invalid Date of Birth. Please choose a valid date.");
	            return "redirect:/user/update/{id}";
			}
			
	        if (data.getContactNumber().length() != 10) {
	            session.setAttribute("msg", "Phone number must have 10 digits.");
	            return "redirect:/user/update/{id}";
	        }

	        // Validating temperature range (assuming a reasonable range)
	        double temperature = data.getTemperature();
	        if (temperature < 95.0 || temperature > 105.0) {
	            session.setAttribute("msg", "Invalid temperature. Please enter a temperature within the range of 95.0°F to 105.0°F.");
	            return "redirect:/user/update/{id}";
	        }
			
			
			dataService.updateData(id, data);
		}
		return "redirect:/user/";
	}
	
	@GetMapping("/")
	public String viewPatients(Model model) {
		return findPaginated(1, "patientName", "asc", model);
	}	
	
	/* ===================== VIEWING THE PATIENT'S LIST WITH PROPER PAGINATION AND SORTING =============================== */
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		//int pageSize = 10;
		 
		 int pageSize = 5;
		
		Page<DataDtls> page = dataService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<DataDtls> listData = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");	
		
		model.addAttribute("listData", listData);
		
		return "view";
	}
	
	/* ================== FOR UPLOADING THE FILE ================================= */
	@PostMapping("/uploadFile/{id}")
	public String uploadFile(@PathVariable int id, @RequestParam("file") MultipartFile file, @RequestParam("uploadDate") String uploadDate, RedirectAttributes redirectAttributes) {
		
        if (!file.getOriginalFilename().toLowerCase().endsWith(".dcm")) {
            redirectAttributes.addFlashAttribute("error", "Only .dcm files are allowed.");
            return "redirect:/user/";
        }
	
		dataService.uploadFile(id, file, uploadDate);
		
		return "redirect:/user/";
	}
	
	/* ================= FOR DOWNLOADING THE FILE =========================
	@GetMapping("/downloadFile/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable int id){
		return dataService.downloadFile(id);
	}
*/
	@GetMapping("/fupload/{id}")
	public String uploadDownloadFile(@PathVariable int id ,Model model) {
		DataDtls data = dataService.getDataById(id);
		
   	model.addAttribute("data", data);
		
		return "fupload";
	} 	  
	

/*  =============== ONLY FOR TESTING AND SELF PRACTICE FOR RE-ROUTING TO ANOTHER APP ======================= */
	
/*	@GetMapping("/react")
	public void handleGet(HttpServletResponse response) {
	    response.setHeader("Location", "http://localhost:3000/");
	    response.setStatus(302);
	}
	
	
	
/*  ================ FOR VIEWING THE DICOM IMAGE ============================== */
	
	@GetMapping("/test/{id}")
	public ResponseEntity<byte[]> getView(@PathVariable int id){
		try {
		DataDtls data = dataService.getDataById(id);
		
		String inputDicomFilePath = data.getFilePath();
		
	    File file = new File(inputDicomFilePath);
	    DicomInputStream dis = new DicomInputStream(file);
	    Attributes attributes = dis.readDataset();
	    
	    /* ============== FETCHING THE IMAGE FROM FILE PATH ========================== */
    	ImageInputStream img = new FileImageInputStream(new File(inputDicomFilePath));
        
        ImageReader reader = ImageIO.getImageReadersByFormatName("DICOM").next();

        reader.setInput(img);

        BufferedImage image = reader.read(0, new ImageReadParam());
        
        /* Used to convert in Byte array */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        

       /* =========== CONVERTING IMAGE TO JPEG ============= */
        
        ImageIO.write(image, "JPEG", baos);
        
        byte[] imageBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
       
        
        headers.setContentType(MediaType.IMAGE_JPEG);

        img.close();
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
	}
	catch (Exception e) {
		e.printStackTrace();
		 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
		
} 
	
/*  ============== ONLY FOR TESTING FOR CORNERSTONE DICOM VIEWER ================== */
	@GetMapping("/tst")
	public String test() {
		return "dicom";
	}
	  

}
