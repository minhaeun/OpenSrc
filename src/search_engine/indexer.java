package search_engine;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class indexer {
    String path;
    String[] body = new String[5];
    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
    ArrayList<ArrayList<String>> weightList = new ArrayList<>();

    public indexer(String path){
        this.path = path;
    }
    public void getBody() throws ParserConfigurationException, IOException, SAXException {
        File file = new File(path);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(file);

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
                              //System.out.println("node name2: " + nodeName2);
                               //System.out.println("node attribute: " + element2.getTextContent());
                            if (nodeName2.equals("body")) {
                                body[i] = element2.getTextContent();
                            }

                        }
                    }
                }
            }
        }
/*
        for(int i = 0;i<5;i++){
                System.out.println("body["+ i + "] = " + body[i]);
        }
*/

      // System.out.println(Arrays.toString(data));


    }

    public void getData() throws IOException, SAXException, ParserConfigurationException {
        this.getBody();
        String[][] str = new String[5][];
        String[] set;



        for(int i = 0; i < this.body.length; i++) {

            str[i] = this.body[i].split("#");
        }
        for(int i = 0; i < str.length; i++){
            ArrayList<String> setList = new ArrayList<>();
            for(int j =0; j < str[i].length; j++){
                set = str[i][j].split(":");
                setList.add(set[0]);
                setList.add(set[1]);
              //  System.out.println("set[0] : " + set[0]);
               // System.out.println("set[1] : " + set[1]);

            }
            this.dataList.add(setList);
        }
        for(int i = 0;i < this.dataList.size(); i++){
            for(int j = 0; j<this.dataList.get(i).size(); j += 2){
                //System.out.println(this.dataList.get(i).get(j));
                this.weightList.add(tf_idf(this.dataList.get(i).get(j)));

            }
        }




    }

    public ArrayList<String> tf_idf(String word) {
        int doc_count = 0;
        double weight;
        ArrayList<String> index = new ArrayList<>();
        index.add(word);
        int[] frequency = new int[5];
        for(int i = 0; i < this.dataList.size(); i++) {
            if (this.dataList.get(i).contains(word)) {
                for(int j = 0; j < dataList.get(i).size(); j++) {
                    if(j % 2 == 0 && this.dataList.get(i).get(j).equals(word)) {
                        doc_count++;
                        //frequency[i] = this.dataList.get(i).indexOf(word);
                        //  System.out.println("frequency[" + i + "] = " + frequency[i]);
                        frequency[i] = Integer.parseInt(this.dataList.get(i).get(j + 1));
                        //System.out.println("frequency[" + i + "] = " + frequency[i]);

                    }
                }


            } else {
                frequency[i] = 0;
            }

        }
        for(int i = 0; i < this.dataList.size(); i++){
            if(doc_count == 0) weight = 0;
            else {
                weight = frequency[i] * Math.log((double)5 / (double)doc_count);
                weight = Math.round((weight * 100)) / 100.0;
            }
            index.add(String.valueOf(i));
            index.add(String.valueOf(weight));


        }

/*
    for(int i = 0; i < index.size();i++){
        System.out.println("index["+i+"] = "+ index.get(i));
    }
*/
    return index;
    }

    public void makeHashmap() throws IOException, ParserConfigurationException, SAXException {
        this.getData();
        LinkedHashSet<ArrayList<String>> linkedHashSet = new LinkedHashSet<>(this.weightList);
        ArrayList<ArrayList<String>> finalList = new ArrayList<>(linkedHashSet);

        /*
        for(int i = 0; i < finalList.size(); i++){
            for(int j = 0; j < finalList.get(i).size();j++){
                System.out.println("finalList["+ i +"]["+j+"] = "+ finalList.get(i).get(j));
            }
        }

*/

        FileOutputStream fileStream = new FileOutputStream("./output/index.post");

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);

        HashMap hashMap = new HashMap();

        for(int i = 0; i < finalList.size(); i++){
            ArrayList<String> valueList = new ArrayList<>();
            for(int j = 1; j < finalList.get(i).size(); j++) {
                valueList.add(finalList.get(i).get(j));
            }
            hashMap.put(finalList.get(i).get(0), valueList);
        }

        objectOutputStream.writeObject(hashMap);

        objectOutputStream.close();


    }

    public void printHashmap() throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException {
        this.makeHashmap();
        FileInputStream fileInputStream = new FileInputStream("./output/index.post");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap hashMap = (HashMap) object;
        Iterator<String> iterator = hashMap.keySet().iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            ArrayList<String> value = (ArrayList) hashMap.get(key);
            System.out.println(key + " -> " + value);
        }

    }



}
