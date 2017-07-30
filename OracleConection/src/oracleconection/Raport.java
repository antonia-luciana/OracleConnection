/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleconection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author lucia_000
 */
public class Raport {
   
    static Integer nrClients=0;
    ArrayList<String> valids = new ArrayList<String>();

    public void add(String word) {
        valids.add(word);
    }

    public ArrayList<String> getValidWords() {
        return valids;
    }

    public void generateHTML(String fileName,String select,ArrayList<List> tabel) {
        List rand = new ArrayList();
        
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        Template t = ve.getTemplate("Report2.vm");

        VelocityContext context = new VelocityContext();
        context.put("select", select);
        context.put("tabel", tabel);
      
        
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
       // nrClients++;
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName+".html"));
            out.write(writer.toString());
            out.close();
        } catch (IOException e) {
            System.out.println("Exception");
        }
    }
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

