package util;

import main.menu.AIPanel;
import model.DriverAI;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


public class AICompiler {

    // ToDo
    public DriverAI compile(AIPanel aiPanel) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, aiPanel.getFile().getPath());
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { aiPanel.getFile().getParentFile().toURI().toURL() });
        Class<?> cls = Class.forName(aiPanel.getAIName(), true, classLoader);
        Object obj = cls.newInstance();
        return (DriverAI)obj;

    }

}
