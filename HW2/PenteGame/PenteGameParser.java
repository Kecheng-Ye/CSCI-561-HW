package PenteGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PenteGameParser {
    public static class PenteGameParserResult{
        public final PenteGame game;
        public final PenteGameState state;

        public PenteGameParserResult(final PenteGame game, final PenteGameState state) {
            this.game = game;
            this.state = state;
        }
    }

    public static PenteGameParserResult parseFromFile(String filePath) {
        try {
            File myObj = new File(filePath);
            Scanner fileReader = new Scanner(myObj);

            // First Line
            String playerString = fileReader.nextLine();
            final PenteGamePlayer player = PenteGamePlayer.parseFromStr(playerString);

            // Second Line
            float remainningSec = readOneFloat(fileReader);

            // Third Line
            List<Integer> capturePieces = readTwoNumber(fileReader);
            final int whiteCapturePieces = capturePieces.get(0);
            final int blackCapturePieces = capturePieces.get(1);
            assert (whiteCapturePieces % 2 == 0) && (blackCapturePieces % 2 == 0);

            // Forth Line
            final PenteGameBoard board = PenteGameBoard.parseFromFile(fileReader);

            assert !fileReader.hasNextLine();
            fileReader.close();

            PenteGame game = new PenteGame(player);
            PenteGameState state = PenteGameState.reconstructFromBoardAndCaptures(board, whiteCapturePieces, blackCapturePieces);
            return new PenteGameParserResult(game, state);

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static float readOneFloat(Scanner fileReader) {
        String numberStr = fileReader.nextLine();
        return Float.parseFloat(numberStr);
    }

    private static int readOneNumber(Scanner fileReader) {
        String numberStr = fileReader.nextLine();
        return Integer.parseInt(numberStr);
    }

    private static List<Integer> read_N_Number(Scanner fileReader, int n) {
        String inputNumStr = fileReader.nextLine().strip();
        String[] splitStrArr = inputNumStr.split(",");
        assert splitStrArr.length == n;
        return Arrays.stream(splitStrArr).map(numStr -> Integer.parseInt(numStr.strip())).collect(Collectors.toList());
    }

    private static List<Integer> readTwoNumber(Scanner fileReader) {
        return read_N_Number(fileReader, 2);
    }

}
