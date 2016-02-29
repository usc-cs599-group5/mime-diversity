package csci599;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;

public class JSONGenerator
{
    static ObjectMapper mapper = new ObjectMapper();

    // This assumes that fingerprint and correlationStrengths have the same keys.
    public static void generateJSON(String filePath,HashMap<String,ArrayList<Double>> fingerprint,HashMap<String,ArrayList<Double>> correlationStrengths) {
        Map<String, BFAFingerprint> json = new HashMap<>();
        for (String mime : fingerprint.keySet()) {
            BFAFingerprint jsonFingerprint= new BFAFingerprint();
            jsonFingerprint.BFD = fingerprint.get(mime);
            jsonFingerprint.CS = correlationStrengths.get(mime);
            json.put(mime, jsonFingerprint);
        }
        try {
            mapper.writeValue(new File(filePath), json);
        } catch (IOException ex) {
            System.out.println("Error writing BFA JSON file.");
        }
    }

    public static HashMap<String, BFAFingerprint> readJSON(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), new TypeReference<Map<String, BFAFingerprint>>() { } );
    }
    
}
