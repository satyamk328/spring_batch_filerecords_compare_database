package com.wipro.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.wipro.listener.HealthListener;
import com.wipro.model.Employee;
import com.wipro.processor.EmployeeProcessor;
import com.wipro.writer.EmployeeWriter;

@Configuration
public class BatchConfiguration {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Bean
	FlatFileItemReader<Employee> reader() {
	    FlatFileItemReader<Employee> reader = new FlatFileItemReader<>();
	    reader.setResource(new FileSystemResource("D:\\temp\\employee.csv"));
	    reader.setLinesToSkip(1);  
	    reader.setLineMapper(new DefaultLineMapper<Employee>() {
	        {
	            setLineTokenizer(new DelimitedLineTokenizer() {
	                {
	                    setNames("id", "name", "email", "department");
	                }
	            });
	            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {
	                {
	                    setTargetType(Employee.class);
	                }
	            });
	        }
	    });

	    return reader;
	}

	@Bean
    ItemWriter<Employee> writer() {
        return new EmployeeWriter();
    }

	@Bean
	HealthListener listener() {
		return new HealthListener();
	}

	@Bean
	EmployeeProcessor processor() {
		return new EmployeeProcessor();
	}

	@Bean
	Step step1() {
		return stepBuilderFactory.get("step1").<Employee, Employee>chunk(2).reader(reader()).processor(processor())
				.writer(writer()).listener(listener()).build();
	}

	@Bean
	Job employeeJob(Step step1) {
		return jobBuilderFactory.get("employeeJob").start(step1).build();
	}
}
