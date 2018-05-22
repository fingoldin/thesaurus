import java.util.Scanner;
import java.io.*;

class FileReadPlz
{
    public static String read(String path)
    {
        try {
            File f = new File(path);
            FileReader r = new FileReader(f);

            int l = (int)f.length();

            char[] buf = new char[l];

            r.read(buf, 0, l);

            r.close();

            return String.valueOf(buf);
        }
        catch(Exception e) {
            e.printStackTrace();
        
            return "";
        }
    }
}
