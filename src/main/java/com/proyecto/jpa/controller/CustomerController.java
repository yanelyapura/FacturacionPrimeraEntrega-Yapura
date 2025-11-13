package com.proyecto.jpa.controller;

import com.proyecto.jpa.entity.Customer;
import com.proyecto.jpa.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión de clientes (Capa de Presentación).
 * 
 * <p>Expone endpoints HTTP para realizar operaciones CRUD sobre clientes
 * y gestionar su estado (activo, inactivo, suspendido).</p>
 * 
 * <h2>Arquitectura de 3 Capas</h2>
 * <pre>
 * CustomerController (Capa de Presentación) ← Esta clase
 *         ↓
 * CustomerService (Capa de Negocio)
 *         ↓
 * CustomerRepository (Capa de Datos)
 * </pre>
 * 
 * @see Customer
 * @see CustomerService
 * @author Proyecto JPA
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Obtiene todos los clientes.
     * 
     * @return lista de clientes con código 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    /**
     * Obtiene un cliente por su ID.
     * 
     * @param id el ID del cliente
     * @return el cliente con código 200 OK, o 404 Not Found si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return customerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca un cliente por su email.
     * 
     * @param email el email del cliente
     * @return el cliente con código 200 OK, o 404 Not Found si no existe
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        return customerService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene clientes activos.
     * 
     * @return lista de clientes con estado ACTIVE
     */
    @GetMapping("/active")
    public ResponseEntity<List<Customer>> getActiveCustomers() {
        List<Customer> customers = customerService.findActiveCustomers();
        return ResponseEntity.ok(customers);
    }

    /**
     * Obtiene clientes por estado.
     * 
     * @param status el estado del cliente (ACTIVE, INACTIVE, SUSPENDED)
     * @return lista de clientes con ese estado
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Customer>> getCustomersByStatus(@PathVariable Customer.CustomerStatus status) {
        List<Customer> customers = customerService.findByStatus(status);
        return ResponseEntity.ok(customers);
    }

    /**
     * Busca clientes por nombre o apellido.
     * 
     * @param searchTerm texto a buscar
     * @return lista de clientes que coinciden
     */
    @GetMapping("/search")
    public ResponseEntity<List<Customer>> searchCustomers(@RequestParam String searchTerm) {
        List<Customer> customers = customerService.searchByName(searchTerm);
        return ResponseEntity.ok(customers);
    }

    /**
     * Crea un nuevo cliente.
     * 
     * @param customer el cliente a crear
     * @return el cliente creado con código 201 Created
     */
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        try {
            Customer savedCustomer = customerService.save(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza un cliente existente.
     * 
     * @param id el ID del cliente a actualizar
     * @param customer los nuevos datos del cliente
     * @return el cliente actualizado con código 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody Customer customer) {
        
        if (!customerService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        customer.setId(id);
        try {
            Customer updatedCustomer = customerService.save(customer);
            return ResponseEntity.ok(updatedCustomer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza el estado de un cliente.
     * 
     * @param id el ID del cliente
     * @param status el nuevo estado
     * @return el cliente actualizado con código 200 OK
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Customer> updateStatus(
            @PathVariable Long id,
            @RequestParam Customer.CustomerStatus status) {
        try {
            Customer updatedCustomer = customerService.updateStatus(id, status);
            return ResponseEntity.ok(updatedCustomer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Suspende un cliente.
     * 
     * @param id el ID del cliente a suspender
     * @return el cliente suspendido con código 200 OK
     */
    @PostMapping("/{id}/suspend")
    public ResponseEntity<Customer> suspendCustomer(@PathVariable Long id) {
        try {
            Customer suspendedCustomer = customerService.suspendCustomer(id);
            return ResponseEntity.ok(suspendedCustomer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Activa un cliente.
     * 
     * @param id el ID del cliente a activar
     * @return el cliente activado con código 200 OK
     */
    @PostMapping("/{id}/activate")
    public ResponseEntity<Customer> activateCustomer(@PathVariable Long id) {
        try {
            Customer activatedCustomer = customerService.activateCustomer(id);
            return ResponseEntity.ok(activatedCustomer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina un cliente por su ID.
     * 
     * <p><b>Operación en Cascada:</b> Al eliminar el cliente, todos sus pedidos
     * asociados se eliminarán automáticamente.</p>
     * 
     * @param id el ID del cliente a eliminar
     * @return código 204 No Content si se eliminó
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cuenta el número total de clientes.
     * 
     * @return el número de clientes con código 200 OK
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustomers() {
        long count = customerService.count();
        return ResponseEntity.ok(count);
    }
}

