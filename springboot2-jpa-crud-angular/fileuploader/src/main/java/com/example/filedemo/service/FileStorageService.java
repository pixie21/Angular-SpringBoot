package com.example.filedemo.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.List;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.example.filedemo.controller.CryptoUtils;
import com.example.filedemo.exception.CryptoException;
import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.exception.MyFileNotFoundException;
import com.example.filedemo.payload.UploadFileResponse;
import com.example.filedemo.property.FileStorageProperties;
import com.example.filedemo.repositories.FileRepo;


@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    public static final String CIPHER_ALGORITHM = "AES";
    public static final String KEY_ALGORITHM = "AES";
    public static final String PASSWORD_HASH_ALGORITHM = "SHA-256";
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    
    private Provider provider= new BouncyCastleProvider();
    @Autowired
    protected FileRepo fileRepo;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
               .normalize();
        System.out.println("printing here"+fileStorageLocation);

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
        
        
    }
    
    public UploadFileResponse create(UploadFileResponse filedata) {
        return fileRepo.save(filedata);
}

    public File storeFile(File file) throws CryptoException {
    	String key = "Mary has one cat";
        File inputFile = file;
        String newfileName = "";
        String fileName = file.getName();
        File encryptedFile = new File("C:/Users/dchambers/FileFolder" + fileName + ".encrypted");
        
         
        try {
            encryptedFile = CryptoUtils.encrypt(key, inputFile, encryptedFile);
            newfileName = encryptedFile.getName();
           
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            newfileName = "Didntwork";
            
        }
        Path targetLocation = this.fileStorageLocation.resolve(newfileName);
        InputStream initialStream = null;
		try {
			initialStream = new FileInputStream(encryptedFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			Files.copy(initialStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return encryptedFile;
    }
    
    public List<UploadFileResponse> getAll(){
        return fileRepo.findAll();
}
    
    public Resource loadFileAsResource(String fileName) {
    	//System.out.println(fileName);
    	
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            System.out.println("here"+filePath);
            Resource resource = new UrlResource(filePath.toUri());
            System.out.println(resource);
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
    
// 
//    public static void encrypt(String key, File inputFile, File outputFile)
//            throws CryptoException {
//        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
//    }
// 
//    public static void decrypt(String key, File inputFile, File outputFile)
//            throws CryptoException {
//        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
//    }
// 
//    private static void doCrypto(int cipherMode, String key, File inputFile,
//            File outputFile) throws CryptoException {
//        try {
//            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
//            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
//            cipher.init(cipherMode, secretKey);
//             
//            FileInputStream inputStream = new FileInputStream(inputFile);
//            byte[] inputBytes = new byte[(int) inputFile.length()];
//            inputStream.read(inputBytes);
//             
//            byte[] outputBytes = cipher.doFinal(inputBytes);
//             
//            FileOutputStream outputStream = new FileOutputStream(outputFile);
//            outputStream.write(outputBytes);
//             
//            inputStream.close();
//            outputStream.close();
//             
//        } catch (NoSuchPaddingException | NoSuchAlgorithmException
//                | InvalidKeyException | BadPaddingException
//                | IllegalBlockSizeException | IOException ex) {
//            throw new CryptoException("Error encrypting/decrypting file", ex);
//        }
//    }

	public List<UploadFileResponse> get(String username) {
		 List<UploadFileResponse> result = fileRepo.findByempName(username);
         return result;
}
	
	

}
