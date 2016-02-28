package csci599;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    static int noOfMIMETypes=15;
    static int charSetSize=256;
    
    /*public static String generateJSON(HashMap<String,ArrayList<Double>> fingerprint,HashMap<String,ArrayList<Double>> correlationStrengths)
    {
        String json="{";
        for(Object MIMEType:fingerprint.keySet())
        {
            json=json+"\""+MIMEType+"\": ";
            json=json+"{";
            json=json+"\"BFD\": ";
            json=json+"[";
            Iterator i1=fingerprint.get(MIMEType).iterator();
            while(i1.hasNext())
                json=json+i1.next()+",";
            json=json.substring(0, json.length()-1);
            json=json+"],";
            json=json+"\"CS\": ";
            json=json+"[";
            Iterator i2=correlationStrengths.get(MIMEType).iterator();
            while(i2.hasNext())
                json=json+i2.next()+",";
            json=json.substring(0, json.length()-1);
            json=json+"]";
            json=json+"},";
        }
        json=json.substring(0, json.length()-2);
        json=json+"}";
        return json;
    }*/
    
    /*public static double[][] getJSONFingerptint(String filePath) throws FileNotFoundException, IOException
    {
        double[][] fingerprint=new double[noOfMIMETypes][charSetSize];
        BufferedReader br=new BufferedReader(new FileReader(filePath));
        String input="",inputFile="";
        while((input=br.readLine())!=null)
        {
            inputFile=inputFile+input;
        }
        int i=0,j=0;
        String[] MIMETypes=inputFile.split("},");
        MIMETypes[0]=MIMETypes[0].substring(1, MIMETypes[0].length());
        MIMETypes[14]=MIMETypes[14].substring(0,MIMETypes[14].length()-1);
        return fingerprint;
    }*/
    
    /*public static void generateJSON(String filePath,HashMap<String,ArrayList<Double>> fingerprint,HashMap<String,ArrayList<Double>> correlationStrengths) throws IOException
    {
        WriteJSONFingerPrint FPHashMap = new WriteJSONFingerPrint(fingerprint,correlationStrengths);
        mapper.writeValue(new File(filePath), FPHashMap);
    }*/
    
    public static HashMap<String, ArrayList<Double>> getJSONFingerprint(String filePath) throws IOException
    {
        HashMap<String,JSONFingerPrint> JSONfingerprint;
        JSONfingerprint = mapper.readValue(filePath,new TypeReference<Map<String, JSONFingerPrint>>() { } );
        HashMap<String, ArrayList<Double>> fingerprint=new HashMap<String, ArrayList<Double>>();
        for(Object MIMEType:JSONfingerprint.keySet())
        {
            if(!fingerprint.containsKey(MIMEType))
            {
                ArrayList<Double> BFD=JSONfingerprint.get(MIMEType).BFD;
                fingerprint.put((String) MIMEType, BFD);
            }
        }
        return fingerprint;
    }
    
    public static HashMap<String, ArrayList<Double>> getJSONCorrelationStrengths(String filePath) throws IOException
    {
        HashMap<String,JSONFingerPrint> JSONfingerprint;
        JSONfingerprint = mapper.readValue(filePath,new TypeReference<Map<String, JSONFingerPrint>>() { } );
        HashMap<String, ArrayList<Double>> fingerprint=new HashMap<String, ArrayList<Double>>();
        for(Object MIMEType:JSONfingerprint.keySet())
        {
            if(!fingerprint.containsKey(MIMEType))
            {
                ArrayList<Double> BFD=JSONfingerprint.get(MIMEType).CS;
                fingerprint.put((String) MIMEType, BFD);
            }
        }
        return fingerprint;
    }
    
    public static double[][] getJSONFingerprintArray(String filePath) throws IOException
    {
        HashMap<String,JSONFingerPrint> JSONfingerprint;
        JSONfingerprint = mapper.readValue(filePath,new TypeReference<HashMap<String, JSONFingerPrint>>() { } );
        HashMap<String, ArrayList<Double>> fingerprint=new HashMap<String, ArrayList<Double>>();
        for(Object MIMEType:JSONfingerprint.keySet())
        {
            if(!fingerprint.containsKey(MIMEType))
            {
                ArrayList<Double> BFD=JSONfingerprint.get(MIMEType).BFD;
                fingerprint.put((String) MIMEType, BFD);
            }
        }
        double[][] fingerprintArray=new double[noOfMIMETypes][charSetSize];
        int i=0,j=0;
        for(Object MIMEType:fingerprint.keySet())
        {
            ArrayList<Double> BFD=(ArrayList<Double>) fingerprint.get(MIMEType);
            Iterator i1=BFD.iterator();
            while(i1.hasNext())
            {
                fingerprintArray[i][j]=(double) i1.next();
                j+=1;
            }
            i+=1;
        }
        return fingerprintArray;
    }
   
    public static double[][] getJSONCSArray(String filePath) throws IOException
    {
        HashMap<String,JSONFingerPrint> JSONfingerprint;
        JSONfingerprint = mapper.readValue(filePath,new TypeReference<Map<String, JSONFingerPrint>>() { } );
        HashMap<String, ArrayList<Double>> fingerprint=new HashMap<String, ArrayList<Double>>();
        for(Object MIMEType:JSONfingerprint.keySet())
        {
            if(!fingerprint.containsKey(MIMEType))
            {
                ArrayList<Double> BFD=JSONfingerprint.get(MIMEType).CS;
                fingerprint.put((String) MIMEType, BFD);
            }
        }
        double[][] fingerprintArray=new double[noOfMIMETypes][charSetSize];
        int i=0,j=0;
        for(Object MIMEType:fingerprint.keySet())
        {
            ArrayList<Double> CS=(ArrayList<Double>) fingerprint.get(MIMEType);
            Iterator i1=CS.iterator();
            while(i1.hasNext())
            {
                fingerprintArray[i][j]=(double) i1.next();
                j+=1;
            }
            i+=1;
        }
        return fingerprintArray;
    }
}

class JSONFingerPrint
{
    public ArrayList<Double> BFD;
    public ArrayList<Double> CS;
}

class WriteJSONFingerPrint
{
    public HashMap<String,ArrayList<ArrayList<Double>>> fingerprint;
    //public HashMap<String,ArrayList<Double>> BFD;
    //public HashMap<String,ArrayList<Double>> CS;
    
    WriteJSONFingerPrint(HashMap<String,ArrayList<Double>> bfdHM,HashMap<String,ArrayList<Double>> csHM)
    {
        fingerprint=new HashMap<String,ArrayList<ArrayList<Double>>>();
        HashMap<String,ArrayList<Double>> BFD=new HashMap<String,ArrayList<Double>>(bfdHM);
        HashMap<String,ArrayList<Double>> CS=new HashMap<String,ArrayList<Double>>(csHM);
        for(Object MIMEType: bfdHM.keySet())
        {
            if(!fingerprint.containsKey(MIMEType))
            {
                ArrayList<ArrayList<Double>> FP=new ArrayList<ArrayList<Double>>();
                ArrayList<Double> bfdAL=BFD.get(MIMEType);
                ArrayList<Double> cdAL=CS.get(MIMEType);
                FP.add(bfdAL);
                FP.add(cdAL);
                fingerprint.put((String) MIMEType, FP);
            }
        }
    }
}
