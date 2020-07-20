package com.springboot2appconnecttoAngular.springboot2jpacrudangular.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot2appconnecttoAngular.springboot2jpacrudangular.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
