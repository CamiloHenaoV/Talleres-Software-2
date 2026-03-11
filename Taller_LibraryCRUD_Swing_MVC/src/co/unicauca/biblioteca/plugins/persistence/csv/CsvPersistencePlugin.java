package co.unicauca.biblioteca.plugins.persistence.csv;

import co.unicauca.biblioteca.core.kernel.Plugin;
import co.unicauca.biblioteca.core.kernel.PluginContext;
import co.unicauca.biblioteca.core.spi.PersistenceProvider;
import co.unicauca.biblioteca.model.entity.Book;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Plugin (Microkernel) - Persistencia CSV (sin librerías externas).
 */
public class CsvPersistencePlugin implements Plugin, PersistenceProvider {
    private File file;

    @Override
    public String id() {
        return "csv-persistence";
    }

    @Override
    public void start(PluginContext context) {
        // El kernel comparte recursos globales (carpeta data)
        File dataDir = new File("data");
        if (!dataDir.exists()) dataDir.mkdirs();
        this.file = new File(dataDir, "books.csv");

        context.services().registerPersistence(this);
        context.eventBus().publish(new co.unicauca.biblioteca.core.events.LibraryEvent(
            co.unicauca.biblioteca.core.events.LibraryEventType.STATUS_MESSAGE,
            "Plugin activado: " + name() + " (" + file.getPath() + ")"
        ));
    }

    @Override
    public String name() {
        return "CSV Persistence";
    }

    @Override
    public List<Book> load() {
        if (file == null || !file.exists()) {
            return List.of();
        }
        List<Book> books = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";", -1);
                if (parts.length < 4) continue;
                String title = unescape(parts[0]);
                String author = unescape(parts[1]);
                String isbn = unescape(parts[2]);
                int reservations = Integer.parseInt(parts[3]);
                books.add(new Book(title, author, isbn, reservations));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error leyendo CSV: " + ex.getMessage(), ex);
        }
        return books;
    }

    @Override
    public void save(List<Book> books) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8))) {
            for (Book b : books) {
                bw.write(escape(b.getTitle()) + ";" + escape(b.getAuthor()) + ";" + escape(b.getIsbn()) + ";" + b.getReservations());
                bw.newLine();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error escribiendo CSV: " + ex.getMessage(), ex);
        }
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace(";", "\\;");
    }

    private String unescape(String s) {
        if (s == null) return "";
        StringBuilder out = new StringBuilder();
        boolean esc = false;
        for (int i=0;i<s.length();i++) {
            char c = s.charAt(i);
            if (esc) {
                out.append(c);
                esc = false;
            } else if (c == '\\') {
                esc = true;
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }
}
