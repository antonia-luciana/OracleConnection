/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleconection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.nashorn.internal.objects.NativeString.toUpperCase;
import static oracleconection.InterfaceMainPage.updateRaport;

/**
 *
 * @author lucia_000
 */
public class OracleConection {

    /**
     * @param args the command line arguments
     */
    public static ArrayList referenceTable= new ArrayList();
    public static ArrayList referencedBy= new ArrayList();
    public static ArrayList foreignKey= new ArrayList();
    
    public static Connection conection = null;

    public static boolean deschidere(String username, char password[]) {

        try {
            String driverName = "oracle.jdbc.driver.OracleDriver";
            Class.forName(driverName);
            String serverName = "BU";
            String serverPort = "1521";
            String sid = "XE";
            String url = "jdbc:oracle:thin:@" + serverName + ":" + serverPort + ":" + sid;

            String myPassword = String.valueOf(password);
            for (int i = 0; i < password.length; i++) {
                password[i] = 0;
            }
            
            conection = DriverManager.getConnection(url, username, myPassword);
            System.out.println("Succesfully connected to the database!");

        } catch (Exception e) {
            System.out.println("Eroare la conectare cu baza de date");
            return false;
        }
        return true;
    }

   

    public static void getMetadata() {
        try {
            Statement st = conection.createStatement();
            ResultSet rs = st.executeQuery("Select object_name , object_type from user_objects");
            while (rs.next()) {
                String objectName = rs.getString(1);
                String objectType = rs.getString(2);
                System.out.println(objectName + " " + objectType);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Eroare la scoaterea datelor");
        }
    }

   

    public static ArrayList< String> afiseaza(String colons, String table, String wishes) {
        ArrayList< String> rezultat = new ArrayList<>();
        try {
            Statement st = conection.createStatement();

            int nCols = 0;
            ResultSet rs = st.executeQuery(" select max(column_id) from all_tab_columns where table_name = '" + toUpperCase(table) + "'");
            if (rs.next()) {
                String nr = rs.getString(1);
                if (nr == null) {
                    throw new Exception("Problem with the tabel");
                }
                nCols = Integer.parseInt(nr);
                // System.out.println(nCols);
            }
            rs.close();

            String line1 = new String();

            ArrayList numeColoane = new ArrayList();  //!!

            ResultSet rs3 = st.executeQuery(" select column_name from all_tab_columns where table_name = '" + toUpperCase(table) + "'");
            while (rs3.next()) {
                String valoare = rs3.getString(1);
                // System.out.println(valoare+',');
                numeColoane.add(valoare);
                line1 = line1 + "," + valoare;
            }
            rezultat.add(line1);
            rs3.close();

            //System.out.println(numeColoane);
            
            ArrayList<List> tabel = new ArrayList<List>();
            String interogare = " SELECT " + colons + " FROM " + table + " " + wishes;
            String[] coloane = colons.split(",");
            System.out.println("prima coloana:"+coloane[0]);
            ResultSet rs2 = st.executeQuery(interogare);
            int index=0;
            while (rs2.next()) {
                String line = new String();
                ArrayList rand = new ArrayList();
                if(colons.contains("*")){
                for (int i = 0; i < nCols; i++) {   
                String valoare = rs2.getString(i + 1);
                line = line + "," + valoare;
                rand.add(valoare);
                }
                }
                else {
                    index=0;
                    System.out.println("Avem coloane specificate!!"+colons);
                    for (int i = 0; i < nCols && index<coloane.length; i++) {   
                        System.out.println("de comparat: "+numeColoane.get(i)+" && "+coloane[index].toUpperCase());
                        //System.out.println();
                    if(numeColoane.get(i).equals(coloane[index].toUpperCase())){
                    System.out.println("Sunt egale!!!");
                    String valoare = rs2.getString(index + 1);
                    line = line + "," + valoare;
                    rand.add(valoare);
                    index++;
                     System.out.println("Am scos: "+valoare+ " pt indexul "+index);
                    }
                    else {
                        System.out.println("Nu sunt egale!!!");
                        line=line+","+" ";
                    rand.add(" ");
                    }
                    }
                     System.out.println(line);
                }
                tabel.add(rand);
                rezultat.add(line);
                
            }
            
            rs2.close();
            Raport raport = new Raport();
            raport.generateHTML("raportPartial",interogare, tabel);
            updateRaport.maresteRaport("raportPartial");
            
           // System.out.println(tabel);
        } catch (Exception e) {
            System.out.println("Eroare la scoaterea datelor");
        }
        
        return rezultat;
    }


    static void  afisareRezultat(List<String> values){
        for( String lista : values ){
            String[] linePieces = lista.split(",");
            for(int i=0;i<linePieces.length ; i++)
                System.out.print( linePieces[i]+" " );
            System.out.println();
        }
             
    }
    public static boolean insert(String comanda){
        try {
            Statement st = conection.createStatement();
            int nCols=0;
            ResultSet rs = st.executeQuery(comanda);
            rs.close(); 
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OracleConection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
        public static boolean delete(String comanda){
        try {
            Statement st = conection.createStatement();
            int nCols=0;
            ResultSet rs = st.executeQuery(comanda);
            rs.close(); 
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OracleConection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static void tableReferencies() {
        
        String comanda = "select a.table_name , b.table_name ,b.constraint_name from user_constraints a, user_constraints b where b.constraint_type = 'R'and a.constraint_name = b.r_constraint_name order by a.table_name";
        try {
            Statement st = conection.createStatement();
            System.out.println("Referenced table ----  Referenced  by ------ Foreign key");
            int nCols = 0;
            ResultSet rs = st.executeQuery(comanda);
            while (rs.next()) {
                String referencedTable = rs.getString(1);
                referenceTable.add(referencedTable);
                String referenceBy = rs.getString(2);
                referencedBy.add(referenceBy);
                String foreign_Key = rs.getString(3);
                foreignKey.add(foreign_Key);
                System.out.println( referencedTable + " " + referenceBy + " "+ foreign_Key );
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(OracleConection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(OracleConection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
