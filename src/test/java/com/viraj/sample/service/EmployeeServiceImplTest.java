package com.viraj.sample.service;

import com.viraj.sample.entity.Employee;
import com.viraj.sample.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee employee;
    // Debemos de trabajar con un estado limpio antes de cada prueba
    @BeforeEach
    public void setUp() {
        employee = new Employee("Juan Suárez", "Senior Developer");
        employee.setEmployeeId(1L);
    }
    @Test
    public void pruebaGuardarEmpleado() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        //Assert and Act
        Employee result = employeeService.saveEmployee(employee);
        assertNotNull(result);
        assertEquals("Juan Suárez", result.getEmployeeName());
        assertEquals("Senior Developer", result.getEmployeeDescription());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
}
