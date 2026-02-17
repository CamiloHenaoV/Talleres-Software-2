package com.taller2solid;

import com.taller2solid.access.IUserRepository;
import com.taller2solid.access.UserRepositorySQLite;
import com.taller2solid.domain.Role;
import com.taller2solid.domain.User;
import com.taller2solid.security.IPasswordEncoder;
import com.taller2solid.security.SHA256PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Programa interactivo para probar con tu base de datos real
 * Usa la BD: usuarios.db creada en DB Browser
 */
public class PruebaConDBReal {
    
    private static final IPasswordEncoder encoder = new SHA256PasswordEncoder();
    private static final IUserRepository repository = new UserRepositorySQLite("usuarios.db");
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║      PRUEBAS CON BASE DE DATOS REAL - usuarios.db        ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        repository.initDatabase();
        System.out.println("✓ Conectado a: usuarios.db\n");
        
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenu();
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1:
                    listarTodosLosUsuarios();
                    break;
                case 2:
                    buscarPorId();
                    break;
                case 3:
                    buscarPorUsername();
                    break;
                case 4:
                    crearNuevoUsuario();
                    break;
                case 5:
                    actualizarUsuario();
                    break;
                case 6:
                    eliminarUsuario();
                    break;
                case 7:
                    probarAutenticacion();
                    break;
                case 8:
                    generarHashPassword();
                    break;
                case 0:
                    continuar = false;
                    break;
                default:
                    System.out.println("❌ Opción inválida\n");
            }
        }
        
        repository.closeConnection();
        scanner.close();
        System.out.println("\n✓ Conexión cerrada. ¡Hasta luego!");
    }
    
    private static void mostrarMenu() {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("MENÚ DE PRUEBAS");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("1. Listar todos los usuarios");
        System.out.println("2. Buscar usuario por ID");
        System.out.println("3. Buscar usuario por username");
        System.out.println("4. Crear nuevo usuario");
        System.out.println("5. Actualizar usuario");
        System.out.println("6. Eliminar usuario");
        System.out.println("7. Probar autenticación (login)");
        System.out.println("8. Generar hash de contraseña");
        System.out.println("0. Salir");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.print("Seleccione una opción: ");
    }
    
    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void listarTodosLosUsuarios() {
        System.out.println("\n📋 LISTA DE TODOS LOS USUARIOS");
        System.out.println("─────────────────────────────────────────────────────────────");
        
        List<User> usuarios = repository.findAll();
        
        if (usuarios.isEmpty()) {
            System.out.println("⚠️  No hay usuarios en la base de datos");
        } else {
            System.out.printf("%-5s %-15s %-30s %-10s %-8s%n", 
                "ID", "Username", "Email", "Rol", "Activo");
            System.out.println("─────────────────────────────────────────────────────────────");
            
            for (User user : usuarios) {
                System.out.printf("%-5d %-15s %-30s %-10s %-8s%n",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    user.isActive() ? "Sí" : "No");
            }
            System.out.println("\nTotal: " + usuarios.size() + " usuarios");
        }
        System.out.println();
    }
    
    private static void buscarPorId() {
        System.out.println("\n🔍 BUSCAR USUARIO POR ID");
        System.out.print("Ingrese el ID: ");
        
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Optional<User> userOpt = repository.findById(id);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                System.out.println("\n✓ Usuario encontrado:");
                mostrarDetalleUsuario(user);
            } else {
                System.out.println("❌ Usuario con ID " + id + " no encontrado");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido");
        }
        System.out.println();
    }
    
    private static void buscarPorUsername() {
        System.out.println("\n🔍 BUSCAR USUARIO POR USERNAME");
        System.out.print("Ingrese el username: ");
        String username = scanner.nextLine();
        
        Optional<User> userOpt = repository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("\n✓ Usuario encontrado:");
            mostrarDetalleUsuario(user);
        } else {
            System.out.println("❌ Usuario '" + username + "' no encontrado");
        }
        System.out.println();
    }
    
    private static void crearNuevoUsuario() {
        System.out.println("\n➕ CREAR NUEVO USUARIO");
        System.out.println("─────────────────────────────────────────────────────────────");
        
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.println("\nRoles disponibles:");
        System.out.println("1. ADMIN");
        System.out.println("2. USER");
        System.out.println("3. GUEST");
        System.out.print("Seleccione rol (1-3): ");
        
        Role role;
        try {
            int roleOption = Integer.parseInt(scanner.nextLine());
            switch (roleOption) {
                case 1: role = Role.ADMIN; break;
                case 2: role = Role.USER; break;
                case 3: role = Role.GUEST; break;
                default:
                    System.out.println("❌ Opción inválida, usando USER por defecto");
                    role = Role.USER;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Opción inválida, usando USER por defecto");
            role = Role.USER;
        }
        
        // Crear usuario y encriptar contraseña
        User newUser = new User(null, username, encoder.encode(password), email, role);
        
        boolean guardado = repository.save(newUser);
        
        if (guardado) {
            System.out.println("\n✅ Usuario creado exitosamente!");
            System.out.println("ID asignado: " + newUser.getId());
            System.out.println("Hash de password: " + newUser.getPassword());
        } else {
            System.out.println("\n❌ Error al crear usuario (posiblemente el username ya existe)");
        }
        System.out.println();
    }
    
    private static void actualizarUsuario() {
        System.out.println("\n✏️  ACTUALIZAR USUARIO");
        System.out.print("Ingrese el ID del usuario a actualizar: ");
        
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Optional<User> userOpt = repository.findById(id);
            
            if (!userOpt.isPresent()) {
                System.out.println("❌ Usuario no encontrado");
                return;
            }
            
            User user = userOpt.get();
            System.out.println("\nUsuario actual:");
            mostrarDetalleUsuario(user);
            
            System.out.println("\nIngrese nuevos datos (Enter para mantener actual):");
            
            System.out.print("Nuevo email [" + user.getEmail() + "]: ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) {
                user.setEmail(email);
            }
            
            System.out.print("Nueva password (dejar vacío para no cambiar): ");
            String password = scanner.nextLine();
            if (!password.isEmpty()) {
                user.setPassword(encoder.encode(password));
            }
            
            System.out.print("¿Activo? (S/N) [" + (user.isActive() ? "S" : "N") + "]: ");
            String activo = scanner.nextLine();
            if (!activo.isEmpty()) {
                user.setActive(activo.equalsIgnoreCase("S"));
            }
            
            boolean actualizado = repository.update(user);
            
            if (actualizado) {
                System.out.println("\n✅ Usuario actualizado exitosamente!");
            } else {
                System.out.println("\n❌ Error al actualizar usuario");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido");
        }
        System.out.println();
    }
    
    private static void eliminarUsuario() {
        System.out.println("\n🗑️  ELIMINAR USUARIO");
        System.out.print("Ingrese el ID del usuario a eliminar: ");
        
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Optional<User> userOpt = repository.findById(id);
            
            if (!userOpt.isPresent()) {
                System.out.println("❌ Usuario no encontrado");
                return;
            }
            
            User user = userOpt.get();
            System.out.println("\nUsuario a eliminar:");
            mostrarDetalleUsuario(user);
            
            System.out.print("\n⚠️  ¿Está seguro? (S/N): ");
            String confirmacion = scanner.nextLine();
            
            if (confirmacion.equalsIgnoreCase("S")) {
                boolean eliminado = repository.delete(id);
                
                if (eliminado) {
                    System.out.println("✅ Usuario eliminado exitosamente");
                } else {
                    System.out.println("❌ Error al eliminar usuario");
                }
            } else {
                System.out.println("❌ Operación cancelada");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido");
        }
        System.out.println();
    }
    
    private static void probarAutenticacion() {
        System.out.println("\n🔐 PROBAR AUTENTICACIÓN (LOGIN)");
        System.out.println("─────────────────────────────────────────────────────────────");
        
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        Optional<User> userOpt = repository.findByUsername(username);
        
        if (!userOpt.isPresent()) {
            System.out.println("\n❌ Usuario o contraseña incorrectos");
            System.out.println();
            return;
        }
        
        User user = userOpt.get();
        
        if (!user.isActive()) {
            System.out.println("\n❌ Usuario inactivo");
            System.out.println();
            return;
        }
        
        boolean passwordCorrecta = encoder.matches(password, user.getPassword());
        
        if (passwordCorrecta) {
            System.out.println("\n✅ AUTENTICACIÓN EXITOSA");
            System.out.println("Bienvenido, " + user.getUsername() + "!");
            System.out.println("Rol: " + user.getRole());
            System.out.println("Email: " + user.getEmail());
        } else {
            System.out.println("\n❌ Usuario o contraseña incorrectos");
        }
        System.out.println();
    }
    
    private static void generarHashPassword() {
        System.out.println("\n🔒 GENERAR HASH DE CONTRASEÑA");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.print("Ingrese la contraseña: ");
        String password = scanner.nextLine();
        
        String hash = encoder.encode(password);
        
        System.out.println("\nHash SHA-256:");
        System.out.println(hash);
        System.out.println("\nPuedes copiar este hash e insertarlo directamente en DB Browser");
        System.out.println();
    }
    
    private static void mostrarDetalleUsuario(User user) {
        System.out.println("  ID: " + user.getId());
        System.out.println("  Username: " + user.getUsername());
        System.out.println("  Email: " + user.getEmail());
        System.out.println("  Rol: " + user.getRole());
        System.out.println("  Activo: " + (user.isActive() ? "Sí" : "No"));
        System.out.println("  Password (hash): " + user.getPassword().substring(0, 20) + "...");
    }
}