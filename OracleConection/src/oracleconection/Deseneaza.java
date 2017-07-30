/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleconection;

/**
 *
 * @author lucia_000
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lucia_000
 */
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import oracleconection.InterfaceMainPage;
import static oracleconection.InterfaceMainPage.myTables;
import static oracleconection.OracleConection.foreignKey;
import static oracleconection.OracleConection.referenceTable;
import static oracleconection.OracleConection.referencedBy;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;

public class Deseneaza {

    public static void main(String[] args) {

    }

    public void start() {

        GraphModel model = new DefaultGraphModel();
        JGraph graph = new JGraph(model);
       
        graph.setCloneable(true);
        graph.setJumpToDefaultPort(true);

        int y = 20;
        int x = 20;
        DefaultGraphCell[] cells = new DefaultGraphCell[myTables.size()+referenceTable.size()];

        for (int i = 0; i < myTables.size(); i++) {
            if (i < myTables.size() / 2) {
                cells[i] = createVertex(myTables.get(i), 20, y + 80, 100, 20, Color.white, false);
                y += 80;
            } else {
                cells[i] = createVertex(myTables.get(i), x + 140, 40, 100, 20, null, false);
                x += 140;

            }
        }
        
       int j = 0;
       for (int i = 0; i < myTables.size() && j<referenceTable.size(); i++) {
            System.out.println(myTables.get(i)+" se compara cu "+referenceTable.get(j));
            while (j<referenceTable.size() && myTables.get(i).equals(referenceTable.get(j))) {
                System.out.println("Sunt egale");
                DefaultEdge edge = new DefaultEdge(foreignKey.get(j));
                System.out.println("Se creaza o muchie etichetata cu "+foreignKey.get(j));
                for (int k = 0; k < myTables.size() && !(myTables.get(k).equals(referencedBy.get(j))); k++) {
                    System.out.println("Sursa sagetii "+cells[i].getChildAt(0));
                    edge.setSource(cells[i].getChildAt(0));
                    System.out.println("Tinta sagetii "+cells[k].getChildAt(0));
                    edge.setTarget(cells[k].getChildAt(0));
                    cells[j+myTables.size()] = edge;
                    int arrow = GraphConstants.ARROW_CLASSIC;
                    GraphConstants.setLineEnd(edge.getAttributes(), arrow);
                    GraphConstants.setEndFill(edge.getAttributes(), true);
                }
                j++;
            } 

        }
        
       
        DefaultEdge edge1 = new DefaultEdge();
        
        graph.getGraphLayoutCache().insert(cells);

        // Show in Frame
        JFrame frame = new JFrame();
        frame.getContentPane().add(new JScrollPane(graph));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static DefaultGraphCell createVertex(String name, double x,
            double y, double w, double h, Color bg, boolean raised) {

        // Create vertex with the given name
        DefaultGraphCell cell = new DefaultGraphCell(name);

        // Set bounds
        GraphConstants.setBounds(cell.getAttributes(),
                new Rectangle2D.Double(x, y, w, h));

        // Set fill color
        if (bg != null) {
            GraphConstants.setGradientColor(cell.getAttributes(), bg);
            GraphConstants.setOpaque(cell.getAttributes(), true);
        }

        // Set raised border
        if (raised) {
            GraphConstants.setBorder(cell.getAttributes(),
                    BorderFactory.createRaisedBevelBorder());
        } else // Set black border
        {
            GraphConstants.setBorderColor(cell.getAttributes(),
                    Color.black);
        }
        // Add a Floating Port
        cell.addPort();

        return cell;
    }
}
