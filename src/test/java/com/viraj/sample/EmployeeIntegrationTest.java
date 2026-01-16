package com.viraj.sample;

import com.viraj.sample.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployeeIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    // Debemos de trabajar con un estado limpio antes de cada prueba
    @BeforeEach
    public void setUp() {
        employeeRepository.deleteAll();
    }
    @Test
    public void pruebaCrearYRecuperarEmpleado() throws Exception {
        mockMvc.perform(post("/employee/save")
                // Definimos el tipo de contenido a JSON
                .contentType(MediaType.APPLICATION_JSON)
                // Definimos el contenido del empleado a crear
                .content("{\"employeeName\":\"Juan Suárez\",\"employeeDescription\":\"Senior Developer\"}"))
                // Definimos el resultado esperado
                .andExpect(status().isOk())
                // Validamos que el nombre sea correcto
                .andExpect(jsonPath("$.employeeName", is("Juan Suárez")))
                // Validamos que la descripción sea correcta
                .andExpect(jsonPath("$.employeeDescription", is("Senior Developer")));
        // Ahora validamos que el empleado creado se pueda recuperar
        mockMvc.perform(get("/employee/getall")
                // Define el tipo de contenido esperado
                .contentType(MediaType.APPLICATION_JSON))
                // Definimos el resultado esperado
                .andExpect(status().isOk())
                // Validamos que haya un solo empleado
                .andExpect(jsonPath("$", hasSize(1)))
                // Validamos que el nombre del empleado sea correcto
                .andExpect(jsonPath("$[0].employeeName", is("Juan Suárez")));
    }
    @Test
    public void pruebaActualizarEmpleado() throws Exception {
        // ASSERT: Crear empleado
        mockMvc.perform(post("/employee/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"employeeName\":\"Diego Barberan\",\"employeeDescription\":\"Developer\"}"))
                .andExpect(status().isOk());

        // ARRANGE & ACT: Recibir y actualizar empleado
        mockMvc.perform(get("/employee/getall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeId").exists());

        // ACT:Actualizar empleado
        mockMvc.perform(put("/employee/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"employeeId\":1,\"employeeName\":\"Diego Updated\",\"employeeDescription\":\"Junior Developer\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeName", is("Diego Updated")));
    }
}
