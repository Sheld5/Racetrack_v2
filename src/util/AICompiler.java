package util;

import main.menu.AIPanel;
import model.DriverAI;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


public class AICompiler {

    public DriverAI compile(AIPanel aiPanel) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, aiPanel.getFile().getPath());
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { aiPanel.getFile().getParentFile().toURI().toURL() });
        Class<?> cls = Class.forName(aiPanel.getAIName(), true, classLoader);
        Object obj = cls.newInstance();
        return (DriverAI)obj;

    }

    /*
        InputStream in = new FileInputStream(aiPanel.getFile());
        String separator = System.getProperty("line.separator");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String source = reader.lines().collect(Collectors.joining(separator));

        String tmpdir = System.getProperty("java.io.tmpdir");
        Path sourcePath = Paths.get(tmpdir, "TestAI.java");
        Files.write(sourcePath, source.getBytes(UTF_8));

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, sourcePath.toFile().getAbsolutePath());
        Path classPath = sourcePath.getParent().resolve("TestAI.class");

        URL classUrl = classPath.getParent().toFile().toURI().toURL();
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{classUrl});
        Class<?> klas = Class.forName("TestAI", true, classLoader);
        return (DriverAI)klas.newInstance();
        */

    /*
    @SuppressWarnings("deprecation")
    public DriverAI compile(AIPanel aiPanel) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        List<String> optionList = new ArrayList<>();
        optionList.add("-classpath");
        optionList.add(System.getProperty("java.class.path") + ";model/DriverAI");

        Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(aiPanel.getFile()));
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, optionList, null, compilationUnit);
        task.call();

        URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(aiPanel.getFile().getPath()).toURI().toURL()});
        Class<?> loadedClass = classLoader.loadClass("resources.ai." + aiPanel.getAIName());
        Object obj = loadedClass.newInstance();
        return (DriverAI)obj;
    }
     */

}
