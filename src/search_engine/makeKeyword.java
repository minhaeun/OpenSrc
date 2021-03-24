package search_engine;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class makeKeyword {
    String path;
    String[][] data = new String[5][2];

    //int doc_id = 0;
    public makeKeyword(String path) {
        this.path = path;
    }

    public void getData() throws ParserConfigurationException, IOException, SAXException {
        //xml 문서 파일
        File file = new File(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);

        //root구하기
        Element root = document.getDocumentElement();

        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String nodeName = element.getNodeName();
                //  System.out.println("nodeName: " + nodeName);
                if (nodeName.equals("doc")) {
                    NodeList children2 = element.getChildNodes();
                    for (int j = 0; j < children2.getLength(); j++) {
                        Node node2 = children2.item(j);
                        if (node2.getNodeType() == Node.ELEMENT_NODE) {
                            Element element2 = (Element) node2;
                            String nodeName2 = element2.getNodeName();
                            //  System.out.println("node name2: " + nodeName2);
                            //   System.out.println("node attribute: " + element2.getTextContent());
                            if (nodeName2.equals("title")) {
                                data[i][j] = element2.getTextContent();

                            }
                            if (nodeName2.equals("body")) {
                                data[i][j] = element2.getTextContent();
                            }

                        }
                    }
                }
            }


        }
/*
        for(int i = 0;i<5;i++){
            for(int j = 0; j < 2; j++){
                System.out.println("data["+ i + "]["+j+"] = " + data[i][j]);
            }
        }
        */

        //System.out.println(Arrays.toString(data));
    }

    public void makeKkma() throws IOException, SAXException, ParserConfigurationException {
        this.getData();
        KeywordExtractor ke = new KeywordExtractor();
        for (int i = 0; i < 5; i++) {
            String tmp = "";
            KeywordList kl = ke.extractKeyword(data[i][1], true);
            for (int j = 0; j < kl.size(); j++){
                Keyword kwrd = kl.get(j);
                String str = kwrd.getString() + ":" + kwrd.getCnt() + "#";
                tmp = tmp + str;
            }
            data[i][1] = tmp;
           // System.out.println(tmp);
        }

    }



    public void makeIndex() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        this.makeKkma();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document document = docBuilder.newDocument();

        Element docs = document.createElement("docs");
        document.appendChild(docs);

        KeywordExtractor ke = new KeywordExtractor();


        for(int i = 0; i < 5; i++){
            Element doc = document.createElement("doc");
            docs.appendChild(doc);
            doc.setAttribute("id", String.valueOf(i));

            Element title = document.createElement("title");
            title.appendChild(document.createTextNode(data[i][0]));
            doc.appendChild(title);

            Element body = document.createElement("body");

            body.appendChild(document.createTextNode(data[i][1]));
            doc.appendChild(body);


            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(new File("output/index.xml")));
            transformer.transform(source, result);
        }
    }
}


