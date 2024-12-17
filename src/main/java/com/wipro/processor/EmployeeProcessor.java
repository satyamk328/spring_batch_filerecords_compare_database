package com.wipro.processor;


import java.util.Optional;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wipro.model.Employee;
import com.wipro.repository.EmployeeRepository;

@Component
public class EmployeeProcessor implements ItemProcessor<Employee, Employee>{

    @Autowired
    private EmployeeRepository repository;

    @Override
    public Employee process(Employee employee) throws Exception {
        Optional<Employee> dbRecord = repository.findByEmail(employee.getEmail());
        if (dbRecord.isPresent()) {
            employee.setMatched(true); 
        } else {
            employee.setMatched(false); 
        }
        return employee;
    }
}
