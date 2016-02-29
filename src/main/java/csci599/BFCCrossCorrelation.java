package csci599;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BFCCrossCorrelation
{
    static int noOfMIMETypes;
    static int charSetSize;
    static List<String> MIMETypes;
    static HashMap<String,double[][]> CorrMatrix;
    
    BFCCrossCorrelation(final File sortFolder)
    {
        noOfMIMETypes=15;
        charSetSize=256;
        MIMETypes=FileTypeFilter.getMIMETypes(sortFolder);
        //initialize fingerprint
        CorrMatrix=new HashMap<String,double[][]>();
        double[][] matrix=new double[charSetSize][charSetSize];
        for(int j=0;j<charSetSize;j++)
        {
            for(int i=j+1;i<charSetSize;i++)
            {
                matrix[j][i]=1.0;
                matrix[i][j]=0.0;
            }
        }
        for(String s:MIMETypes)
            CorrMatrix.put(s, matrix);
    }
    
    public void listFilesForFolder(final File folder) throws IOException, FileNotFoundException, IOException{
        FileTypeFilter.forEach(folder, (file, MIMEType) -> {
            double[] BFD=new double[charSetSize];
            try {
                //calculate BFD of the input file
                BFD=frequencyDist(BFD,file);
            } catch (IOException ex) {
                System.err.println("Error reading file: " + file.getPath());
            }
            //generate BFC matrix of the input file
            double[][] matrix=new double[charSetSize][charSetSize];
            matrix=generateCFMatrix(BFD);
            //matrix=calculateCS(matrix);
            //update fingerprint
            updateFPMatrix(matrix,MIMEType);
        });
        try {
            new ObjectMapper().writeValue(new File("bfc.json"), CorrMatrix);
        } catch (IOException ex) {
            System.err.println("Error writing bfc.json");
        }
    }
    
    public double[] frequencyDist(double[] BFD,File file) throws IOException,FileNotFoundException, IOException
    {
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
        for(int j=0;j<matrix.length;j++)
        {
            for(int i=j+1;i<matrix.length;i++)
            {
                matrix[i][j]=BFD[i]-BFD[j];
                matrix[j][i]=CorrelationStrength(matrix[i][j]);
            }
        }
        return matrix;
    }
    
    public double[][] calculateCS(double[][] matrix)
    {
        for(int j=1;j<matrix.length;j++)
        {
            for(int i=j+1;i<matrix[j].length;i++)
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
        double[][] FPMatrix=CorrMatrix.get(MIMEType);
        int noOfFiles=(int) FPMatrix[0][0];
        for(int j=0;j<FPMatrix.length;j++)
        {
            for(int i=j+1;i<FPMatrix[j].length;i++)
            {
                double newCS=CorrelationStrength(FPMatrix[i][j]-matrix[i][j]);
                FPMatrix[j][i]=((FPMatrix[j][i]*noOfFiles)+newCS)/(noOfFiles+1);
                FPMatrix[i][j]=((FPMatrix[i][j]*noOfFiles)+matrix[i][j])/(noOfFiles+1);
            }
        }
        FPMatrix[0][0]++;
        CorrMatrix.replace(MIMEType,FPMatrix);
    }
}
