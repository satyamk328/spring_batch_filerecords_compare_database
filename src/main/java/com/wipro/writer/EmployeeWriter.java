package com.wipro.writer;

import com.wipro.model.Employee;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class EmployeeWriter implements ItemWriter<Employee> {

    private static final String HEADER = "id,name,email,department\n";

	@Override
    public void write(List<? extends Employee> items) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());
        String matchedFileName = "D:\\temp\\matched_employees_" + currentDate + ".csv";
        String unmatchedFileName = "D:\\temp\\unmatched_employees_" + currentDate + ".csv";
        boolean hasMatched = false;
        boolean hasUnmatched = false;
        for (Employee employee : items) {
            if (employee.isMatched()) {
                hasMatched = true;
            } else {
                hasUnmatched = true;
            }
        }

        try {
            if (hasMatched) {
                try (FileWriter matchedFile = new FileWriter(matchedFileName, true)) {
                    if (new File(matchedFileName).length() == 0) {
                        matchedFile.write(HEADER);
                    }
                    for (Employee employee : items) {
                        if (employee.isMatched()) {
                            String employeeData = employee.getId() + "," + employee.getName() + "," + employee.getEmail() + "," + employee.getDepartment();
                            matchedFile.write(employeeData + "\n");
                        }
                    }
                }
            }

            if (hasUnmatched) {
                try (FileWriter unmatchedFile = new FileWriter(unmatchedFileName, true)) {
                    if (new File(unmatchedFileName).length() == 0) {
                        unmatchedFile.write(HEADER);
                    }
                    for (Employee employee : items) {
                        if (!employee.isMatched()) {
                            String employeeData = employee.getId() + "," + employee.getName() + "," + employee.getEmail() + "," + employee.getDepartment();
                            unmatchedFile.write(employeeData + "\n");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

