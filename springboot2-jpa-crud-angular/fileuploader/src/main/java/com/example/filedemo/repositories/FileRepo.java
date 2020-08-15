package com.example.filedemo.repositories;

import java.util.List;
import java.util.Optional;

import org.hibernate.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.filedemo.payload.UploadFileResponse;

public interface FileRepo extends JpaRepository<UploadFileResponse, Integer>{
	

	    List<UploadFileResponse> findByempName(String username);

	
	
}
