package com.taller2solid.service;

import com.taller2solid.access.IUserRepository;
import com.taller2solid.domain.User;
import com.taller2solid.security.IPasswordEncoder;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de usuarios con lógica de negocio
 * Aplicando múltiples principios SOLID:
 * 
 * SRP: Responsabilidad única de gestionar lógica de negocio de usuarios
 * DIP: Depende de abstracciones (IUserRepository, IPasswordEncoder)
 * OCP: Abierto a extensión mediante inyección de dependencias
 */
public class UserService {
    
    private final IUserRepository repository;
    private final IPasswordEncoder passwordEncoder;
    private final UserValidator validator;
    
    /**
     * Constructor con inyección de dependencias
     * @param repository Repositorio de usuarios
     * @param passwordEncoder Encriptador de contraseñas
     */
    public UserService(IUserRepository repository, IPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.validator = new UserValidator();
    }
    
    /**
     * Crea un nuevo usuario
     * - Valida el usuario
     * - Verifica que el username no exista
     * - Encripta la contraseña
     * - Guarda en la base de datos
     * 
     * @param user Usuario a crear
     * @return Resultado de la operación
     */
    public ServiceResult createUser(User user) {
        // 1. Validar datos del usuario
        List<String> validationErrors = validator.validate(user);
        if (!validationErrors.isEmpty()) {
            return ServiceResult.failure(String.join(", ", validationErrors));
        }
        
        // 2. Verificar que el username no exista
        Optional<User> existingUser = repository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return ServiceResult.failure("El nombre de usuario ya está en uso");
        }
        
        // 3. Encriptar contraseña
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        // 4. Establecer como activo por defecto
        user.setActive(true);
        
        // 5. Guardar en base de datos
        boolean saved = repository.save(user);
        
        if (saved) {
            return ServiceResult.success("Usuario creado exitosamente", user);
        } else {
            return ServiceResult.failure("Error al guardar el usuario en la base de datos");
        }
    }
    
    /**
     * Actualiza un usuario existente
     * - Valida el usuario
     * - Verifica que exista
     * - Si la contraseña cambió, la encripta
     * - Actualiza en la base de datos
     * 
     * @param user Usuario a actualizar
     * @return Resultado de la operación
     */
    public ServiceResult updateUser(User user) {
        // 1. Verificar que el usuario tenga ID
        if (user.getId() == null) {
            return ServiceResult.failure("El ID del usuario es requerido para actualizar");
        }
        
        // 2. Validar datos del usuario
        List<String> validationErrors = validator.validate(user);
        if (!validationErrors.isEmpty()) {
            return ServiceResult.failure(String.join(", ", validationErrors));
        }
        
        // 3. Verificar que el usuario exista
        Optional<User> existingUser = repository.findById(user.getId());
        if (!existingUser.isPresent()) {
            return ServiceResult.failure("El usuario no existe");
        }
        
        // 4. Verificar si el username cambió y si está disponible
        User existing = existingUser.get();
        if (!existing.getUsername().equals(user.getUsername())) {
            Optional<User> userWithSameUsername = repository.findByUsername(user.getUsername());
            if (userWithSameUsername.isPresent()) {
                return ServiceResult.failure("El nombre de usuario ya está en uso");
            }
        }
        
        // 5. Si la contraseña cambió, encriptarla
        // (Verificamos si es diferente del hash almacenado)
        if (!user.getPassword().equals(existing.getPassword())) {
            // Si la contraseña no parece estar encriptada (muy corta), encriptarla
            if (user.getPassword().length() < 40) { // SHA-256 produce 64 caracteres hex
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
            }
        }
        
        // 6. Actualizar en base de datos
        boolean updated = repository.update(user);
        
        if (updated) {
            return ServiceResult.success("Usuario actualizado exitosamente", user);
        } else {
            return ServiceResult.failure("Error al actualizar el usuario");
        }
    }
    
    /**
     * Elimina un usuario por su ID
     * 
     * @param id ID del usuario a eliminar
     * @return Resultado de la operación
     */
    public ServiceResult deleteUser(Integer id) {
        if (id == null) {
            return ServiceResult.failure("El ID es requerido");
        }
        
        // Verificar que el usuario exista
        Optional<User> user = repository.findById(id);
        if (!user.isPresent()) {
            return ServiceResult.failure("El usuario no existe");
        }
        
        // Eliminar
        boolean deleted = repository.delete(id);
        
        if (deleted) {
            return ServiceResult.success("Usuario eliminado exitosamente");
        } else {
            return ServiceResult.failure("Error al eliminar el usuario");
        }
    }
    
    /**
     * Busca un usuario por su ID
     * 
     * @param id ID del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<User> findUserById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return repository.findById(id);
    }
    
    /**
     * Busca un usuario por su username
     * 
     * @param username Nombre de usuario
     * @return Optional con el usuario si existe
     */
    public Optional<User> findUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return Optional.empty();
        }
        return repository.findByUsername(username);
    }
    
    /**
     * Lista todos los usuarios del sistema
     * 
     * @return Lista de usuarios
     */
    public List<User> findAllUsers() {
        return repository.findAll();
    }
    
    /**
     * Autentica un usuario
     * - Verifica que el usuario exista
     * - Verifica que esté activo
     * - Verifica que la contraseña sea correcta
     * 
     * @param username Nombre de usuario
     * @param password Contraseña en texto plano
     * @return Resultado con el usuario autenticado si es exitoso
     */
    public ServiceResult authenticate(String username, String password) {
        // 1. Validar parámetros
        if (username == null || username.trim().isEmpty()) {
            return ServiceResult.failure("El nombre de usuario es requerido");
        }
        
        if (password == null || password.isEmpty()) {
            return ServiceResult.failure("La contraseña es requerida");
        }
        
        // 2. Buscar usuario
        Optional<User> userOptional = repository.findByUsername(username);
        if (!userOptional.isPresent()) {
            return ServiceResult.failure("Usuario o contraseña incorrectos");
        }
        
        User user = userOptional.get();
        
        // 3. Verificar que esté activo
        if (!user.isActive()) {
            return ServiceResult.failure("La cuenta está desactivada");
        }
        
        // 4. Verificar contraseña
        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        if (!passwordMatches) {
            return ServiceResult.failure("Usuario o contraseña incorrectos");
        }
        
        // 5. Autenticación exitosa
        return ServiceResult.success("Autenticación exitosa", user);
    }
    
    // ============================================
    // Clase interna ServiceResult
    // ============================================
    
    /**
     * Clase para encapsular resultados de operaciones del servicio
     * Permite devolver éxito/fallo con mensaje y datos opcionales
     */
    public static class ServiceResult {
        private final boolean success;
        private final String message;
        private final User user;
        
        private ServiceResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }
        
        /**
         * Crea un resultado exitoso sin usuario
         */
        public static ServiceResult success(String message) {
            return new ServiceResult(true, message, null);
        }
        
        /**
         * Crea un resultado exitoso con usuario
         */
        public static ServiceResult success(String message, User user) {
            return new ServiceResult(true, message, user);
        }
        
        /**
         * Crea un resultado fallido
         */
        public static ServiceResult failure(String message) {
            return new ServiceResult(false, message, null);
        }
        
        // Getters
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public User getUser() {
            return user;
        }
        
        @Override
        public String toString() {
            return "ServiceResult{" +
                    "success=" + success +
                    ", message='" + message + '\'' +
                    ", user=" + (user != null ? user.getUsername() : "null") +
                    '}';
        }
    }
}