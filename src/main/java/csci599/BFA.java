package csci599;
import java.io.*;
public class BFA {
    static float[] avgfrequency;
    static int no_of_files;
    BFA(){
        avgfrequency = new float[256];
        no_of_files = 0;
    }
    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } 
            else {
                System.out.println(fileEntry.getName());
                freqAnalysis("C:\\Users\\Public\\Pictures\\Sample Pictures\\" + fileEntry.getName());
                System.out.println("I m back");
            }
    }
}
    public void freqAnalysis(String f){
        float[] fingerprint = new float[256];
        float[] frequency = new float[256];
        for(int i = 0; i < 256;i++){
            frequency[i] = 0;
        }        
        try{
            //FileInputStream in = new FileInputStream("D:\\Studies\\Studies\\USC\\tika-test\\src\\main\\java\\csci599\\TempFile.txt");
            FileInputStream in = new FileInputStream(f);
            int c;
            while ((c = in.read()) != -1){
                frequency[c]+=1;
            }
            //Finding max occurring frequency
            float max = 0;
            for(int i = 0;i < 256; i++){
                if(max < frequency[i]) max = frequency[i];
            }
            //Normalising frequency
            for(int i = 0; i < 256;i++){
                fingerprint[i] = frequency[i]/max;
                System.out.print(fingerprint[i] + " ");
            }
            //Updating avg fingerprint
            for(int i = 0; i < 256;i++){
                avgfrequency[i] = ((avgfrequency[i] * no_of_files) + fingerprint[i])/(no_of_files + 1);
                System.out.print(avgfrequency[i] + " ");
                no_of_files++;
            }
        }
        catch(Exception e){
            System.out.println("File not found exception");
        }

    }
}
