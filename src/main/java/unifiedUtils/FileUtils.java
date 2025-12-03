package unifiedUtils;

import java.io.File;

public class FileUtils {
    /**
     * Recursively searches for a file in a nested folder structure.
     *
     * @param rootDir the root directory to start searching from
     * @param fileName the name of the file to search for
     * @return the File object if found, otherwise null
     */
    public static File findFile(File rootDir, String fileName) {
        if (rootDir == null || !rootDir.exists()) {
            return null;
        }

        if (rootDir.isFile() && rootDir.getName().toLowerCase().startsWith(fileName.toLowerCase())) {
            return rootDir; // Found the file
        }

        if (rootDir.isDirectory()) {
            File[] files = rootDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    File result = findFile(file, fileName);
                    if (result != null) {
                        return result; // Return as soon as file is found
                    }
                }
            }
        }

        return null; // File not found in this path
    }
}
