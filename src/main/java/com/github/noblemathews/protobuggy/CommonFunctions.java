package com.github.noblemathews.protobuggy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CommonFunctions {
    public static String readFile(String path) {
        String result = "";
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while(line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
