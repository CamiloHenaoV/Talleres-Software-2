# Biblioteca CRUD (Java Swing) - MVC + SOLID + Observer + Microkernel

Proyecto sencillo para gestionar libros (CRUD) con Java puro y Swing, ejecutable desde VSCode.

## Ejecutar
- **Windows:** `run.bat`
- **Linux/Mac:** `bash run.sh`

Genera carpeta `out/` con los `.class` y copia los recursos de `META-INF/services` para cargar plugins (ServiceLoader).

## Estructura
- `model/` Entidades, repositorio, servicio
- `view/` Swing (JFrame, JPanel, JTable)
- `controller/` Controlador MVC
- `core/` Eventos (Observer) y Kernel (Microkernel)
- `plugins/` Ejemplos de plugins: persistencia CSV y reporte TXT
