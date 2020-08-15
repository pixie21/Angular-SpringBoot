package com.example.filedemo.controller;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.filedemo.exception.CryptoException;
import com.example.filedemo.exception.MyFileNotFoundException;
import com.example.filedemo.payload.UploadFileResponse;
import com.example.filedemo.service.FileStorageService;



@CrossOrigin(origins="*",allowedHeaders="*")
@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;
    
    @GetMapping("/uploadedFiles")
    public List<UploadFileResponse> getAll(){
        return fileStorageService.getAll();
}
    
    @GetMapping("/uploadedFiles/{empName}")
    public List<UploadFileResponse> get(@PathVariable("empName")String empname ) {
                    List<UploadFileResponse> data = fileStorageService.get(empname);
                    if(data==null) {
                                    throw new MyFileNotFoundException("File for the folowing username not found:");
                    }
                    
                    return data;
    }
    

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("empID") String empid, @RequestParam("file") MultipartFile file) throws IOException, InvalidKeyException, CryptoException{
    	File newFile = FileController.convert(file);
        File encryptedfile = (fileStorageService.storeFile(newFile));
        String fileName = encryptedfile.getName();

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        UploadFileResponse newrecord = new UploadFileResponse(empid, fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
        return fileStorageService.create(newrecord);
    }
    
    
//    @PostMapping("/uploadtoNexus")
//    public void sendtoNexus(@RequestParam("raw.assetN") MultipartFile file,
//    						@RequestParam("raw.directory") String directory,
//    						@RequestParam("raw.assetN.filename") String filename) {
//    	String fileName = (fileStorageService.storeFile(file));
//    	String username = "mgsp-s0-app";
//    	String password = "Aluminum:-)Paternal:-)Virus:-)Flier";
//    	String url = "https://bizdevnex.egovja.com/service/rest/v1/components?repository=m"
//    			+ "gsp-ff3c7289-06fb-478d-b88b-6c1121eb9494";
//    	 
//    	HttpHeaders headers = new HttpHeaders();
//    	headers.setBasicAuth(username, password);
////    	
////    	HttpEntity<?> request = new HttpEntity<>());
////    	ResponseEntity<?> response = new RestTemplate().postForEntity(url, request, String.class);
//    	
//    }
    
//    @PostMapping("/uploadMultipleFiles")
//    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//        return Arrays.asList(files)
//                .stream()
//                .map(file -> uploadFile(file))
//                .collect(Collectors.toList());
//    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException, CryptoException {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        File encryptedFile = resource.getFile();
        
        String tempWord = ".encrypted" ; 
        fileName = fileName.replaceAll(tempWord, ""); 
        System.out.println(fileName);
        String key = "Mary has one cat";
   	
        File decryptedFileName = new File(fileName);
        File decryptedFile = CryptoUtils.decrypt(key, encryptedFile, decryptedFileName);
        System.out.println("My DECRYPTEDFILE" + decryptedFile);
       
        Resource newresource = fileStorageService.loadFileAsResource(decryptedFile.getAbsolutePath());
        
        // Try to determine file's content type
        String contentType = null;
        contentType = request.getServletContext().getMimeType(newresource.getFile().getAbsolutePath());

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + newresource.getFilename() + "\"")
                .body(newresource);
    }
    
    public static File convert(MultipartFile file) throws IOException {
    	File convFile = new File(file.getOriginalFilename());
    	convFile.createNewFile();
    	FileOutputStream fos = new FileOutputStream(convFile);
    	fos.write(file.getBytes());
    	fos.close();
    	return convFile;
    }
    
    
    

}
