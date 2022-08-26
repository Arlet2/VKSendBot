package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

public class ReportService {
    private final String FILE_NAME = "report";

    private StringBuffer stringBuffer;

    public void createNewReport() {
        stringBuffer = new StringBuffer();
    }

    public void addMessageToReport(String msg) {
        stringBuffer.append(msg).append("\n");
    }

    public void finalReport() {
        try {
            FileWriter writer = new FileWriter(FILE_NAME+"-"+LocalTime.now().toString());

            writer.write(stringBuffer.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR WITH REPORT SAVING");
        }
    }
}
