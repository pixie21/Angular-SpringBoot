package com.example.employee.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.employee.exceptions.FileStorageException;
import com.example.employee.models.Employee;
import com.example.employee.repositories.IEmployeeRepository;



@Component
public class EmployeeService {
    
    @Autowired
    protected IEmployeeRepository EmployeeRepo;
    
    public List<Employee> getAll(){
                    return EmployeeRepo.findAll();
    }
    
//    public Employee create(Employee Employee, MultipartFile file) {
//    	System.out.println("employee Service");
//    	
//    	 // Normalize file name
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//
//        try {
//            // Check if the file's name contains invalid characters
//            if(fileName.contains("..")) {
//                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
//            }
//
//            Employee dbFile = new Employee(Employee.getId(),Employee.getFirstname(), Employee.getLastname(), Employee.getEmail(), compressBytes(file.getBytes()));
//
//            return EmployeeRepo.save(dbFile);
//        } catch (IOException ex) {
//            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
//        }
//    }
    
    public Employee create(Employee employee) {
        return EmployeeRepo.save(employee);
}

    
    
    
    public Employee get(int id) {
                    Optional<Employee> result = EmployeeRepo.findById(id);
                    return result.isPresent() ? result.get() : null;
    }
    
        public Employee update(int id, Employee data) {
                        Optional<Employee> result = EmployeeRepo.findById(id);
                        if(result.isPresent()) {
                                        Employee original = result.get();
                                        original.setFirstname(data.getFirstname());
                                        original.setLastname(data.getLastname());
                                        original.setEmail(data.getEmail());
                                        original.setPhoto_url(data.getPhoto_url());
                                       
                                        
                                        return EmployeeRepo.save(original);
                        }
                        return null;
        }
                
                        
                public boolean delete(int id) {
                                Optional<Employee> result = EmployeeRepo.findById(id);
                                if(result.isPresent()) {
                                                Employee original = result.get();                                          
                                                EmployeeRepo.delete(original);
                                                return true;
                                }
                                return false;
                }
                
                
                // compress the image bytes before storing it in the database
                
                    public static byte[] compressBytes(byte[] data) {
                
                        Deflater deflater = new Deflater();
                
                        deflater.setInput(data);
                
                        deflater.finish();
                
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
                
                        byte[] buffer = new byte[1024];
                
                        while (!deflater.finished()) {
                
                            int count = deflater.deflate(buffer);
                
                            outputStream.write(buffer, 0, count);
                
                        }
                
                        try {
                
                            outputStream.close();
                
                        } catch (IOException e) {
                
                        }
                
                        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
                
                        return outputStream.toByteArray();
                
                    }
                
                    // uncompress the image bytes before returning it to the angular application
                
                    public static byte[] decompressBytes(byte[] data) {
                
                        Inflater inflater = new Inflater();
                
                        inflater.setInput(data);
                
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
                
                        byte[] buffer = new byte[1024];
                
                        try {
                
                            while (!inflater.finished()) {
                
                                int count = inflater.inflate(buffer);
                
                                outputStream.write(buffer, 0, count);
                
                            }
                
                            outputStream.close();
                
                        } catch (IOException ioe) {
                
                        } catch (DataFormatException e) {
                
                        }
                
                        return outputStream.toByteArray();
                
                    }
}





