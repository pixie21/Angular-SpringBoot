package com.example.employee.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee.models.Employee;

public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {

}
