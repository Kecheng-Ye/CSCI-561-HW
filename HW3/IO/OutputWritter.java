package IO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputWritter {
    public static void writeSolutionsToFile(final String filePath, final boolean result) {
        try {
            File outputFile = new File(filePath);
            outputFile.createNewFile();
            FileWriter myWriter = new FileWriter(filePath);
            myWriter.write(result ? "TRUE" : "FALSE");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
