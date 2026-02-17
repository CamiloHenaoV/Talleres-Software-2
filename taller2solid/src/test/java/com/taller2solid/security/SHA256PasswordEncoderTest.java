package com.taller2solid.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para SHA256PasswordEncoder
 * Valida la encriptación y comparación de contraseñas
 */
@DisplayName("Pruebas unitarias de SHA256PasswordEncoder")
class SHA256PasswordEncoderTest {
    
    private SHA256PasswordEncoder encoder;
    
    @BeforeEach
    void setUp() {
        encoder = new SHA256PasswordEncoder();
    }
    
    @Test
    @DisplayName("Debe encriptar una contraseña y generar un hash válido")
    void testEncodeGeneraHashValido() {
        // Arrange
        String passwordPlano = "miPassword123";
        
        // Act
        String hash = encoder.encode(passwordPlano);
        
        // Assert
        assertNotNull(hash, "El hash no debe ser nulo");
        assertFalse(hash.isEmpty(), "El hash no debe estar vacío");
        assertEquals(64, hash.length(), "SHA-256 debe generar un hash de 64 caracteres hexadecimales");
        assertNotEquals(passwordPlano, hash, "El hash no debe ser igual a la contraseña en texto plano");
    }
    
    @Test
    @DisplayName("Debe generar el mismo hash para la misma contraseña")
    void testEncodeEsDeterministico() {
        // Arrange
        String password = "password123";
        
        // Act
        String hash1 = encoder.encode(password);
        String hash2 = encoder.encode(password);
        
        // Assert
        assertEquals(hash1, hash2, "Encriptar la misma contraseña debe producir el mismo hash");
    }
    
    @Test
    @DisplayName("Debe generar hashes diferentes para contraseñas diferentes")
    void testHashesDiferentesParaPasswordsDiferentes() {
        // Arrange
        String password1 = "password123";
        String password2 = "password456";
        
        // Act
        String hash1 = encoder.encode(password1);
        String hash2 = encoder.encode(password2);
        
        // Assert
        assertNotEquals(hash1, hash2, "Contraseñas diferentes deben producir hashes diferentes");
    }
    
    @Test
    @DisplayName("Debe validar correctamente cuando la contraseña coincide")
    void testMatchesConPasswordCorrecta() {
        // Arrange
        String passwordPlano = "miPasswordSegura";
        String hash = encoder.encode(passwordPlano);
        
        // Act
        boolean resultado = encoder.matches(passwordPlano, hash);
        
        // Assert
        assertTrue(resultado, "matches() debe retornar true cuando la contraseña es correcta");
    }
    
    @Test
    @DisplayName("Debe rechazar cuando la contraseña no coincide")
    void testMatchesConPasswordIncorrecta() {
        // Arrange
        String passwordCorrecta = "passwordCorrecta";
        String passwordIncorrecta = "passwordIncorrecta";
        String hash = encoder.encode(passwordCorrecta);
        
        // Act
        boolean resultado = encoder.matches(passwordIncorrecta, hash);
        
        // Assert
        assertFalse(resultado, "matches() debe retornar false cuando la contraseña es incorrecta");
    }
    
    @Test
    @DisplayName("Debe manejar contraseñas con caracteres especiales")
    void testEncodeConCaracteresEspeciales() {
        // Arrange
        String passwordEspecial = "P@ssw0rd!#$%";
        
        // Act
        String hash = encoder.encode(passwordEspecial);
        
        // Assert
        assertNotNull(hash, "Debe poder encriptar contraseñas con caracteres especiales");
        assertEquals(64, hash.length(), "El hash debe tener longitud correcta");
    }
    
    @Test
    @DisplayName("Debe manejar contraseñas vacías")
    void testEncodeConPasswordVacia() {
        // Arrange
        String passwordVacia = "";
        
        // Act
        String hash = encoder.encode(passwordVacia);
        
        // Assert
        assertNotNull(hash, "Debe generar un hash incluso para contraseña vacía");
        assertEquals(64, hash.length(), "El hash debe tener longitud correcta");
    }
    
    @Test
    @DisplayName("Debe manejar contraseñas muy largas")
    void testEncodeConPasswordLarga() {
        // Arrange
        String passwordLarga = "a".repeat(1000);
        
        // Act
        String hash = encoder.encode(passwordLarga);
        
        // Assert
        assertNotNull(hash, "Debe poder encriptar contraseñas largas");
        assertEquals(64, hash.length(), "El hash debe tener longitud correcta independiente del tamaño del input");
    }
    
    @Test
    @DisplayName("Debe generar hash solo con caracteres hexadecimales")
    void testHashContieneCaracteresHexadecimales() {
        // Arrange
        String password = "testPassword";
        
        // Act
        String hash = encoder.encode(password);
        
        // Assert
        assertTrue(hash.matches("^[a-f0-9]+$"), "El hash debe contener solo caracteres hexadecimales (0-9, a-f)");
    }
    
    @Test
    @DisplayName("Debe ser sensible a mayúsculas y minúsculas")
    void testSensibleAMayusculas() {
        // Arrange
        String password1 = "Password";
        String password2 = "password";
        
        // Act
        String hash1 = encoder.encode(password1);
        String hash2 = encoder.encode(password2);
        
        // Assert
        assertNotEquals(hash1, hash2, "Contraseñas que difieren solo en mayúsculas deben tener hashes diferentes");
    }
}