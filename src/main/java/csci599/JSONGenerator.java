package csci599;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
        json=json.substring(0, json.length()-2);
        json=json+"}";
        return json;
    }
    
    public static double[][] getJSONFingerptint(String filePath) throws FileNotFoundException, IOException
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
    }
}
