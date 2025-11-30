package org.clinica.utils;

import java.nio.file.Paths;

public class PAthFXML {

    public static String PathFXML(){
        String path = "src/main/java/org/clinica/view" ;
        return Paths.get(path).toAbsolutePath().toString();
    }
}
