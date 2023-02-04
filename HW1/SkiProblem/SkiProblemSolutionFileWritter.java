package SkiProblem;

import Search.Result;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SkiProblemSolutionFileWritter {
    public static void writeSolutionsToFile(String filePath, List<Result<SkiSolution>> solutions) {
        try {
            File outputFile = new File(filePath);
            outputFile.createNewFile();
            FileWriter myWriter = new FileWriter(filePath);
            for (final Result<SkiSolution> solution : solutions) {
                myWriter.write(solution.toString() + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
