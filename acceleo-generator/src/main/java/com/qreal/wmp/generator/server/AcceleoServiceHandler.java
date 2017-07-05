package com.qreal.wmp.generator.server;

import com.qreal.wmp.generator.model.Model;
import com.qreal.wmp.generator.model.Node;
import com.qreal.wmp.generator.model.NodeProperty;
import com.qreal.wmp.generator.model.Palette;
import com.qreal.wmp.thrift.gen.*;
import cs.ualberta.launcher.Launcher;
import edu.ca.ualberta.ssrg.chaintracker.acceleo.main.AcceleoLauncherException;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@Transactional
public class AcceleoServiceHandler implements AcceleoServiceThrift.Iface {
    @Override
    public void createMetamodel(TPalette tPalette) throws TAborted {
        
        Palette palette = new Palette(tPalette);
        File meta = new File("metamodels/" + palette.getName() + ".ecore");
        PrintWriter out = null;
        try {
            meta.createNewFile();
            out = new PrintWriter(meta.getAbsoluteFile());
            if (!meta.exists()) {
                meta.createNewFile();
            }
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<ecore:EPackage xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:xsi=" +
                    "\"http://www.w3.org/2001/XMLSchema-instance\"");
            out.println("xmlns:ecore=\"http://www.eclipse.org/emf/2002/Ecore\" name=\"" +  palette.getName() +
                    "\" nsURI=\"http://wmp.dsm\" nsPrefix=\"wmp.dsm\">");
            out.println("<eClassifiers xsi:type=\"ecore:EClass\" name=\"Node\">");
            out.println("\t<eStructuralFeatures xsi:type=\"ecore:EReference\" name=\"to\" eType=\"#//Node\"/>");
            out.println("\t<eStructuralFeatures xsi:type=\"ecore:EAttribute\" name=\"name\" eType=\"ecore:EDataType " +
                    "http://www.eclipse.org/emf/2002/Ecore#//EString\"/>");
            out.println("  </eClassifiers>");
            for (Node node: palette.getNodes()) {
                out.println("<eClassifiers xsi:type=\"ecore:EClass\" name=\"" + node.getName() +
                        "\" eSuperTypes=\"#//Node\">");
                for (NodeProperty property: node.getProperties()) {
                    out.println("\t<eStructuralFeatures xsi:type=\"ecore:EAttribute\" name=\"" + property.getName() +
                            "\" eType=\"ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//E" + property.getType()
                            + "\"");
                    out.println("defaultValueLiteral=\"" + property.getValue() + "\"/>");
                }
                out.println("</eClassifiers>");
            }
            out.println("<eClassifiers xsi:type=\"ecore:EClass\" name=\"Model\">");
            out.println("\t<eStructuralFeatures xsi:type=\"ecore:EReference\" name=\"nodes\" upperBound=\"-1\"");
            out.println("\t\teType=\"#//Node\"/>");
            out.println("</eClassifiers>");
            out.println("</ecore:EPackage> ");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Override
    public void generate(TModel tModel) throws TAborted {
        Model model = new Model(tModel);
        File meta = new File("models/" + model.getName() + ".xmi");
        PrintWriter out = null;
        try {
            meta.createNewFile();
            out = new PrintWriter(meta.getAbsoluteFile());
            if (!meta.exists()) {
                meta.createNewFile();
            }
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<wmp.dsm:Model xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" " +
                    "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:wmp.dsm=\"http://wmp.dsm\">");
            int ind = 1;
            for (Node node : model.getNodes()) {
                out.print("\t<nodes xsi:type=\"wmp.dsm:" + node.getName() + "\" name=\"" + node.getName() + "\" ");
                for (NodeProperty property : node.getProperties()) {
                    out.print(property.getName() + "=\"" + property.getValue() + "\"");
                }
                if (ind < model.getNodes().size()) {
                    out.println("to=\"//@nodes." + ind + "\"></nodes>");
                }
                else {
                    out.println("/>");
                }
                ind++;
            }
            out.print("</wmp.dsm:Model>");
            out.flush();

            Launcher launcher = new Launcher();

            String metamodelName = model.getMetamodelName();

            launcher.runAcceleo("metamodels/" + metamodelName + ".ecore", metamodelName,
                    "models/" + model.getName() + ".xmi",
                    "transformations/M2T/generateRobots.mtl", "gen/");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (AcceleoLauncherException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
