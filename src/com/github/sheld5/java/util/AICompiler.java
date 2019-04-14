package util;

import main.CarPanel;
import model.DriverAI;
import org.joor.Reflect;

import java.io.*;

/**
 * Compiles external AI code.
 */
public class AICompiler {

    /**
     * Compiles the AI of the given CarPanel if it has not been compiled yet and returns an instance of it.
     * Uses the jOOR library. Uses the get-methods of the CarPanel to get the name of the AI and the name of its file.
     * @param carPanel the CarPanel from which the methods gets the required information about the AI.
     * @return an instance of the AI described in the CarPanel given as the parameter.
     * @throws IOException
     * @see DriverAI
     * @see CarPanel#getAiName()
     * @see CarPanel#getAiFile()
     */
    public DriverAI compile(CarPanel carPanel) throws IOException {
        try {
            System.out.println(carPanel.getAiFile().getPath());
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
