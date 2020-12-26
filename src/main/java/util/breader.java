package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class breader extends BufferedReader {

    public breader(File file) throws Exception, Error {
        super(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
    }

    public breader(String filename) throws Exception, Error {
        this(new File(filename));
    }
}
