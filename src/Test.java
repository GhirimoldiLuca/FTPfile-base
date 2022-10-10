import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException{
        FileWriter fw = new FileWriter(new File("text.txt"));
        fw.append("hello");
        fw.append("hello");
        fw.append("hello");
        fw.close();
    }
}
