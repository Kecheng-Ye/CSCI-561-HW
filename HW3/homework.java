import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class homework {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("^[A-Z]\\w*");
        Matcher m = p.matcher("Abcbcbc");
        if (m.find()) {
            System.out.println("true");
        }
    }
}
