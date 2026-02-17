package com.taller2solid.access;

import com.taller2solid.domain.User;
import java.util.List;
import java.util.Optional;

/**
 * Interface del repositorio de usuarios
 * Aplicando DIP (Dependency Inversion Principle):
 * - Los módulos de alto nivel (Service) no dependen de módulos de bajo nivel (implementación)
 * - Ambos dependen de abstracciones (esta interface)
 */
public interface IUserRepository {
    
    /**
     * Guarda un nuevo usuario en la base de datos
     */
    boolean save(User user);
    
    /**
     * Actualiza un usuario existente
     */
    boolean update(User user);
    
    /**
     * Elimina un usuario por su ID
     */
    boolean delete(Integer id);
    
    /**
     * Busca un usuario por su ID
     */
    Optional<User> findById(Integer id);
    
    /**
     * Busca un usuario por su username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Lista todos los usuarios
     */
    List<User> findAll();
    
    /**
     * Inicializa la conexión a la base de datos
     */
    void initDatabase();
    
    /**
     * Cierra la conexión a la base de datos
     */
    void closeConnection();
}