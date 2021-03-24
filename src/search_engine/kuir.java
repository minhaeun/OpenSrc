package search_engine;

<<<<<<< HEAD
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class kuir {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        if (args[0].equals("-c")) {
            makeCollection mC = new makeCollection(args[1]);
            mC.HtmltoXml();
        }
        else if(args[0].equals("-k")){
            makeKeyword mK = new makeKeyword("./output/"+ args[1]);
            mK.makeIndex();
        }
        else{
            System.out.println("no");
        }

=======
public class kuir {
    public static void main(String[] args){
        makeCollection mC = new makeCollection();
        mC.HtmltoXml();
>>>>>>> 98d089e721270e770de4c7845abcf1a3ce7348b0
    }
}