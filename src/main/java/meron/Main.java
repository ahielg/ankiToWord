package meron;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class Main {
    static FileWriter fw;
    public static List<String> listFiles(String path) {
        String[] pathNames;

        // Creates a new File instance by converting the given pathname string
        // into an abstract pathname
        File f = new File(path);

        FilenameFilter filter = (f1, name) -> name.endsWith(".txt") &&  !name.endsWith("-new.txt");

        // Populates the array with names of files and directories
        pathNames = f.list(filter);


        // For each pathname in the pathNames array
        assert pathNames != null;
        for (String pathname : pathNames) {
            // Print the names of files and directories
            System.out.println(pathname);
        }
        return Arrays.asList(pathNames);

    }
    public static void main(String[] args) throws IOException {
        String path = "c:\\My\\dev\\";
        List<String> files = listFiles(path);
        //if (1==1) return;
        //String file = "c:\\My\\dev\\01.txt";
        for (String file : files) {
            extractB(path+file);
        }
        //extractB(file);
    }

    private static void extractB(String file) throws IOException {
        File inputFile = new File(file);
        String line = "";
        //
        String outputFile = inputFile.getParent() + "\\" + inputFile.getName().replace(".", "-new.");
        fw = new FileWriter(outputFile);
        System.out.println("Output file: "+ outputFile);

        BufferedReader in = new BufferedReader(new FileReader(inputFile));
        //goto the parasha start line
        while (line != null) {
            line = printFile(in);
            if (line != null) {
                ignoreLines(in);
            }
        }
        fw.close();
    }

    private static String printFile(BufferedReader in) throws IOException {
        String line="";
        while (line != null && !line.trim().startsWith("עיקר תוי\"ט") && !line.trim().startsWith("רמב\"ם")) {
            //System.out.println(line);

            fw.write(line.replaceAll("\\s*\\{[^\\}]*\\}\\s*", " ") +System.lineSeparator());
            //fw.write(line+System.lineSeparator());
            line = in.readLine();
        }
        return line;
    }

    private static String ignoreLines(BufferedReader in) throws IOException {
        String line = "";
        while (line != null && !(line.trim().startsWith("פרק ") && line.trim().length() < 20)) {
            line = in.readLine();
        }
        if (line != null) {
          //  System.out.println(line);
            fw.write(line);
            fw.write(System.lineSeparator());
        }


        return line;
    }
}
