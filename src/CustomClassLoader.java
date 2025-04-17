import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomClassLoader extends ClassLoader {
    private final String customClassPath;

    public CustomClassLoader(String customClassPath) {
        this.customClassPath = customClassPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = loadClassData(name);
        if (classData == null) {
            throw new ClassNotFoundException("Class named " + name + " is not found");
        }
        return defineClass(name, classData, 0, classData.length);
    }

    private byte[] loadClassData(String className) {
        String classAsPath = className.replace('.', File.separatorChar) + ".class";
        Path path = Paths.get(customClassPath, classAsPath);

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            return null;
        }
    }
}
