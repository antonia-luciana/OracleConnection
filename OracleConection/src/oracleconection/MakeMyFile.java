package oracleconection;

/**
 *
 * @author lucia_000
 */
import java.io.*;

public class MakeMyFile {
    public void make(String nume1,String nume2) {
        FileReader fr = null;
        FileWriter fw = null;
        try {
            fr = new FileReader(nume1);
            fw = new FileWriter(nume2);
            int c = fr.read();
            while(c!=-1) {
                fw.write(c);
                c = fr.read();
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            close(fr);
            close(fw);
        }
    }
    public static void close(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch(IOException e) {
            System.out.println("Eroare in MakeFile");
        }
    }
}