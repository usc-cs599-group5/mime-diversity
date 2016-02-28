package csci599;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BFCCrossCorrelation
{
    static int noOfMIMETypes;
    static int charSetSize;
    static List<String> MIMETypes;
    static HashMap<String,ArrayList<ArrayList<Double>>> CorrMatrix;
    
    BFCCrossCorrelation(File sortFolder)
    {
        noOfMIMETypes=15;
        charSetSize=256;
        MIMETypes=FileTypeFilter.getMIMETypes(sortFolder);
        CorrMatrix=new HashMap<String,ArrayList<ArrayList<Double>>>();
        ArrayList<Double> tempAL1=new ArrayList<Double>();
        ArrayList<ArrayList<Double>> tempAL2=new ArrayList<ArrayList<Double>>();
        for(int j=0;j<charSetSize;j++)
        {
            for(int i=0;i<charSetSize;i++)
            {
                if(i>j)
                    tempAL1.add(1.0);
                else
                    tempAL1.add(0.0);
            }
            tempAL2.add(tempAL1);
        }
        for(String s:MIMETypes)
            CorrMatrix.put(s, tempAL2);
    }
    
    public void listFilesForFolder(final File folder) throws IOException, FileNotFoundException, IOException{
        FileTypeFilter.forEach(folder, (file, MIMEType) -> {
            double[] BFD=new double[charSetSize];
            try {
                BFD=frequencyDist(file);
            } catch (IOException ex) {
            }
            double[][] matrix=new double[noOfMIMETypes][charSetSize];
            matrix=generateCFMatrix(BFD);
            matrix=calculateCS(matrix);
            updateFPMatrix(matrix,MIMEType);
            //matrix=generateCorrMatrix(file);
        });        
    }
    
    public double[] frequencyDist(File file) throws IOException,FileNotFoundException, IOException
    {
        double[] BFD=new double[charSetSize];
        for(int i=0;i<BFD.length;i++)
            BFD[i]=0.0;
        FileInputStream in = new FileInputStream(file);
        int c=0;
        while((c=in.read())!=-1)
            BFD[c]++;
        return BFD;
    }
    
    public double[][] generateCFMatrix(double[] BFD)
    {
        double[][] matrix=new double[charSetSize][charSetSize];
        for(int j=1;j<matrix.length;j++)
        {
            for(int i=0;i<j;i++)
                matrix[j][i]=BFD[j]-BFD[i];
        }
        return matrix;
    }
    
    public double[][] calculateCS(double[][] matrix)
    {
        for(int j=1;j<matrix.length;j++)
        {
            for(int i=j+1;j<matrix[j].length;i++)
                matrix[j][i]=CorrelationStrength(matrix[i][j]);
        }
        return matrix;
    }
    
    public double CorrelationStrength(double x)
    {
        return 1-Math.abs(x);
    }
    
    public void updateFPMatrix(double[][] matrix,String MIMEType)
    {
        ArrayList<ArrayList<Double>> matrixAL=new ArrayList<ArrayList<Double>>(CorrMatrix.get(MIMEType));
        double[][] tempMatrix=new double[charSetSize][charSetSize];
        for(int j=0;j<matrix.length;j++)
        {
            for(int i=0;i<matrix[j].length;i++)
                tempMatrix[j][i]=matrixAL.get(j).get(i);
        }
        int noOfFiles=(int) tempMatrix[0][0];
        for(int j=0;j<matrix.length;j++)
        {
            for(int i=j+1;i<matrix[j].length;j++)
            {
                double newCS=CorrelationStrength(tempMatrix[i][j]-matrix[i][j]);
                matrix[j][i]=((tempMatrix[j][i]*noOfFiles)+newCS)/(noOfFiles+1);
            }
        }
        for(int j=0;j<matrix.length;j++)
        {
            for(int i=0;i<j;i++)
                tempMatrix[j][i]=((tempMatrix[j][i]*noOfFiles)+matrix[j][i])/(noOfFiles+1);
        }
        tempMatrix[0][0]++;
        ArrayList<ArrayList<Double>> AL1=new ArrayList<ArrayList<Double>>();
        for(int j=0;j<tempMatrix.length;j++)
        {
            ArrayList<Double> AL2=new ArrayList<Double>();
            for(int i=0;i<tempMatrix[j].length;i++)
                AL2.add(matrix[j][i]);
            AL1.add(AL2);
        }
        CorrMatrix.replace(MIMEType,AL1);
    }
}
