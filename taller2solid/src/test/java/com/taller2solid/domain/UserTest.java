package com.taller2solid.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase User del dominio
 * Valida constructores, getters, setters y comportamiento de la entidad
 */
@DisplayName("Pruebas unitarias de User")
class UserTest {
    
    @Test
    @DisplayName("Debe crear un usuario con constructor vacío")
    void testConstructorVacio() {
        // Arrange & Act
        User user = new User();
        
        // Assert
        assertNotNull(user, "El usuario no debe ser nulo");
        assertNull(user.getId(), "El ID debe ser nulo inicialmente");
        assertNull(user.getUsername(), "El username debe ser nulo inicialmente");
        assertNull(user.getPassword(), "El password debe ser nulo inicialmente");
        assertNull(user.getEmail(), "El email debe ser nulo inicialmente");
        assertNull(user.getRole(), "El role debe ser nulo inicialmente");
        assertFalse(user.isActive(), "El usuario debe estar inactivo por defecto");
    }
    
    @Test
    @DisplayName("Debe crear un usuario con constructor completo")
    void testConstructorCompleto() {
        // Arrange & Act
        User user = new User(1, "testuser", "password123", "test@example.com", Role.USER);
        
        // Assert
        assertNotNull(user, "El usuario no debe ser nulo");
        assertEquals(1, user.getId(), "El ID debe ser 1");
        assertEquals("testuser", user.getUsername(), "El username debe ser 'testuser'");
        assertEquals("password123", user.getPassword(), "El password debe ser 'password123'");
        assertEquals("test@example.com", user.getEmail(), "El email debe ser 'test@example.com'");
        assertEquals(Role.USER, user.getRole(), "El role debe ser USER");
        assertTrue(user.isActive(), "El usuario debe estar activo por defecto en constructor completo");
    }
    
    @Test
    @DisplayName("Debe establecer y obtener el ID correctamente")
    void testSetYGetId() {
        // Arrange
        User user = new User();
        
        // Act
        user.setId(10);
        
        // Assert
        assertEquals(10, user.getId(), "El ID debe ser 10");
    }
    
    @Test
    @DisplayName("Debe establecer y obtener el username correctamente")
    void testSetYGetUsername() {
        // Arrange
        User user = new User();
        
        // Act
        user.setUsername("nuevoUsuario");
        
        // Assert
        assertEquals("nuevoUsuario", user.getUsername(), "El username debe ser 'nuevoUsuario'");
    }
    
    @Test
    @DisplayName("Debe establecer y obtener el password correctamente")
    void testSetYGetPassword() {
        // Arrange
        User user = new User();
        
        // Act
        user.setPassword("miPassword123");
        
        // Assert
        assertEquals("miPassword123", user.getPassword(), "El password debe ser 'miPassword123'");
    }
    
    @Test
    @DisplayName("Debe establecer y obtener el email correctamente")
    void testSetYGetEmail() {
        // Arrange
        User user = new User();
        
        // Act
        user.setEmail("correo@test.com");
        
        // Assert
        assertEquals("correo@test.com", user.getEmail(), "El email debe ser 'correo@test.com'");
    }
    
    @Test
    @DisplayName("Debe establecer y obtener el role correctamente")
    void testSetYGetRole() {
        // Arrange
        User user = new User();
        
        // Act
        user.setRole(Role.ADMIN);
        
        // Assert
        assertEquals(Role.ADMIN, user.getRole(), "El role debe ser ADMIN");
    }
    
    @Test
    @DisplayName("Debe establecer y obtener el estado activo correctamente")
    void testSetYGetActive() {
        // Arrange
        User user = new User();
        
        // Act
        user.setActive(true);
        
        // Assert
        assertTrue(user.isActive(), "El usuario debe estar activo");
        
        // Act
        user.setActive(false);
        
        // Assert
        assertFalse(user.isActive(), "El usuario debe estar inactivo");
    }
    
    @Test
    @DisplayName("Debe generar toString con información correcta")
    void testToString() {
        // Arrange
        User user = new User(5, "admin", "pass", "admin@test.com", Role.ADMIN);
        
        // Act
        String resultado = user.toString();
        
        // Assert
        assertNotNull(resultado, "toString no debe retornar nulo");
        assertTrue(resultado.contains("id=5"), "toString debe contener el id");
        assertTrue(resultado.contains("username='admin'"), "toString debe contener el username");
        assertTrue(resultado.contains("email='admin@test.com'"), "toString debe contener el email");
        assertTrue(resultado.contains("role=ADMIN"), "toString debe contener el role");
        assertTrue(resultado.contains("active=true"), "toString debe contener el estado activo");
    }
    
    @Test
    @DisplayName("Debe manejar roles diferentes correctamente")
    void testDiferentesRoles() {
        // Arrange & Act
        User admin = new User(1, "admin", "pass", "admin@test.com", Role.ADMIN);
        User user = new User(2, "user", "pass", "user@test.com", Role.USER);
        User guest = new User(3, "guest", "pass", "guest@test.com", Role.GUEST);
        
        // Assert
        assertEquals(Role.ADMIN, admin.getRole(), "Debe ser ADMIN");
        assertEquals(Role.USER, user.getRole(), "Debe ser USER");
        assertEquals(Role.GUEST, guest.getRole(), "Debe ser GUEST");
    }
    
    @Test
    @DisplayName("Debe permitir cambiar todos los atributos de un usuario")
    void testModificarTodosLosAtributos() {
        // Arrange
        User user = new User(1, "original", "pass123", "original@test.com", Role.USER);
        
        // Act
        user.setId(99);
        user.setUsername("modificado");
        user.setPassword("newpass456");
        user.setEmail("nuevo@test.com");
        user.setRole(Role.ADMIN);
        user.setActive(false);
        
        // Assert
        assertEquals(99, user.getId());
        assertEquals("modificado", user.getUsername());
        assertEquals("newpass456", user.getPassword());
        assertEquals("nuevo@test.com", user.getEmail());
        assertEquals(Role.ADMIN, user.getRole());
        assertFalse(user.isActive());
    }
}