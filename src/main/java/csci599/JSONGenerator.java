package csci599;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class JSONGenerator
{
    static int noOfMIMETypes=15;
    static int charSetSize=256;
    
    public static String generateJSON(HashMap<String,ArrayList<Double>> fingerprint,HashMap<String,ArrayList<Double>> correlationStrengths)
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
        json=json.substring(0, json.length()-1);
        json=json+"}";
        return json;
    }
    
    public static double[][] getJSONFingerptint(String filePath) throws FileNotFoundException
    {
        double[][] fingerprint=new double[noOfMIMETypes][charSetSize];
        FileInputStream in = new FileInputStream(filePath);
        
        return fingerprint;
    }
}
