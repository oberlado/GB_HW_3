package server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class History {
    private static PrintWriter out;

    private static String LoginHis(String login) {
        return "history/history_" + login + ".txt";
    }

    public static void start(String login) {
        try {
            out = new PrintWriter(new FileOutputStream(LoginHis(login), true), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        if (out != null) {
            out.close();
        }
    }

    public static void writeLine(String msg) {
        out.println(msg);
    }

    public static String getLastHist(String login) {
        if (!Files.exists(Paths.get(getLastHist(login)))) {
            return "";
        }
        StringBuilder sd = new StringBuilder();
        try {
            List<String>histLine = Files.readAllLines(Paths.get(getLastHist(login)));
            int startPos = 0;
            if (histLine.size()>100){startPos = histLine.size()-100;
        }
            for (int i = startPos;i<histLine.size();i++){
            sd.append(histLine.get(i)).append(System.lineSeparator());}
        } catch (IOException e) {
            e.printStackTrace();
        }return sd.toString();
    }
}