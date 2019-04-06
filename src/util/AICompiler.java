package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AICompiler {

    public void compile(File source) throws FileNotFoundException {
        try {
            InputStream in = new FileInputStream(source);
        } catch (FileNotFoundException e) {

        }
    }

}
