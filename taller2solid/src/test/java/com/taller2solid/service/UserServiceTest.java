package com.taller2solid.service;

import com.taller2solid.access.IUserRepository;
import com.taller2solid.domain.Role;
import com.taller2solid.domain.User;
import com.taller2solid.security.IPasswordEncoder;
import com.taller2solid.service.UserService.ServiceResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para UserService
 * Usa Mockito para simular dependencias (repositorio y encoder)
 */
@DisplayName("Pruebas unitarias de UserService")
class UserServiceTest {
    
    @Mock
    private IUserRepository mockRepository;
    
    @Mock
    private IPasswordEncoder mockEncoder;
    
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(mockRepository, mockEncoder);
    }
    
    // ========================================
    // PRUEBAS DE CREAR USUARIO
    // ========================================
    
    @Test
    @DisplayName("Debe crear un usuario válido exitosamente")
    void testCrearUsuarioExitoso() {
        // Arrange
        User user = new User();
        user.setUsername("nuevouser");
        user.setPassword("pass123");
        user.setEmail("nuevo@test.com");
        user.setRole(Role.USER);
        
        when(mockRepository.findByUsername("nuevouser")).thenReturn(Optional.empty());
        when(mockEncoder.encode("pass123")).thenReturn("hashedPassword123");
        when(mockRepository.save(any(User.class))).thenReturn(true);
        
        // Act
        ServiceResult result = userService.createUser(user);
        
        // Assert
        assertTrue(result.isSuccess(), "La creación debe ser exitosa");
        assertEquals("Usuario creado exitosamente", result.getMessage());
        assertTrue(user.isActive(), "El usuario debe estar activo");
        assertEquals("hashedPassword123", user.getPassword(), "La contraseña debe estar encriptada");
        
        verify(mockRepository).findByUsername("nuevouser");
        verify(mockEncoder).encode("pass123");
        verify(mockRepository).save(user);
    }
    
    @Test
    @DisplayName("Debe rechazar crear usuario con username duplicado")
    void testCrearUsuarioUsernameDuplicado() {
        // Arrange
        User user = new User();
        user.setUsername("existente");
        user.setPassword("pass123");
        user.setEmail("test@test.com");
        user.setRole(Role.USER);
        
        User usuarioExistente = new User();
        usuarioExistente.setUsername("existente");
        
        when(mockRepository.findByUsername("existente")).thenReturn(Optional.of(usuarioExistente));
        
        // Act
        ServiceResult result = userService.createUser(user);
        
        // Assert
        assertFalse(result.isSuccess(), "La creación debe fallar");
        assertTrue(result.getMessage().contains("ya está en uso"), "El mensaje debe indicar username duplicado");
        
        verify(mockRepository).findByUsername("existente");
        verify(mockEncoder, never()).encode(anyString());
        verify(mockRepository, never()).save(any(User.class));
    }
    
    @Test
    @DisplayName("Debe rechazar crear usuario con datos inválidos")
    void testCrearUsuarioDatosInvalidos() {
        // Arrange
        User user = new User();
        user.setUsername("ab"); // muy corto
        user.setPassword("123"); // inválido
        user.setEmail("emailinvalido");
        user.setRole(Role.USER);
        
        // Act
        ServiceResult result = userService.createUser(user);
        
        // Assert
        assertFalse(result.isSuccess(), "La creación debe fallar por validación");
        
        verify(mockRepository, never()).findByUsername(anyString());
        verify(mockEncoder, never()).encode(anyString());
        verify(mockRepository, never()).save(any(User.class));
    }
    
    @Test
    @DisplayName("Debe rechazar crear usuario cuando falla el guardado")
    void testCrearUsuarioFallaGuardado() {
        // Arrange
        User user = new User();
        user.setUsername("usuario");
        user.setPassword("pass123");
        user.setEmail("test@test.com");
        user.setRole(Role.USER);
        
        when(mockRepository.findByUsername("usuario")).thenReturn(Optional.empty());
        when(mockEncoder.encode("pass123")).thenReturn("hashedPassword");
        when(mockRepository.save(any(User.class))).thenReturn(false);
        
        // Act
        ServiceResult result = userService.createUser(user);
        
        // Assert
        assertFalse(result.isSuccess(), "La creación debe fallar");
        assertTrue(result.getMessage().contains("Error al guardar"), "El mensaje debe indicar error de guardado");
        
        verify(mockRepository).save(any(User.class));
    }
    
    // ========================================
    // PRUEBAS DE AUTENTICACIÓN
    // ========================================
    
    @Test
    @DisplayName("Debe autenticar usuario con credenciales correctas")
    void testAutenticacionExitosa() {
        // Arrange
        User user = new User();
        user.setUsername("usuario");
        user.setPassword("hashedPassword");
        user.setActive(true);
        
        when(mockRepository.findByUsername("usuario")).thenReturn(Optional.of(user));
        when(mockEncoder.matches("pass123", "hashedPassword")).thenReturn(true);
        
        // Act
        ServiceResult result = userService.authenticate("usuario", "pass123");
        
        // Assert
        assertTrue(result.isSuccess(), "La autenticación debe ser exitosa");
        assertEquals("Autenticación exitosa", result.getMessage());
        assertNotNull(result.getUser(), "Debe retornar el usuario autenticado");
        
        verify(mockRepository).findByUsername("usuario");
        verify(mockEncoder).matches("pass123", "hashedPassword");
    }
    
    @Test
    @DisplayName("Debe rechazar autenticación con contraseña incorrecta")
    void testAutenticacionPasswordIncorrecta() {
        // Arrange
        User user = new User();
        user.setUsername("usuario");
        user.setPassword("hashedPassword");
        user.setActive(true);
        
        when(mockRepository.findByUsername("usuario")).thenReturn(Optional.of(user));
        when(mockEncoder.matches("passIncorrecta", "hashedPassword")).thenReturn(false);
        
        // Act
        ServiceResult result = userService.authenticate("usuario", "passIncorrecta");
        
        // Assert
        assertFalse(result.isSuccess(), "La autenticación debe fallar");
        assertTrue(result.getMessage().contains("incorrectos"), "El mensaje debe indicar credenciales incorrectas");
        
        verify(mockEncoder).matches("passIncorrecta", "hashedPassword");
    }
    
    @Test
    @DisplayName("Debe rechazar autenticación de usuario inexistente")
    void testAutenticacionUsuarioInexistente() {
        // Arrange
        when(mockRepository.findByUsername("noexiste")).thenReturn(Optional.empty());
        
        // Act
        ServiceResult result = userService.authenticate("noexiste", "pass123");
        
        // Assert
        assertFalse(result.isSuccess(), "La autenticación debe fallar");
        assertTrue(result.getMessage().contains("incorrectos"), "El mensaje debe indicar credenciales incorrectas");
        
        verify(mockRepository).findByUsername("noexiste");
        verify(mockEncoder, never()).matches(anyString(), anyString());
    }
    
    @Test
    @DisplayName("Debe rechazar autenticación de usuario inactivo")
    void testAutenticacionUsuarioInactivo() {
        // Arrange
        User user = new User();
        user.setUsername("usuario");
        user.setPassword("hashedPassword");
        user.setActive(false);
        
        when(mockRepository.findByUsername("usuario")).thenReturn(Optional.of(user));
        
        // Act
        ServiceResult result = userService.authenticate("usuario", "pass123");
        
        // Assert
        assertFalse(result.isSuccess(), "La autenticación debe fallar");
        assertTrue(result.getMessage().contains("desactivada"), "El mensaje debe indicar cuenta desactivada");
        
        verify(mockRepository).findByUsername("usuario");
        verify(mockEncoder, never()).matches(anyString(), anyString());
    }
    
    @Test
    @DisplayName("Debe rechazar autenticación con username vacío")
    void testAutenticacionUsernameVacio() {
        // Act
        ServiceResult result1 = userService.authenticate("", "pass123");
        ServiceResult result2 = userService.authenticate("   ", "pass123");
        ServiceResult result3 = userService.authenticate(null, "pass123");
        
        // Assert
        assertFalse(result1.isSuccess(), "Debe rechazar username vacío");
        assertFalse(result2.isSuccess(), "Debe rechazar username solo con espacios");
        assertFalse(result3.isSuccess(), "Debe rechazar username nulo");
        
        verify(mockRepository, never()).findByUsername(anyString());
    }
    
    @Test
    @DisplayName("Debe rechazar autenticación con password vacío")
    void testAutenticacionPasswordVacio() {
        // Act
        ServiceResult result1 = userService.authenticate("usuario", "");
        ServiceResult result2 = userService.authenticate("usuario", null);
        
        // Assert
        assertFalse(result1.isSuccess(), "Debe rechazar password vacío");
        assertFalse(result2.isSuccess(), "Debe rechazar password nulo");
        
        verify(mockRepository, never()).findByUsername(anyString());
    }
    
    // ========================================
    // PRUEBAS DE ACTUALIZAR USUARIO
    // ========================================
    
    @Test
    @DisplayName("Debe actualizar un usuario existente exitosamente")
    void testActualizarUsuarioExitoso() {
        // Arrange
        User usuarioExistente = new User(1, "usuario", "hashViejo", "old@test.com", Role.USER);
        
        User usuarioActualizado = new User();
        usuarioActualizado.setId(1);
        usuarioActualizado.setUsername("usuario");
        usuarioActualizado.setPassword("newpass123");
        usuarioActualizado.setEmail("new@test.com");
        usuarioActualizado.setRole(Role.ADMIN);
        
        when(mockRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(mockEncoder.encode("newpass123")).thenReturn("hashNuevo");
        when(mockRepository.update(any(User.class))).thenReturn(true);
        
        // Act
        ServiceResult result = userService.updateUser(usuarioActualizado);
        
        // Assert
        assertTrue(result.isSuccess(), "La actualización debe ser exitosa");
        assertEquals("hashNuevo", usuarioActualizado.getPassword(), "La contraseña debe estar encriptada");
        
        verify(mockRepository).findById(1);
        verify(mockEncoder).encode("newpass123");
        verify(mockRepository).update(usuarioActualizado);
    }
    
    @Test
    @DisplayName("Debe rechazar actualizar usuario sin ID")
    void testActualizarUsuarioSinId() {
        // Arrange
        User user = new User();
        user.setUsername("usuario");
        user.setPassword("pass123");
        user.setEmail("test@test.com");
        user.setRole(Role.USER);
        
        // Act
        ServiceResult result = userService.updateUser(user);
        
        // Assert
        assertFalse(result.isSuccess(), "Debe rechazar actualización sin ID");
        assertTrue(result.getMessage().contains("ID"), "El mensaje debe mencionar el ID");
        
        verify(mockRepository, never()).findById(any());
        verify(mockRepository, never()).update(any());
    }
    
    @Test
    @DisplayName("Debe rechazar actualizar usuario inexistente")
    void testActualizarUsuarioInexistente() {
        // Arrange
        User user = new User();
        user.setId(999);
        user.setUsername("usuario");
        user.setPassword("pass123");
        user.setEmail("test@test.com");
        user.setRole(Role.USER);
        
        when(mockRepository.findById(999)).thenReturn(Optional.empty());
        
        // Act
        ServiceResult result = userService.updateUser(user);
        
        // Assert
        assertFalse(result.isSuccess(), "Debe rechazar actualización de usuario inexistente");
        assertTrue(result.getMessage().contains("no existe"), "El mensaje debe indicar que no existe");
        
        verify(mockRepository).findById(999);
        verify(mockRepository, never()).update(any());
    }
    
    // ========================================
    // PRUEBAS DE ELIMINAR USUARIO
    // ========================================
    
    @Test
    @DisplayName("Debe eliminar un usuario existente exitosamente")
    void testEliminarUsuarioExitoso() {
        // Arrange
        User user = new User(1, "usuario", "pass", "test@test.com", Role.USER);
        
        when(mockRepository.findById(1)).thenReturn(Optional.of(user));
        when(mockRepository.delete(1)).thenReturn(true);
        
        // Act
        ServiceResult result = userService.deleteUser(1);
        
        // Assert
        assertTrue(result.isSuccess(), "La eliminación debe ser exitosa");
        assertTrue(result.getMessage().contains("eliminado"), "El mensaje debe confirmar eliminación");
        
        verify(mockRepository).findById(1);
        verify(mockRepository).delete(1);
    }
    
    @Test
    @DisplayName("Debe rechazar eliminar usuario con ID nulo")
    void testEliminarUsuarioIdNulo() {
        // Act
        ServiceResult result = userService.deleteUser(null);
        
        // Assert
        assertFalse(result.isSuccess(), "Debe rechazar eliminación con ID nulo");
        assertTrue(result.getMessage().contains("ID"), "El mensaje debe mencionar el ID");
        
        verify(mockRepository, never()).findById(any());
        verify(mockRepository, never()).delete(any());
    }
    
    @Test
    @DisplayName("Debe rechazar eliminar usuario inexistente")
    void testEliminarUsuarioInexistente() {
        // Arrange
        when(mockRepository.findById(999)).thenReturn(Optional.empty());
        
        // Act
        ServiceResult result = userService.deleteUser(999);
        
        // Assert
        assertFalse(result.isSuccess(), "Debe rechazar eliminación de usuario inexistente");
        assertTrue(result.getMessage().contains("no existe"), "El mensaje debe indicar que no existe");
        
        verify(mockRepository).findById(999);
        verify(mockRepository, never()).delete(any());
    }
    
    // ========================================
    // PRUEBAS DE BÚSQUEDA
    // ========================================
    
    @Test
    @DisplayName("Debe encontrar usuario por ID")
    void testFindUserById() {
        // Arrange
        User user = new User(1, "usuario", "pass", "test@test.com", Role.USER);
        when(mockRepository.findById(1)).thenReturn(Optional.of(user));
        
        // Act
        Optional<User> result = userService.findUserById(1);
        
        // Assert
        assertTrue(result.isPresent(), "Debe encontrar el usuario");
        assertEquals("usuario", result.get().getUsername());
        
        verify(mockRepository).findById(1);
    }
    
    @Test
    @DisplayName("Debe retornar vacío cuando no encuentra usuario por ID")
    void testFindUserByIdNoExiste() {
        // Arrange
        when(mockRepository.findById(999)).thenReturn(Optional.empty());
        
        // Act
        Optional<User> result = userService.findUserById(999);
        
        // Assert
        assertFalse(result.isPresent(), "No debe encontrar el usuario");
        
        verify(mockRepository).findById(999);
    }
    
    @Test
    @DisplayName("Debe encontrar usuario por username")
    void testFindUserByUsername() {
        // Arrange
        User user = new User(1, "usuario", "pass", "test@test.com", Role.USER);
        when(mockRepository.findByUsername("usuario")).thenReturn(Optional.of(user));
        
        // Act
        Optional<User> result = userService.findUserByUsername("usuario");
        
        // Assert
        assertTrue(result.isPresent(), "Debe encontrar el usuario");
        assertEquals(1, result.get().getId());
        
        verify(mockRepository).findByUsername("usuario");
    }
    
    @Test
    @DisplayName("Debe listar todos los usuarios")
    void testFindAllUsers() {
        // Arrange
        List<User> usuarios = Arrays.asList(
            new User(1, "user1", "pass", "user1@test.com", Role.USER),
            new User(2, "user2", "pass", "user2@test.com", Role.ADMIN)
        );
        when(mockRepository.findAll()).thenReturn(usuarios);
        
        // Act
        List<User> result = userService.findAllUsers();
        
        // Assert
        assertEquals(2, result.size(), "Debe retornar 2 usuarios");
        
        verify(mockRepository).findAll();
    }
}