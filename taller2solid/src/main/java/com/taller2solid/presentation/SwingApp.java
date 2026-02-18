package com.taller2solid.presentation;

import com.taller2solid.access.IUserRepository;
import com.taller2solid.access.UserRepositorySQLite;
import com.taller2solid.security.IPasswordEncoder;
import com.taller2solid.security.SHA256PasswordEncoder;
import com.taller2solid.service.UserService;

import javax.swing.*;

/**
 * EJEMPLO DE USO:
 * 
 *   // Autenticar usuario
 *   UserService.ServiceResult result = userService.authenticate(username, password);
 *   if (result.isSuccess()) {
 *       User user = result.getUser();
 *       // Abrir ventana principal
 *   } else {
 *       JOptionPane.showMessageDialog(null, result.getMessage());
 *   }
 * 
 *   // Listar usuarios
 *   List<User> usuarios = userService.findAllUsers();
 *   for (User u : usuarios) {
 *       System.out.println(u.getUsername());
 *   }
 * 
 *   // Crear usuario
 *   User newUser = new User(null, "juan", "password123", "juan@mail.com", Role.USER);
 *   UserService.ServiceResult result = userService.createUser(newUser);
 *   if (result.isSuccess()) {
 *       JOptionPane.showMessageDialog(null, "Usuario creado!");
 *   }
 * 
 * VENTANAS SUGERIDAS:
 * 1. LoginFrame - Ventana de inicio de sesión
 * 2. MainFrame - Ventana principal con menú
 * 3. UserListFrame - Listado de usuarios en tabla
 * 4. UserFormDialog - Formulario para crear/editar
 * 
 * TODO LO DEMÁS YA ESTÁ HECHO (BD, seguridad, validaciones)
 */
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
            // TODO: Tu compañero debe descomentar esto cuando cree LoginFrame
            new LoginFrame(userService).setVisible(true);
            
            // Mientras tanto, mostrar mensaje
            JOptionPane.showMessageDialog(null,
                "Backend configurado correctamente!\n\n" +
                "Próximo paso:\n" +
                "1. Crear LoginFrame.java\n" +
                "2. Descomentar la línea de arriba\n\n" +
                "El userService ya está disponible para usar.",
                "Sistema listo",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    /**
     * Inicializa todos los servicios necesarios
     * Tu compañero NO necesita modificar esto
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
