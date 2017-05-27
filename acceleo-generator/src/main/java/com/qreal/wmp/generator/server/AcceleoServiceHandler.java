package com.qreal.wmp.generator.server;

import com.qreal.wmp.generator.model.Node;
import com.qreal.wmp.generator.model.NodeProperty;
import com.qreal.wmp.generator.model.Palette;
import com.qreal.wmp.thrift.gen.*;
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
            out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" +
                    "<ecore:EPackage xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:xsi=" +
                    "\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "xmlns:ecore=\"http://www.eclipse.org/emf/2002/Ecore\" name=\"" +  palette.getName() +
                    "\" nsURI=\"http://wmp.dsm\" nsPrefix=\"wmp.dsm\">\n" +
                    "<eClassifiers xsi:type=\"ecore:EClass\" name=\"Node\">\n" +
                    "    <eStructuralFeatures xsi:type=\"ecore:EReference\" name=\"to\" eType=\"#//Node\"/>\n" +
                    "    <eStructuralFeatures xsi:type=\"ecore:EReference\" name=\"from\" eType=\"#//Node\"/>\n" +
                    "    <eStructuralFeatures xsi:type=\"ecore:EAttribute\" name=\"name\" eType=\"ecore:EDataType " +
                    "http://www.eclipse.org/emf/2002/Ecore#//EString\"/>\n" +
                    "  </eClassifiers>\n");
            for (Node node: palette.getNodes()) {
                out.print("<eClassifiers xsi:type=\"ecore:EClass\" name=\"" + node.getName() +
                        "\" eSuperTypes=\"#//Node\">\n");
                for (NodeProperty property: node.getProperties()) {
                    out.print("<eStructuralFeatures xsi:type=\"ecore:EAttribute\" name=\"" + property.getName() +
                            "\" eType=\"ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//E" + property.getType()
                            + "\"\n" +
                            "defaultValueLiteral=\"" + property.getValue() + "\"/>");
                }
                out.print("</eClassifiers>\n");
            }
            out.print("<eClassifiers xsi:type=\"ecore:EClass\" name=\"Model\">\n" +
                    "    <eStructuralFeatures xsi:type=\"ecore:EReference\" name=\"nodes\" upperBound=\"-1\"\n" +
                    "        eType=\"#//Node\"/>\n" +
                    "  </eClassifiers>\n" +
                    "</ecore:EPackage> ");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
