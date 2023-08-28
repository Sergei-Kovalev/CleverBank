package ru.ngs.summerjob.config;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class Config {

    public static Map<String, Map<String, String>> getConfig() {
        try (InputStream inputStream = new FileInputStream("src/main/resources/properties.yml")) {
            Yaml yaml = new Yaml();
            return yaml.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
