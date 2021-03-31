package search_engine;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class kuir {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, TransformerException, ClassNotFoundException {
        if (args[0].equals("-c")) {
            makeCollection mC = new makeCollection(args[1]);
            mC.HtmltoXml();
        } else if (args[0].equals("-k")) {
            makeKeyword mK = new makeKeyword("./output/" + args[1]);
            mK.makeIndex();
        } else if (args[0].equals("-i")){
            indexer indexer = new indexer("./output/" + args[1]);
            indexer.printHashmap();
        }
        else {
            System.out.println("no");
        }




    }
}