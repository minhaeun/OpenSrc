package search_engine;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class CreateXml {
    public void HtmltoXml() {
        ReadFile readFile = new ReadFile();
        File[] fileList = readFile.OpenFile();
        if(fileList.length > 0){
            try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document newDoc = docBuilder.newDocument();
            Element docs = newDoc.createElement("docs");
            newDoc.appendChild(docs);

            for(int i = 0; i < fileList.length; i++) {

                org.jsoup.nodes.Document document = Jsoup.parse(fileList[i], "UTF-8");
                org.jsoup.select.Elements titles = document.select("title");
                org.jsoup.select.Elements p = document.select("p");

                Element doc = newDoc.createElement("doc");
                docs.appendChild(doc);
                doc.setAttribute("id", String.valueOf(i));

                Element title = newDoc.createElement("title");
                title.appendChild(newDoc.createTextNode(titles.text()));
                doc.appendChild(title);

                Element body = newDoc.createElement("body");
                body.appendChild(newDoc.createTextNode(p.text()));
                doc.appendChild(body);


                TransformerFactory transformerFactory = TransformerFactory.newInstance();

                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                DOMSource source = new DOMSource(newDoc);
                StreamResult result = new StreamResult(new FileOutputStream(new File("output/collection.xml")));
                transformer.transform(source, result);
            }

                } catch (Exception e) {
                    e.printStackTrace();
                }

        }

    }

}
