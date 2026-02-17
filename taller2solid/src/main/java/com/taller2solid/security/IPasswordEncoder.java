package com.taller2solid.security;
/**
 * Interface para encriptación de contraseñas
 * Aplicando ISP (Interface Segregation Principle):
 * - Interface pequeña con métodos específicos relacionados
 */
public interface IPasswordEncoder {
    
    /**
     * Encripta una contraseña en texto plano
     */
    String encode(String rawPassword);
    
    /**
     * Verifica si una contraseña coincide con el hash
     */
    boolean matches(String rawPassword, String encodedPassword);
}