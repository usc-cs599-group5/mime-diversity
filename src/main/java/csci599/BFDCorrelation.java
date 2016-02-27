package csci599;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BFDCorrelation
{
    static String[] MIMEType=new String[15];
    static int noOfMIMETypes;
    static int charSetSize;
    
    BFDCorrelation()
    {
        for(int i=0;i<15;i++)
        {
            MIMEType[i]="MIMEType "+(i+1);
        }
        noOfMIMETypes=15;
        charSetSize=256;
    }
    
    public String BFC(String filePath) throws IOException
    {
        //contains BFD of input file
        double[] inputFile=new double[charSetSize];
        
        //read the fingerprint from json
        double[][] fingerPrintBFDs=new double[noOfMIMETypes][charSetSize];
        
        //read correlation strengths from json //used if second interpretation of assurance level is used
        double[][] fingerPrintCS=new double[noOfMIMETypes][charSetSize];
        
        //correlation strengths of input file and fingerprint
        double[][] correlationStrength=new double[noOfMIMETypes][charSetSize];
        
        //assurance levels of input file and fingerprint //used if second interpretation of assurance level is used
        double[][] assuranceLevel=new double[noOfMIMETypes][charSetSize];
        //calculate the BFD of the input file
        inputFile=BFD(filePath);
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
        /*
        //calculate assurance level //not sure this is correct //from whatever understood from the algorithm
        for(int i=0;i<15;i++)
        {
            for(int j=0;j<256;j++)
            {
                assuranceLevel[i][j]=(correlationStrength[i][j]+fingerPrintCS[i][j])/2;
            }
        }*/
    }
    
    public double[] BFD(String filePath) throws FileNotFoundException, IOException
    {
        double[] freqDist=new double[charSetSize];
        FileInputStream in = new FileInputStream(filePath);
        int ch=-1;
        while((ch=in.read())!=-1)
        {
            freqDist[ch]++;
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
        return Math.pow(input, beta);
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
}
