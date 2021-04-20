package midterm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class genSnippet {
    String path;
    String query;
    ArrayList<String> queryWord = new ArrayList<>();

    public genSnippet(String path, String query) {
        this.path = path;
        this.query = query;
    }

    public void readFile() throws FileNotFoundException {
        ArrayList<String[]> totalList = new ArrayList<>();
        File file = new File(path);
        Scanner scan = new Scanner(file);

        while(scan.hasNextLine()) {
            String line = scan.nextLine();
            totalList.add(line.split(" "));
        }


    }

    public void readQuery(){
        queryWord.add(query.split(" ");
    }



    public void count(){

    }

    public static void main(String args[]) throws FileNotFoundException {
        if (args[0].equals("-f")) {
            String path = args[1];
        }
            if(args[2].equals("-q")) {
                String query = args[3];
                genSnippet gen = new genSnippet("./", query);
                gen.readFile();

        }
    }
}
