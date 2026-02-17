package com.taller2solid.domain;

/**
 * Enumeración de roles del sistema
 */
public enum Role {
    ADMIN("Administrador"),
    USER("Usuario"),
    GUEST("Invitado");
    
    private final String description;
    
    Role(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}