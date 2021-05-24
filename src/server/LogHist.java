package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class LogHist {
    private File msgHist;

    public void FileHist(ClientHandler client){
    this.msgHist = new File("src/message_hist/history.txt");
    if(!this.msgHist.exists()){
        try {
            this.msgHist.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    printLastLines(client);
}
    private void printLastLines(ClientHandler client) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(msgHist))){
            String line;
            while((line = in.readLine()) != null){
                lines.add(line);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        for (int i = lines.size() - 1; (i >= 0) && (i >= lines.size() - 100); i--) {
            client.sendMsg(lines.get(i));
        }
    }
}

