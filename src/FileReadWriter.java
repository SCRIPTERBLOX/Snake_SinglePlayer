import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.List;

public class FileReadWriter {
    public static void write(String name, String path, String contents) {
        // Replace illegal characters in the filename
        name = name.replaceAll("[:\\\\/*?|<>]", "-");  // Replace invalid characters with hyphen

        // Ensure the directory exists
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();  // Create directories if they don't exist
        }

        // Combine path and sanitized name safely
        Path fullPath = Paths.get(path, name);

        File file = new File(fullPath.toString());

        try {
            // Create the file
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());

                // Write contents to the file
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(contents);
                    System.out.println("Contents written to the file.");
                }
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
            for (String line : lines) {
                return line;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "No result found";
        }
        return "No result found";
    }
}