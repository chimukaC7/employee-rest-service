package com.bytecodevelocity.service;

import com.bytecodevelocity.exception_handler.EmployeeNotFound;
import com.bytecodevelocity.model.Employee;
import com.bytecodevelocity.model.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
//this controller will handle all the REST API methods
@RestController
public class EmployeeController {

    @Autowired
    EmployeeDao service;

    //@RequestMapping(method = RequestMethod.GET,path = "/welcome")
    @GetMapping("/employees")
    public List<Employee> getAll() {
        return service.getAllEmployees();
    }

    @GetMapping("/employees/{empId}")
//    public Employee getEmployeeById(@PathVariable int empId){
//        Employee employee = service.getEmployeeById(empId);
//        if (employee == null)
//            throw new EmployeeNotFound("Employee Not Found.");
//        return employee;
//    }
    public EntityModel<Employee> getEmployeeById(@PathVariable int empId){
        Employee employee = service.getEmployeeById(empId);

        if(employee == null)
            throw new EmployeeNotFound("Employee Not Found.");

        //implementing HATEOS
        EntityModel<Employee> employeeEntityModel = EntityModel.of(employee);

        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAll()).withRel("all-employees");
        employeeEntityModel.add(link);

        return employeeEntityModel;
    }

    @PostMapping("/employees")
//    public void saveEmployee(@RequestBody Employee emp){
//        service.saveEmployee(emp);
//    }
    //@Valid ensure that the request is valid(containing the right parameters if applicable)
    public ResponseEntity<Object> saveEmployee(@Valid @RequestBody Employee emp){//enabled the validation
        Employee newEmployee = service.saveEmployee(emp);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()//returning the current request
                .path("{employeeId}")
                .buildAndExpand(newEmployee.getId())
                .toUri();

        return ResponseEntity.created(uri).build();

    }

    @DeleteMapping("/employees/{empId}")
    public void deleteEmployee(@PathVariable int empId){
        Employee emp = service.deleteEmployee(empId);

        if(emp == null)
            throw new EmployeeNotFound("Employee Not Found .");
    }

}
