package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatterBuilder;

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
            String time = LocalTime.now().format(
                    new DateTimeFormatterBuilder()
                            .appendPattern("H-m-s")
                            .toFormatter());

            FileWriter writer = new FileWriter(FILE_NAME + "_" + time + ".report");

            writer.write(stringBuffer.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR WITH REPORT SAVING. REPORT WILL BE PRINT HERE");
            System.out.println(stringBuffer.toString());
        }
    }
}
