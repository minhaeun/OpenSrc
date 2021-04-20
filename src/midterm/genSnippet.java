package midterm;

import search_engine.searcher;

import java.io.File;
import java.lang.reflect.Array;
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

    public void readFile() {
        ArrayList<String[]> all = new ArrayList<>();
        File file = new File(path);
        String text = file.toString();

    }

    public void readQuery(){
        queryWord.add(query.split(" ");
    }



    public void count(){

    }

    public static void main(String args[]) {
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
