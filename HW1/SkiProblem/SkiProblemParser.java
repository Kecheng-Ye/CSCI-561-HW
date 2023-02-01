package SkiProblem;

import Search.SearchMethod;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SkiProblemParser {
    public static class SkiProblemParserResult{
        public final SkiProblem problem;
        public final SearchMethod searchMethod;

        public SkiProblemParserResult(final SkiProblem problem, final SearchMethod searchMethod) {
            this.problem = problem;
            this.searchMethod = searchMethod;
        }
    }

    public static SkiProblemParserResult parseFromText(String filePath) {
        try {
            File myObj = new File(filePath);
            Scanner fileReader = new Scanner(myObj);

            // First Line
            String methodString = fileReader.nextLine();
            SearchMethod method = SearchMethod.fromStr(methodString);

            // Second Line
            List<Integer> mapDimensions = readTwoNumber(fileReader);
            int width = mapDimensions.get(0);
            int height = mapDimensions.get(1);

            // Third Line
            List<Integer> startPosition = readTwoNumber(fileReader);
            Coordinate startCoordinate = new Coordinate(startPosition.get(0), startPosition.get(1));

            // Forth Line
            int stamina = readOneNumber(fileReader);

            // Fifth Line
            int numOfLodges = readOneNumber(fileReader);

            // Next N Lines
            List<Coordinate> lodgesCoordinate = new ArrayList<>();
            for (int i = 0; i < numOfLodges; i++) {
                List<Integer> lodgePosition = readTwoNumber(fileReader);
                lodgesCoordinate.add(new Coordinate(lodgePosition.get(0), lodgePosition.get(1)));
            }

            // Next H Lines
            List<List<Integer>> map = new ArrayList<>();
            for(int i = 0; i < height; i++) {
                List<Integer> oneRow = read_N_Number(fileReader, width);
                map.add(oneRow);
            }

            assert !fileReader.hasNextLine();
            fileReader.close();

            SkiProblem problem = new SkiProblem(width, height, map, startCoordinate, stamina, lodgesCoordinate);
            return new SkiProblemParserResult(problem, method);

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int readOneNumber(Scanner fileReader) {
        String numberStr = fileReader.nextLine();
        return Integer.parseInt(numberStr);
    }

    private static List<Integer> read_N_Number(Scanner fileReader, int n) {
        String mapDimension = fileReader.nextLine();
        String[] splitStrArr = mapDimension.split(" ");
        assert splitStrArr.length == n;
        return Arrays.stream(splitStrArr).map(Integer::parseInt).collect(Collectors.toList());
    }

    private static List<Integer> readTwoNumber(Scanner fileReader) {
        return read_N_Number(fileReader, 2);
    }
}
