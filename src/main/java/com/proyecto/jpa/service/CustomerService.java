package com.proyecto.jpa.service;

import com.proyecto.jpa.entity.Customer;
import com.proyecto.jpa.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de clientes (Capa de Negocio).
 * 
 * <p>Implementa la lógica de negocio relacionada con los clientes,
 * incluyendo validaciones de datos y gestión del estado del cliente.</p>
 * 
 * <h2>Arquitectura de 3 Capas</h2>
 * <pre>
 * CustomerController (Capa de Presentación)
 *         ↓
 * CustomerService (Capa de Negocio) ← Esta clase
 *         ↓
 * CustomerRepository (Capa de Datos)
 * </pre>
 * 
 * <h2>Operaciones en Cascada</h2>
 * <p>Al eliminar un cliente, todos sus pedidos asociados se eliminan
 * automáticamente gracias a la configuración de cascada en la entidad Customer.</p>
 * 
 * @see Customer
 * @see CustomerRepository
 * @author Proyecto JPA
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param customerRepository repositorio de clientes
     */
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Obtiene todos los clientes del sistema.
     * 
     * @return lista de todos los clientes
     */
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    /**
     * Busca un cliente por su ID.
     * 
     * @param id el ID del cliente
     * @return un Optional con el cliente si existe
     */
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    /**
     * Busca un cliente por su email.
     * 
     * @param email el email del cliente
     * @return un Optional con el cliente si existe
     */
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    /**
     * Busca clientes por estado.
     * 
     * @param status el estado del cliente
     * @return lista de clientes con ese estado
     */
    public List<Customer> findByStatus(Customer.CustomerStatus status) {
        return customerRepository.findByStatus(status);
    }

    /**
     * Busca clientes activos.
     * 
     * @return lista de clientes con estado ACTIVE
     */
    public List<Customer> findActiveCustomers() {
        return customerRepository.findByStatus(Customer.CustomerStatus.ACTIVE);
    }

    /**
     * Busca clientes por nombre o apellido.
     * 
     * @param searchTerm texto a buscar
     * @return lista de clientes que coinciden
     */
    public List<Customer> searchByName(String searchTerm) {
        return customerRepository.findByFirstNameContainingOrLastNameContaining(searchTerm, searchTerm);
    }

    /**
     * Verifica si existe un cliente con el email dado.
     * 
     * @param email el email a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    /**
     * Crea o actualiza un cliente.
     * 
     * <p>Valida que el cliente tenga datos obligatorios y email único.</p>
     * 
     * @param customer el cliente a guardar
     * @return el cliente guardado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    @Transactional
    public Customer save(Customer customer) {
        validateCustomer(customer);
        return customerRepository.save(customer);
    }

    /**
     * Actualiza el estado de un cliente.
     * 
     * @param customerId el ID del cliente
     * @param newStatus el nuevo estado
     * @return el cliente actualizado
     * @throws IllegalArgumentException si el cliente no existe
     */
    @Transactional
    public Customer updateStatus(Long customerId, Customer.CustomerStatus newStatus) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + customerId));
        
        customer.setStatus(newStatus);
        return customerRepository.save(customer);
    }

    /**
     * Suspende un cliente (cambia su estado a SUSPENDED).
     * 
     * @param customerId el ID del cliente a suspender
     * @return el cliente suspendido
     */
    @Transactional
    public Customer suspendCustomer(Long customerId) {
        return updateStatus(customerId, Customer.CustomerStatus.SUSPENDED);
    }

    /**
     * Activa un cliente (cambia su estado a ACTIVE).
     * 
     * @param customerId el ID del cliente a activar
     * @return el cliente activado
     */
    @Transactional
    public Customer activateCustomer(Long customerId) {
        return updateStatus(customerId, Customer.CustomerStatus.ACTIVE);
    }

    /**
     * Elimina un cliente por su ID.
     * 
     * <p><b>Operación en Cascada:</b> Al eliminar el cliente, todos sus pedidos
     * asociados se eliminarán automáticamente.</p>
     * 
     * @param id el ID del cliente a eliminar
     * @throws IllegalArgumentException si no existe un cliente con ese ID
     */
    @Transactional
    public void deleteById(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un cliente con ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    /**
     * Cuenta el número total de clientes.
     * 
     * @return el número total de clientes
     */
    public long count() {
        return customerRepository.count();
    }

    /**
     * Valida que un cliente tenga datos válidos.
     * 
     * @param customer el cliente a validar
     * @throws IllegalArgumentException si los datos son inválidos
     */
    private void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("El cliente no puede ser null");
        }
        if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (customer.getLastName() == null || customer.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        
        // Validar email único (solo para nuevos clientes o si cambió el email)
        if (customer.getId() == null) {
            if (existsByEmail(customer.getEmail())) {
                throw new IllegalArgumentException("Ya existe un cliente con el email: " + customer.getEmail());
            }
        }
    }
}

