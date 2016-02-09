package csci599;

import java.io.File;
import org.apache.tika.Tika;

// trying out code on Tika in Action pg 33
public class App {
    public static void main(String[] args) throws Exception {
        Tika tika = new Tika();
        for (String file : args) {
            System.out.println(tika.parseToString(new File(file)));
        }
    }
}
