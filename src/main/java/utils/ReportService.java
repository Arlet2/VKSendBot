package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;

public class ReportService {
    private final static String FILE_NAME = "report";

    private StringBuffer stringBuffer;

    public void createNewReport() {
        stringBuffer = new StringBuffer();
    }

    public void addMessageToReport(String msg) {
        stringBuffer.append(msg).append("\n");
    }

    public void finalReport() {
        try {
            String time = LocalDateTime.now().format(
                    new DateTimeFormatterBuilder()
                            .appendPattern("dd.MM.yy_(HH.mm)")
                            .toFormatter());

            FileWriter writer = new FileWriter(FILE_NAME + "_" + time + ".report");

            writer.write(stringBuffer.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка с сохранением отчёта. Отчёт будет напечатан здесь");
            System.out.println(stringBuffer.toString());
        }
    }
}
