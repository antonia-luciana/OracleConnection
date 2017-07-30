/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleconection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Scrie {

    private File outputFile;
    private BufferedWriter bw;
   // @SuppressWarnings("resource")
    

    public void initializare(String fileName) throws FileNotFoundException {
        outputFile = new File("C:\\Users\\lucia_000\\Documents\\NetBeansProjects\\OracleConection\\"+fileName+".html");
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Scrie.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void maresteRaport(String fileName) throws IOException {
        File input = new File(fileName+".html");
        Document doc = Jsoup.parse(input, "UTF-8");

        Elements links = doc.select("BODY");

       // bw.append(fileName.toString());
        bw.append(links.toString());
    }
    
    public void finalizare() throws IOException{
        bw.close();
    }
    
    public static void main(String args[]) throws FileNotFoundException, IOException{
        Scrie mf = new Scrie();
        mf.initializare("rez");
        mf.maresteRaport("rez");
        //mf.maresteRaport("test3");
        mf.finalizare();
        
    }
}
