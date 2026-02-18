package com.taller2solid.presentation;

import com.taller2solid.access.IUserRepository;
import com.taller2solid.access.UserRepositorySQLite;
import com.taller2solid.security.IPasswordEncoder;
import com.taller2solid.security.SHA256PasswordEncoder;
import com.taller2solid.service.UserService;

import javax.swing.*;

public class SwingApp {
    
    // Service ya configurado y listo para usar
    private static UserService userService;
    
    public static void main(String[] args) {
        // Configurar Look and Feel nativo del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Inicializar dependencias (ya está todo configurado)
        initializeServices();
        
        // Ejecutar la aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LoginFrame(userService).setVisible(true);
        });
    }
    
    /**
     * Inicializa todos los servicios necesarios
     */
    private static void initializeServices() {
        
        // Configurar repositorio (capa de acceso a datos)
        IUserRepository repository = new UserRepositorySQLite("usuarios.db");
        repository.initDatabase();
        
        // Configurar encriptador de contraseñas
        IPasswordEncoder passwordEncoder = new SHA256PasswordEncoder();
        
        // Crear el servicio (capa de lógica de negocio)
        userService = new UserService(repository, passwordEncoder);
    }
    
    /**
     * Obtener el servicio de usuarios
     * Puedes llamar a SwingApp.getUserService() desde cualquier ventana
     */
    public static UserService getUserService() {
        return userService;
    }
}
