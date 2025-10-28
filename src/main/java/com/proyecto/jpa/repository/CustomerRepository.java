package com.proyecto.jpa.repository;

import com.proyecto.jpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Customer.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Busca un cliente por su email.
     * 
     * @param email email del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Busca clientes por estado.
     * 
     * @param status estado del cliente
     * @return lista de clientes con ese estado
     */
    List<Customer> findByStatus(Customer.CustomerStatus status);

    /**
     * Busca clientes cuyo nombre o apellido contenga el texto dado.
     * 
     * @param firstName texto a buscar en el nombre
     * @param lastName texto a buscar en el apellido
     * @return lista de clientes que coinciden
     */
    List<Customer> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);

    /**
     * Verifica si existe un cliente con el email dado.
     * 
     * @param email email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
}

