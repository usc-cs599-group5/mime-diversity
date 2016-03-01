package csci599;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class BFDCorrelation
{
    static String[] MIMEType=new String[15];
    static int noOfMIMETypes;
    static int charSetSize;
    static List<String> ContentType;
    
    BFDCorrelation()
    {
        for(int i=0;i<15;i++)
        {
            MIMEType[i]="MIMEType "+(i+1);
        }
        noOfMIMETypes=15;
        charSetSize=256;
        ContentType = new ArrayList<>();
        ContentType.add("image/jpg");
        ContentType.add("image/png");
        ContentType.add("text/plain");
    }
    
    //Analysing each files in a folder
    public void listFilesForFolder(final File folder) {
        for (File file : folder.listFiles()) {
            if (!file.isDirectory()) {
                BFC(file.getPath());
                
            }
        }
    }
    
    public String BFC(String filePath)
    {
        //read json file
        Map<String, BFAFingerprint> json;
        try {
            json = JSONGenerator.readJSON("bfa.json");
            //System.out.println(json);
        } catch (IOException ex) {
            System.out.println("Error reading bfa.json. Make sure it exists.");
            return "";
        }

        //contains BFD of input file
        double[] inputFile=new double[charSetSize];
        
        //read the fingerprint from json
        double[][] fingerPrintBFDs=new double[noOfMIMETypes][charSetSize];
        /*double[][] fingerPrintBFDs=JSONGenerator.getJSONFingerprintArray("E:\\Sem 2\\CSCI 599\\fingerprint.json");
        for(int i=0;i<fingerPrintBFDs.length;i++)
        {
            for(int j=0;j<fingerPrintBFDs[i].length;j++)
                System.out.print(fingerPrintBFDs[i][j]+", ");
            System.out.println("");
        }*/
        //read correlation strengths from json //used if second interpretation of assurance level is used
        double[][] fingerPrintCS=new double[noOfMIMETypes][charSetSize];
        
        //correlation strengths of input file and fingerprint
        double[][] correlationStrength=new double[noOfMIMETypes][charSetSize];
        
        //assurance levels of input file and fingerprint //used if second interpretation of assurance level is used
        double[][] assuranceLevel=new double[noOfMIMETypes][charSetSize];
        //calculate the BFD of the input file
        try {
            inputFile=BFD(filePath);
        } catch (Exception ex) {
            System.out.println("Error reading file: " + filePath);
            return "";
        }
        //normalize the BFD
        inputFile=Normalize(inputFile);
        //perform companding if spikes are present
        inputFile=CompandInputFileBFD(inputFile);
        //calculate correlation strength of input file for all mime types
        for(int i=0;i<noOfMIMETypes;i++)
        {
            for(int j=0;j<charSetSize;j++)
            {
                double correlationFactor=inputFile[j]-fingerPrintBFDs[i][j];
                correlationStrength[i][j]=findCorrelationStrength(correlationFactor);
            }
        }
        /*
        //calculate assurance level //not sure this is correct //my interpretation
        double[] assuranceLevels=new double[noOfMIMETypes];
        for(int i=0;i<noOfMIMETypes;i++)
        {
            assuranceLevels[i]=0;
            for(int j=0;j<charSetSize;j++)
                assuranceLevels[i]=assuranceLevels[i]+correlationStrength[i][j];
            assuranceLevels[i]=assuranceLevels[i]/charSetSize;
        }
        int matchedMIMEType=findMaxIndex(assuranceLevels);
        return MIMEType[matchedMIMEType];
        */
        //calculate assurance level //not sure this is correct //from whatever understood from the algorithm
        for(int i=0;i<15;i++)
        {
            for(int j=0;j<256;j++)
            {
                assuranceLevel[i][j]=(correlationStrength[i][j]+fingerPrintCS[i][j])/2;
            }
        }
        double[] avgAL=new double[noOfMIMETypes];
        avgAL=avg(assuranceLevel,avgAL);
        int matchedMIMEType=findMatch(avgAL);
        if(matchedMIMEType<=14)
            return MIMEType[matchedMIMEType];
        else
            return "MIMEType Not Determined";
    }
    
    public double[] BFD(String filePath) throws FileNotFoundException, IOException
    {
        double[] freqDist=new double[charSetSize];
        try (FileInputStream in = new FileInputStream(filePath)) {
            int ch=-1;
            while((ch=in.read())!=-1)
            {
                freqDist[ch]++;
            }
        }
        return freqDist;
    }
    
    public double[] Normalize(double[] inputFile)
    {
        double max=findMax(inputFile);
        for(int i=0;i<inputFile.length;i++)
            inputFile[i]=inputFile[i]/max;
        return inputFile;
    }
    
    public double findMax(double[] array)
    {
        double max=array[0];
        for(int i=0;i<array.length;i++)
        {
            if(max<array[i])
                max=array[i];
        }
        return max;
    }
    
    public double[] CompandInputFileBFD(double[] inputFile)
    {
        for(int i=0;i<inputFile.length;i++)
            inputFile[i]=Compand(inputFile[i]);
        return inputFile;
    }
    
    public double Compand(double input)
    {
        double beta=1.5;
        return Math.pow(input, (1/beta));
    }
    
    public double findCorrelationStrength(double correlationFactor)
    {
        //this is linear formula for correlation
        //return 1-Math.abs(correlationFactor);
        //have to use bell curve formula
        double sigma=0.0375;
        return Math.pow(Math.E, (((-1)*Math.pow(correlationFactor, 2))/(2*Math.pow(sigma, 2))));
    }
    
    public int findMaxIndex(double[] array)
    {
        int maxIndex=0;
        double max=array[0];
        for(int i=0;i<array.length;i++)
        {
            if(max<array[i])
            {
                max=array[i];
                maxIndex=i;
            }
        }
        return maxIndex;
    }
    
    public double[] avg(double[][] array,double[] result)
    {
        for(int i=0;i<array.length;i++)
        {
            double sum=0;
            int j=0;
            for(j=0;j<array[i].length;j++)
            {
                sum=sum+array[i][j];
            }
            result[i]=sum/j;
        }
        return result;
    }
    
    public int findMatch(double[] array)
    {
        int match=-1;
        double max=0.99;
        for(int i=0;i<array.length;i++)
        {
            if(max<=array[i])
            {
                max=array[i];
                match=i;
            }
        }
        return match;
    }
}
