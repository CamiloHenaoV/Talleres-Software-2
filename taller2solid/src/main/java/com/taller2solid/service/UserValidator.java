package com.taller2solid.service;

import com.taller2solid.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Clase para validar usuarios
 * Aplicando SRP (Single Responsibility Principle):
 * - Responsabilidad única: Validar reglas de negocio de usuarios
 */
public class UserValidator {
    
    // Patrón para validar email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    // Requisitos de contraseña
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MAX_USERNAME_LENGTH = 20;
    
    /**
     * Valida un usuario completo
     * @param user Usuario a validar
     * @return Lista de errores (vacía si es válido)
     */
    public List<String> validate(User user) {
        List<String> errors = new ArrayList<>();
        
        if (user == null) {
            errors.add("El usuario no puede ser nulo");
            return errors;
        }
        
        // Validar username
        errors.addAll(validateUsername(user.getUsername()));
        
        // Validar password
        errors.addAll(validatePassword(user.getPassword()));
        
        // Validar email
        errors.addAll(validateEmail(user.getEmail()));
        
        // Validar role
        if (user.getRole() == null) {
            errors.add("El rol es obligatorio");
        }
        
        return errors;
    }
    
    /**
     * Valida solo el username
     */
    public List<String> validateUsername(String username) {
        List<String> errors = new ArrayList<>();
        
        if (username == null || username.trim().isEmpty()) {
            errors.add("El nombre de usuario es obligatorio");
            return errors;
        }
        
        String trimmedUsername = username.trim();
        
        if (trimmedUsername.length() < MIN_USERNAME_LENGTH) {
            errors.add("El nombre de usuario debe tener al menos " + MIN_USERNAME_LENGTH + " caracteres");
        }
        
        if (trimmedUsername.length() > MAX_USERNAME_LENGTH) {
            errors.add("El nombre de usuario no puede exceder " + MAX_USERNAME_LENGTH + " caracteres");
        }
        
        // Solo letras, números y guión bajo
        if (!trimmedUsername.matches("^[a-zA-Z0-9_]+$")) {
            errors.add("El nombre de usuario solo puede contener letras, números y guión bajo");
        }
        
        return errors;
    }
    
    /**
     * Valida solo la contraseña
     */
    public List<String> validatePassword(String password) {
        List<String> errors = new ArrayList<>();
        
        if (password == null || password.isEmpty()) {
            errors.add("La contraseña es obligatoria");
            return errors;
        }
        
        if (password.length() < MIN_PASSWORD_LENGTH) {
            errors.add("La contraseña debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres");
        }
        
        // Validar que tenga al menos un número
        if (!password.matches(".*\\d.*")) {
            errors.add("La contraseña debe contener al menos un número");
        }
        
        // Validar que tenga al menos una letra
        if (!password.matches(".*[a-zA-Z].*")) {
            errors.add("La contraseña debe contener al menos una letra");
        }
        
        return errors;
    }
    
    /**
     * Valida solo el email
     */
    public List<String> validateEmail(String email) {
        List<String> errors = new ArrayList<>();
        
        if (email == null || email.trim().isEmpty()) {
            errors.add("El correo electrónico es obligatorio");
            return errors;
        }
        
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            errors.add("El formato del correo electrónico no es válido");
        }
        
        return errors;
    }
    
    /**
     * Verifica si un usuario es válido
     * @param user Usuario a validar
     * @return true si es válido, false si tiene errores
     */
    public boolean isValid(User user) {
        return validate(user).isEmpty();
    }
}