package csci599;

public class BFDCorrelation
{
    
    public void BFDCorrelation()
    {
        float[] inputFile=new float[256];
        float[][] fingerPrints=new float[15][256];
        float[][] correlation=new float[15][257];
        
        for(int i=0;i<256;i++)
        {
            float sum=0;
            for(int j=0;j<256;j++)
            {
                correlation[i][j]=Math.abs(inputFile[j]-fingerPrints[i][j]);
                sum=sum+correlation[i][j];
            }
            correlation[i][256]=sum/256;
        }
        
        int matchedMIMEType=0;
        float max=correlation[0][256];
        for(int i=1;i<15;i++)
        {
            if(max<correlation[i][256])
            {
                max=correlation[i][256];
                matchedMIMEType=i;
            }
        }
        
    }
}
