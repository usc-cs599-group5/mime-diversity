package csci599;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BFCCrossCorrelation
{
    static int noOfMIMETypes;
    static int charSetSize;
    static ArrayList<String> MIMETypes;
    static HashMap<String,ArrayList<ArrayList<Double>>> CorrMatrix;
    
    BFCCrossCorrelation()
    {
        noOfMIMETypes=15;
        charSetSize=256;
        MIMETypes=new ArrayList<String>();
        MIMETypes.add("image/jpg");
        MIMETypes.add("image/png");
        MIMETypes.add("text/plain");
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
        Iterator i=MIMETypes.iterator();
        while(i.hasNext())
            CorrMatrix.put((String) i.next(), tempAL2);
    }
    
    public void listFilesForFolder(final File folder) throws IOException, FileNotFoundException, IOException{
        FileTypeFilter.forEach(folder,MIMETypes, (file, MIMEType) -> {
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
        {
            BFD[c]++;
        }
        return BFD;
    }
    
    public double[][] generateCFMatrix(double[] BFD)
    {
        double[][] matrix=new double[charSetSize][charSetSize];
        for(int j=1;j<matrix.length;j++)
        {
            for(int i=0;i<j;i++)
            {
                matrix[j][i]=BFD[j]-BFD[i];
            }
        }
        return matrix;
    }
    
    public double[][] calculateCS(double[][] matrix)
    {
        for(int j=1;j<matrix.length;j++)
        {
            for(int i=j+1;j<matrix[j].length;i++)
            {
                matrix[j][i]=CorrelationStrength(matrix[i][j]);
            }
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
            {
                tempMatrix[j][i]=matrixAL.get(j).get(i);
            }
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
            {
                tempMatrix[j][i]=((tempMatrix[j][i]*noOfFiles)+matrix[j][i])/(noOfFiles+1);
            }
        }
        tempMatrix[0][0]++;
    }
}
