package com.microsoft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class Main {

	public static void main(String[] args) throws Exception{
    	Translate.setClientId("client_test_id");
    	Translate.setClientSecret("5lBWg5NsJmZfzAnpkw3VvI+l6RQEJ9dO9EIg9rmrslQ=");

    	File dict = new File("dict.json");
    	InputStreamReader reader = new InputStreamReader(new FileInputStream(dict), "UTF-8");
    	JSONObject obj = new JSONObject();
    	JSONParser p = new JSONParser();
    	obj = (JSONObject) p.parse(reader);
    	reader.close();
    	
    	OutputStreamWriter sw = new OutputStreamWriter(new FileOutputStream(new File("t.json")), "UTF-8");
    	sw.write(obj.toJSONString());
    	sw.close();
    	
    	/*File f = new File("Chinese characters.txt");
		InputStreamReader r = new InputStreamReader(new FileInputStream(f), "UTF-8");
		OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(new File("dict.json")), "UTF-8");
		int i, count = 0;
		char key;
		String[] input = new String[100], output;
		char raw[] = new char[100];
		String jsonOutput;
		while((i = r.read()) != -1){
			key = (char)i;
			if(!obj.containsKey(key)){
				input[count] = String.valueOf(key);
				raw[count] = key;
				count++;
				if(count == 100){
					output = Translate.execute(input, Language.ENGLISH);
					for(int c = 0; c < output.length; c++){
						obj.put(raw[c], output[c]);
					}
					jsonOutput = obj.toJSONString();
					w.write(jsonOutput);
					w.flush();
					count = 0;
				}
			}
		}
		output = Translate.execute(input, Language.ENGLISH);
		for(int c = 0; c < output.length; c++){
			obj.put(raw[c], output[c]);
		}
		w.write(obj.toJSONString());
		w.close();
		r.close();*/
	}
	
    public static void main_text(String[] args) throws Exception {
    	Translate.setClientId("client_test_id");
    	Translate.setClientSecret("5lBWg5NsJmZfzAnpkw3VvI+l6RQEJ9dO9EIg9rmrslQ=");

        String filePath = "strings.xml";
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            NodeList strings = doc.getElementsByTagName("string");
            Node n;
            String translated;
            //loop for each employee
            for(int i=0; i<strings.getLength();i++){
            	n = strings.item(i);
            	translated = Translate.execute(n.getTextContent(), Language.AUTO_DETECT, Language.ENGLISH);
            	System.out.println(n.getTextContent() + " => " + translated);
            	n.setTextContent(translated);
            }
            //write the updated document to file or console
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("employee_updated.xml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            System.out.println("XML file updated successfully");
            
        } catch (SAXException | ParserConfigurationException | IOException | TransformerException e1) {
            e1.printStackTrace();
        }
    }
}