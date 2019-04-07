package util;

import main.menu.AIPanel;
import model.DriverAI;
import org.joor.Reflect;

import java.io.*;


public class AICompiler {

    public DriverAI compile(AIPanel aiPanel) throws IOException {
        try {
            InputStream in = new FileInputStream(aiPanel.getFile());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line).append("\n");
                line = reader.readLine();
            }
            String content = builder.toString();

            return Reflect.compile("model." + aiPanel.getAIName(), content).create().get();
        } catch (IOException e) {
             System.out.println("Error while compiling AI " + aiPanel.getAIName());
             System.out.println("AI file: " + aiPanel.getFile());
             throw e;
        }
    }

}
