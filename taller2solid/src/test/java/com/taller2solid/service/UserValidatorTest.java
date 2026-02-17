package com.taller2solid.service;

import com.taller2solid.domain.Role;
import com.taller2solid.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para UserValidator
 * Valida todas las reglas de negocio para usuarios
 */
@DisplayName("Pruebas unitarias de UserValidator")
class UserValidatorTest {
    
    private UserValidator validator;
    
    @BeforeEach
    void setUp() {
        validator = new UserValidator();
    }
    
    // ========================================
    // PRUEBAS DE USUARIO COMPLETO
    // ========================================
    
    @Test
    @DisplayName("Debe validar un usuario completamente válido sin errores")
    void testUsuarioValido() {
        // Arrange
        User user = new User();
        user.setUsername("usuario123");
        user.setPassword("pass123");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);
        
        // Act
        List<String> errores = validator.validate(user);
        
        // Assert
        assertTrue(errores.isEmpty(), "No debe haber errores para un usuario válido");
        assertTrue(validator.isValid(user), "isValid() debe retornar true");
    }
    
    @Test
    @DisplayName("Debe rechazar un usuario nulo")
    void testUsuarioNulo() {
        // Act
        List<String> errores = validator.validate(null);
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber errores para usuario nulo");
        assertEquals(1, errores.size(), "Debe haber exactamente 1 error");
        assertTrue(errores.get(0).contains("no puede ser nulo"), "El mensaje debe indicar que el usuario es nulo");
    }
    
    @Test
    @DisplayName("Debe acumular múltiples errores de validación")
    void testMultiplesErrores() {
        // Arrange
        User user = new User();
        user.setUsername("ab"); // muy corto
        user.setPassword("123"); // muy corta y sin letras
        user.setEmail("emailinvalido"); // formato incorrecto
        user.setRole(null); // role nulo
        
        // Act
        List<String> errores = validator.validate(user);
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber múltiples errores");
        assertTrue(errores.size() >= 4, "Debe haber al menos 4 errores");
    }
    
    // ========================================
    // PRUEBAS DE USERNAME
    // ========================================
    
    @Test
    @DisplayName("Debe aceptar un username válido")
    void testUsernameValido() {
        // Act
        List<String> errores = validator.validateUsername("usuario_123");
        
        // Assert
        assertTrue(errores.isEmpty(), "No debe haber errores para username válido");
    }
    
    @Test
    @DisplayName("Debe rechazar username nulo")
    void testUsernameNulo() {
        // Act
        List<String> errores = validator.validateUsername(null);
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber error para username nulo");
        assertTrue(errores.get(0).contains("obligatorio"), "El mensaje debe indicar que es obligatorio");
    }
    
    @Test
    @DisplayName("Debe rechazar username vacío")
    void testUsernameVacio() {
        // Act
        List<String> errores = validator.validateUsername("");
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber error para username vacío");
    }
    
    @Test
    @DisplayName("Debe rechazar username solo con espacios")
    void testUsernameSoloEspacios() {
        // Act
        List<String> errores = validator.validateUsername("   ");
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber error para username solo con espacios");
    }
    
    @Test
    @DisplayName("Debe rechazar username muy corto")
    void testUsernameMuyCorto() {
        // Act
        List<String> errores = validator.validateUsername("ab");
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber error para username menor a 3 caracteres");
        assertTrue(errores.get(0).contains("al menos 3 caracteres"), "El mensaje debe mencionar el mínimo de caracteres");
    }
    
    @Test
    @DisplayName("Debe rechazar username muy largo")
    void testUsernameMuyLargo() {
        // Arrange
        String usernameLargo = "a".repeat(21);
        
        // Act
        List<String> errores = validator.validateUsername(usernameLargo);
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber error para username mayor a 20 caracteres");
        assertTrue(errores.get(0).contains("no puede exceder 20 caracteres"), "El mensaje debe mencionar el máximo de caracteres");
    }
    
    @Test
    @DisplayName("Debe rechazar username con caracteres especiales")
    void testUsernameConCaracteresEspeciales() {
        // Act
        List<String> errores1 = validator.validateUsername("user@name");
        List<String> errores2 = validator.validateUsername("user name");
        List<String> errores3 = validator.validateUsername("user-name");
        
        // Assert
        assertFalse(errores1.isEmpty(), "Debe rechazar @ en username");
        assertFalse(errores2.isEmpty(), "Debe rechazar espacios en username");
        assertFalse(errores3.isEmpty(), "Debe rechazar guiones en username");
    }
    
    @Test
    @DisplayName("Debe aceptar username con guión bajo")
    void testUsernameConGuionBajo() {
        // Act
        List<String> errores = validator.validateUsername("user_name");
        
        // Assert
        assertTrue(errores.isEmpty(), "Debe aceptar guión bajo en username");
    }
    
    @Test
    @DisplayName("Debe aceptar username en los límites de longitud")
    void testUsernameLimitesLongitud() {
        // Act
        List<String> erroresMinimo = validator.validateUsername("abc"); // 3 caracteres
        List<String> erroresMaximo = validator.validateUsername("a".repeat(20)); // 20 caracteres
        
        // Assert
        assertTrue(erroresMinimo.isEmpty(), "Debe aceptar username con 3 caracteres (mínimo)");
        assertTrue(erroresMaximo.isEmpty(), "Debe aceptar username con 20 caracteres (máximo)");
    }
    
    // ========================================
    // PRUEBAS DE PASSWORD
    // ========================================
    
    @Test
    @DisplayName("Debe aceptar un password válido")
    void testPasswordValido() {
        // Act
        List<String> errores = validator.validatePassword("pass123");
        
        // Assert
        assertTrue(errores.isEmpty(), "No debe haber errores para password válido");
    }
    
    @Test
    @DisplayName("Debe rechazar password nulo")
    void testPasswordNulo() {
        // Act
        List<String> errores = validator.validatePassword(null);
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber error para password nulo");
        assertTrue(errores.get(0).contains("obligatoria"), "El mensaje debe indicar que es obligatorio");
    }
    
    @Test
    @DisplayName("Debe rechazar password vacío")
    void testPasswordVacio() {
        // Act
        List<String> errores = validator.validatePassword("");
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber error para password vacío");
    }
    
    @Test
    @DisplayName("Debe rechazar password muy corto")
    void testPasswordMuyCorto() {
        // Act
        List<String> errores = validator.validatePassword("pas1");
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber error para password menor a 6 caracteres");
        assertTrue(errores.get(0).contains("al menos 6 caracteres"), "El mensaje debe mencionar el mínimo de caracteres");
    }
    
    @Test
    @DisplayName("Debe rechazar password sin números")
    void testPasswordSinNumeros() {
        // Act
        List<String> errores = validator.validatePassword("password");
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber error para password sin números");
        assertTrue(errores.get(0).contains("al menos un número"), "El mensaje debe indicar que falta un número");
    }
    
    @Test
    @DisplayName("Debe rechazar password sin letras")
    void testPasswordSinLetras() {
        // Act
        List<String> errores = validator.validatePassword("123456");
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber error para password sin letras");
        assertTrue(errores.get(0).contains("al menos una letra"), "El mensaje debe indicar que falta una letra");
    }
    
    @Test
    @DisplayName("Debe aceptar password con mayúsculas y minúsculas")
    void testPasswordConMayusculasYMinusculas() {
        // Act
        List<String> errores = validator.validatePassword("PassWord123");
        
        // Assert
        assertTrue(errores.isEmpty(), "Debe aceptar password con mayúsculas y minúsculas");
    }
    
    @Test
    @DisplayName("Debe aceptar password con caracteres especiales")
    void testPasswordConCaracteresEspeciales() {
        // Act
        List<String> errores = validator.validatePassword("pass@123!");
        
        // Assert
        assertTrue(errores.isEmpty(), "Debe aceptar password con caracteres especiales");
    }
    
    @Test
    @DisplayName("Debe aceptar password en el límite mínimo")
    void testPasswordLimiteMinimo() {
        // Act
        List<String> errores = validator.validatePassword("pass12"); // 6 caracteres
        
        // Assert
        assertTrue(errores.isEmpty(), "Debe aceptar password con 6 caracteres exactos");
    }
    
    // ========================================
    // PRUEBAS DE EMAIL
    // ========================================
    
    @Test
    @DisplayName("Debe aceptar un email válido")
    void testEmailValido() {
        // Act
        List<String> errores1 = validator.validateEmail("usuario@example.com");
        List<String> errores2 = validator.validateEmail("user.name@domain.co");
        List<String> errores3 = validator.validateEmail("test123@test-domain.com");
        
        // Assert
        assertTrue(errores1.isEmpty(), "Debe aceptar email válido básico");
        assertTrue(errores2.isEmpty(), "Debe aceptar email con punto en usuario");
        assertTrue(errores3.isEmpty(), "Debe aceptar email con guión en dominio");
    }
    
    @Test
    @DisplayName("Debe rechazar email nulo")
    void testEmailNulo() {
        // Act
        List<String> errores = validator.validateEmail(null);
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber error para email nulo");
        assertTrue(errores.get(0).contains("obligatorio"), "El mensaje debe indicar que es obligatorio");
    }
    
    @Test
    @DisplayName("Debe rechazar email vacío")
    void testEmailVacio() {
        // Act
        List<String> errores = validator.validateEmail("");
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber error para email vacío");
    }
    
    @Test
    @DisplayName("Debe rechazar email solo con espacios")
    void testEmailSoloEspacios() {
        // Act
        List<String> errores = validator.validateEmail("   ");
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber error para email solo con espacios");
    }
    
    @Test
    @DisplayName("Debe rechazar email sin @")
    void testEmailSinArroba() {
        // Act
        List<String> errores = validator.validateEmail("usuarioexample.com");
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe rechazar email sin @");
        assertTrue(errores.get(0).contains("formato"), "El mensaje debe indicar formato inválido");
    }
    
    @Test
    @DisplayName("Debe rechazar email sin dominio")
    void testEmailSinDominio() {
        // Act
        List<String> errores = validator.validateEmail("usuario@");
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe rechazar email sin dominio");
    }
    
    @Test
    @DisplayName("Debe rechazar email sin extensión")
    void testEmailSinExtension() {
        // Act
        List<String> errores = validator.validateEmail("usuario@domain");
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe rechazar email sin extensión (.com, .co, etc)");
    }
    
    @Test
    @DisplayName("Debe rechazar email con formato incorrecto")
    void testEmailFormatoIncorrecto() {
        // Act
        List<String> errores1 = validator.validateEmail("@example.com");
        List<String> errores2 = validator.validateEmail("usuario@@example.com");
        List<String> errores3 = validator.validateEmail("usuario @example.com");
        
        // Assert
        assertFalse(errores1.isEmpty(), "Debe rechazar email que empieza con @");
        assertFalse(errores2.isEmpty(), "Debe rechazar email con doble @");
        assertFalse(errores3.isEmpty(), "Debe rechazar email con espacios");
    }
    
    // ========================================
    // PRUEBAS DE ROLE
    // ========================================
    
    @Test
    @DisplayName("Debe aceptar usuario con role válido")
    void testRoleValido() {
        // Arrange
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("pass123");
        user1.setEmail("user1@test.com");
        user1.setRole(Role.ADMIN);
        
        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("pass123");
        user2.setEmail("user2@test.com");
        user2.setRole(Role.USER);
        
        User user3 = new User();
        user3.setUsername("user3");
        user3.setPassword("pass123");
        user3.setEmail("user3@test.com");
        user3.setRole(Role.GUEST);
        
        // Act
        List<String> errores1 = validator.validate(user1);
        List<String> errores2 = validator.validate(user2);
        List<String> errores3 = validator.validate(user3);
        
        // Assert
        assertTrue(errores1.isEmpty(), "Debe aceptar role ADMIN");
        assertTrue(errores2.isEmpty(), "Debe aceptar role USER");
        assertTrue(errores3.isEmpty(), "Debe aceptar role GUEST");
    }
    
    @Test
    @DisplayName("Debe rechazar usuario con role nulo")
    void testRoleNulo() {
        // Arrange
        User user = new User();
        user.setUsername("usuario");
        user.setPassword("pass123");
        user.setEmail("test@example.com");
        user.setRole(null);
        
        // Act
        List<String> errores = validator.validate(user);
        
        // Assert
        assertFalse(errores.isEmpty(), "Debe haber error para role nulo");
        assertTrue(errores.stream().anyMatch(e -> e.contains("rol") && e.contains("obligatorio")), 
                   "Debe haber un error que indique que el rol es obligatorio");
    }
}
