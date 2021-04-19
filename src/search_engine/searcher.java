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
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class searcher {
    String query;
    String path;
    ArrayList<String> wordList = new ArrayList();
    ArrayList<Integer> weightList = new ArrayList();
    ArrayList<ArrayList<String>> indexList = new ArrayList<>();
    String[] title = new String[5];

    public searcher(String path, String query) throws IOException, SAXException, ParserConfigurationException {
        this.path = path;
        this.query = query;
        this.getTitle();

        for(int i = 0; i < getTitle().length; i++){
            title[i] = getTitle()[i][0];
            //  System.out.println("title[" + i + "] = " + title[i]);

        }

    }

    public void useKkma() {
        KeywordExtractor keywordExtractor = new KeywordExtractor();
        KeywordList keywordList = keywordExtractor.extractKeyword(this.query, true);

        for (int i = 0; i < keywordList.size(); i++) {
            Keyword keyword = keywordList.get(i);
            this.wordList.add(keyword.getString());
            this.weightList.add(keyword.getCnt());
        }
    }

    public HashMap getHashMap() throws IOException, ClassNotFoundException {
        this.useKkma();
        FileInputStream fileInputStream = new FileInputStream(this.path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap hashMap = (HashMap) object;

        //출력 부분
        /*
        Iterator<String> iterator = hashMap.keySet().iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            ArrayList<String> value = (ArrayList) hashMap.get(key);
           // System.out.println(key + " -> " + value);

        }
         */
        /*
        System.out.println(hashMap.get("라면"));
        System.out.println(hashMap.get("면"));
        System.out.println(hashMap.get("분말"));
        System.out.println(hashMap.get("스프"));
         */

        return hashMap;
    }

    public void findWeight() throws IOException, ClassNotFoundException {
        HashMap hashMap = this.getHashMap();
        for (int i = 0; i < wordList.size(); i++) {
            ArrayList<String> valueList = (ArrayList<String>) hashMap.get(this.wordList.get(i));
            this.indexList.add(valueList);
/*
            for(int j = 0; j < valueList.size(); j++){
                System.out.println("valueList[" + i + "][" + j + "] = " + this.indexList.get(i).get(j));
            }

 */
        }


    }

    public ArrayList<Double> CalSim2() throws IOException, ClassNotFoundException {
        this.findWeight();
        ArrayList<Double> similarity_inner = new ArrayList<>();
        ArrayList<Double> similarity_calc = new ArrayList<>();
        ArrayList<Double> similarity = new ArrayList<>();



        for (int j = 1; j < 10; j += 2) {
            double value = 0;
            for (int i = 0; i < this.weightList.size(); i++) {
                value += (weightList.get(i)) * (Double.parseDouble(indexList.get(i).get(j)));
                // System.out.println("value[" + i + "] : " + value);

            }
            // System.out.println("value  : " + value);
            similarity_inner.add(value);  // 이 value는 inner product 용
        }



        for(int i = 1; i < 10; i+=2){
            double tmp = 0;
            double value = 0;
            for(int j = 0; j < this.weightList.size(); j++){
                tmp += Math.pow(weightList.get(j),2);
            }
            value = Math.sqrt(tmp);
            tmp = 0;
            for(int j = 0; j < this.weightList.size(); j++){
                tmp += Math.pow(Double.parseDouble(indexList.get(j).get(i)),2);
            }
            tmp = Math.sqrt(tmp);
            value *= tmp;
            similarity_calc.add(value);

        }

        for(int i = 0; i < similarity_calc.size(); i++) {
            double value = 0;
            if (similarity_calc.get(i) == 0)
                value = 0;
            else
                value = similarity_inner.get(i) / similarity_calc.get(i);

            // System.out.println("value[" + i + "] : " + value);


            // System.out.println("value  : " + value);

            similarity.add(value);
        }
/*
        for(int i = 0; i < 5; i++){
            System.out.println("similarity[" + i + "] = " + similarity.get(i));
        }
 */


        return similarity;


    }

    public String[][] getTitle() throws ParserConfigurationException, IOException, SAXException {
        String[][] data = new String[5][2];
        File file = new File("./output/collection.xml");
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
        return data;
    }

    public void checkRank() throws IOException, ClassNotFoundException {
        ArrayList<Double> similarity = this.CalSim2();
        ArrayList<Integer> rank = new ArrayList<>();
        int maxIndex = 0;
        for(int i = 0; i < 5; i++) {
            maxIndex= similarity.indexOf(Collections.max(similarity));
            rank.add(maxIndex);
            similarity.set(maxIndex, -1.0);
        }
/*
        for(int i = 0; i < 5; i++){
            System.out.println(rank.get(i));
        }

 */
        for(int i = 0; i < 3; i++){
            System.out.println((i +1) + "등 : " + this.title[rank.get(i)]);
        }

    }

}

