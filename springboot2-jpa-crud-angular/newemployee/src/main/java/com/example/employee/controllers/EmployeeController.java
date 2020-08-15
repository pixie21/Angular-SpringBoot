package com.example.employee.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.exceptions.ResourceNotFoundException;
import com.example.employee.models.Employee;
import com.example.employee.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Employee Management CRUD RESTful Services", value = "EmployeeController", description = "Controller for Employee Management Services")
@RestController

@RequestMapping("/api/employees")
@CrossOrigin(origins="http://localhost:4200",allowedHeaders="*")
public class EmployeeController {
                @Autowired
                protected EmployeeService EmployeeService;
                
                @ApiOperation(value = "Retrieve list of employees from postgres database")
                @GetMapping(produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                public List<Employee> getAll() {
                                return EmployeeService.getAll();
                }
                
                @ApiOperation(value = "Retrieve employee's information from postgres database by user id", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                @GetMapping("/{id}")
                @Produces("application/json")
                public Employee get(@PathVariable("id")int id ) {
                                Employee data = EmployeeService.get(id);
                                if(data==null) {
                                                throw new ResourceNotFoundException("Employee","id",id);
                                }
                                
                                return data;
                }
                
//                @GetMapping("/byName{name}")
//                public Employee get(@PathVariable("name") String name ) {
//                                Employee data = EmployeeService.getbyName(name);
//                                if(data==null) {
//                                                throw new ResourceNotFoundException("Employee","name",name);
//                                }
//                                
//                                return data;
//                } 
                
//            @GetMapping("/byCategory{category}")
//            public Employee get(@PathVariable("category") String category ) {
//                            Employee data = EmployeeService.getbyCategory(category);
//                            if(data==null) {
//                                            throw new ResourceNotFoundException("Employee","category",category);
//                            }
//                            
//                            return data;
//            } 
//            
//            @GetMapping("/byCategory{category}")
//            public Employee get(@PathVariable("category") String category ) {
//                            List<Employee> data = EmployeeService.getAll();
//                            List<Employee> filtered_data = null;
//
//                            if(data==null) {
//                                            throw new ResourceNotFoundException("Employee","category",category);
//                            } else {
//                                           int n=0;
//                                            while(data.isEmpty() == false) {
//                                                            if (category.contentEquals("Beverage")){
//                                                                            if(data.get(n).getCategory().contentEquals("Beverage")) {
//                                                                                                            filtered_data.add(data.get(n));
//                                                                            }
//                                                                                            n++;
//                                            }
//                                            }
//                                            
//                            }
                                
//                            return (Employee) filtered_data;
//            } 
                
        //        @PostMapping("")
//                public Employee create( @RequestParam("employee") String data, @RequestParam("file") MultipartFile file) { 
//                	System.out.println("data:"+data);
//                	System.out.println("Test");
//                	Gson g = new Gson();
//                	Employee employee = g.fromJson(data, Employee.class);
//                	System.out.println("-------------");
//                	System.out.println(employee.getFirstname());
////                	return new Employee();
//                	System.out.println(employee + "\n" + file.getOriginalFilename() + "\n" + file.getSize());
//                                return EmployeeService.create(employee,file);
//                              
//                }
                @ApiOperation(value = "Creates a new employee and adds to postgres database", response = Employee.class)
                @Consumes("application/json")
                @ApiImplicitParams({
                    @ApiImplicitParam(name = "firstname", value = "User's first name", required = true, dataType = "string", paramType = "query"),
                    @ApiImplicitParam(name = "lastname", value = "User's surname", required = false, dataType = "string", paramType = "query"),
                    @ApiImplicitParam(name = "email", value = "User's email address", required = true, dataType = "string", paramType = "query"),
                    @ApiImplicitParam(name = "photo_url", value = "base 64 uri", required = false, dataType = "string", paramType = "query")
                  })
                @PostMapping("")
                public Employee create(@Valid @RequestBody Employee data) { 
                    return EmployeeService.create(data);
                   }

                @ApiOperation(value = "Updates an employee's data with a particular ID in postgres database", response = Employee.class)
                @PutMapping("/{id}")
                public Employee update(@ApiParam(value = "name that need to be updated", required = true) @PathVariable("id") int id,
                	      @ApiParam(value = "Updated user object", required = true) @RequestBody Employee data)
    
                {
                                return EmployeeService.update(id, data);
                }
                
                @ApiOperation(value = "Deletes an employee's data with a particular ID in postgres database")
                @DeleteMapping("/{id}")
                public ResponseEntity<?>delete(@PathVariable("id")int id){
                                EmployeeService.delete(id);
                                if(EmployeeService.delete(id))
                                                return ResponseEntity.ok(true);
                                else
                                                return ResponseEntity.status(400).body(false);

                }
}




