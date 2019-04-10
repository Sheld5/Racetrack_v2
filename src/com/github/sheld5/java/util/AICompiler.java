package util;

import main.CarPanel;
import model.DriverAI;
import org.joor.Reflect;

import java.io.*;


public class AICompiler {

    public DriverAI compile(CarPanel carPanel) throws IOException {
        try {
            InputStream in = new FileInputStream(carPanel.getAiFile());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            if (!line.equals("package java.model;")) {
                builder.append("package java.model;\n\n");
            }
            while (line != null) {
                builder.append(line).append("\n");
                line = reader.readLine();
            }
            String content = builder.toString();

            return Reflect.compile("java.model." + carPanel.getAiName(), content).create().get();
        } catch (IOException e) {
             System.out.println("Error while compiling AI " + carPanel.getAiName());
             System.out.println("AI file: " + carPanel.getAiFile());
             throw e;
        }
    }

}
