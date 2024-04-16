package experttal.experttal.controller;
import experttal.experttal.Repository.EmployeeRepository;
import experttal.experttal.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrCreateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        try {
            if (updatedEmployee == null || updatedEmployee.getName() == null || updatedEmployee.getAge() == 0) {
                return ResponseEntity.badRequest().body("Invalid request body");
            }
            Optional<Employee> optionalEmployee = employeeRepository.findById(id);
            if (!optionalEmployee.isPresent()) {
                // Employee does not exist, create a new one with the provided ID
                Employee newEmployee = new Employee();
                newEmployee.setId(id);
                newEmployee.setName(updatedEmployee.getName());
                newEmployee.setAge(updatedEmployee.getAge());
                employeeRepository.save(newEmployee);
                return ResponseEntity.ok("New employee created with ID " + id);
            }else {
                // Employee exists, update the existing one
                Employee existingEmployee = optionalEmployee.get();
                existingEmployee.setName(updatedEmployee.getName());
                existingEmployee.setAge(updatedEmployee.getAge());
                employeeRepository.save(existingEmployee);
                return ResponseEntity.ok("Employee with ID " + id + " updated successfully");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}


