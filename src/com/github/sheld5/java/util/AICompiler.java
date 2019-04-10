package util;

import main.CarPanel;
import model.DriverAI;
import org.joor.Reflect;

import java.io.*;

/**
 * compiles external AI code
 */
public class AICompiler {

    /**
     * compiles the AI of the given CarPanel if it has not been compiled yet and returns an instance of it
     * uses the jOOR library
     * @param carPanel
     * @return
     * @throws IOException
     */
    public DriverAI compile(CarPanel carPanel) throws IOException {
        try {
            InputStream in = new FileInputStream(carPanel.getAiFile());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            if (!line.equals("package model;")) {
                builder.append("package model;\n\n");
            }
            while (line != null) {
                builder.append(line).append("\n");
                line = reader.readLine();
            }
            String content = builder.toString();

            return Reflect.compile("model." + carPanel.getAiName(), content).create().get();
        } catch (IOException e) {
             System.out.println("Error while compiling AI " + carPanel.getAiName());
             System.out.println("AI file: " + carPanel.getAiFile());
             throw e;
        }
    }

}
