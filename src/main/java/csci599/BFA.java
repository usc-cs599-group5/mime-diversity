package csci599;
import com.google.common.io.Files;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BFA {
    static HashMap<String,ArrayList<Double>> avgfrequency = new HashMap<>();
    static HashMap<String,ArrayList<Double>> corrstrength = new HashMap<>();
    static HashMap<String,Integer> no_of_files = new HashMap<>();
    static List<String> ContentType;
    BFA(){
        //Adding Content types
        ContentType = new ArrayList();
        ContentType.add("image/jpg");
        ContentType.add("image/png");
        ContentType.add("text/plain");
        //Initializing no_of-files,avgfrequency,correlation strength
        Iterator<String> content_itr = ContentType.iterator();
        ArrayList<Double> arr = new ArrayList();
        for(int i = 0 ; i < 256;i++){
            arr.add(0.0);
        }
        while(content_itr.hasNext()){
            String s = content_itr.next();
            no_of_files.put(s,0);
            avgfrequency.put(s,arr);
            corrstrength.put(s,arr);
        }
    }
    //Analysing each files in a folder
    public void listFilesForFolder(final File folder){
        FileTypeFilter.forEach(folder,ContentType, (file, contentType) -> {
            //System.out.println(contentType);
            double[] fingerprint = freqAnalysis(file);   //Calculating fingerprint for a file
            avg(contentType,fingerprint);                //Adding fingerprint to its avg
            ArrayList<Double> corrfactor = findcorrelation(fingerprint,contentType); //Calulating correlation factor
            addCorrelation(corrfactor,contentType);
            int num = no_of_files.get(contentType);      //Updating total no of files
            no_of_files.put(contentType, num+1);
            ArrayList<Double> cr = corrstrength.get(contentType);
        });
        displayFingerprintCorr();
        JsonConnect();
        
    }
    public static double[] freqAnalysis(File f){
        double[] fingerprint = new double[256];
        double[] frequency = new double[256];
        for(int i = 0; i < 256;i++){
            frequency[i] = 0;
        }        
        try{
            FileInputStream in = new FileInputStream(f);
            int c;
            //Calculating frequecy of words
            while ((c = in.read()) != -1){
                frequency[c]+=1;
            }
            //Finding max occurring frequency
            double max = 0;
            for(int i = 0;i < 256; i++){
                if(max < frequency[i]) max = frequency[i];
            }
            //Normalising frequency
            for(int i = 0; i < 256;i++){
                fingerprint[i] = frequency[i]/max;
                //System.out.print(fingerprint[i] + " ");
            }
            //Companding function call
            fingerprint = companding(fingerprint);
        }
        catch(Exception e){
            System.out.println("File not found exception");
        }
        return fingerprint;
    }
    public void avg(String Contenttype,double[] fingerprint){
            //Updating avg fingerprint
            ArrayList<Double> arr = avgfrequency.get(Contenttype);
            ArrayList<Double> newavg = new ArrayList();
            Integer num = no_of_files.get(Contenttype);
            int display = 1;
            for(int i = 0 ; i < 256 ; i++){
                Double avgfre = arr.get(i);
                //if(i == 1)System.out.print("Old Avg " + avgfre + "    ");
                Double temp = ((avgfre * num) + fingerprint[i])/(num + 1);
                //if(i == 1)System.out.println("New Avg " + temp);
                newavg.add(i, temp);
                //if(display <= 8){display++;System.out.print(temp + " ");}
                //else {System.out.println();display = 0;}                
            }
            avgfrequency.replace(Contenttype,newavg);
            //System.out.println();
    }
    public static double[] companding(double[] fingerprint){
  
        double beta = 1.5;
        for(int i = 0 ; i < 256 ; i++){
            Double x = fingerprint[i];
            Double y = Math.pow(x,(1/beta));
            fingerprint[i] = y;   
        }
        return fingerprint;
    }
    public static ArrayList<Double> findcorrelation(double[] fingerprint,String contenttype){
        Double[] x = new Double[256];
        ArrayList<Double> arr = avgfrequency.get(contenttype); 
        //Calculating Difference
        for(int i = 0; i < 256 ; i++){
            Double x1 = arr.get(i);
            x[i] = Math.abs(x1-fingerprint[i]);
        }
        //Implementing Bell Curve
        double sigma = 0.0375;
        ArrayList<Double> corrfactor = new ArrayList();
        for(int i = 0 ; i < 256 ; i++){
            double corfact = Math.pow(Math.E,((x[i]*x[i]) * (-1))/(2*sigma));
            //if(i =='a') System.out.println("Hi  " + corfact);
            corrfactor.add(i, corfact);
        }
        //Returning correlation factor
        return corrfactor;
    }
    public void addCorrelation(ArrayList<Double> corrfactor,String contenttype){
        int PNF = no_of_files.get(contenttype);
        //System.out.println("No of Files" + PNF);
        ArrayList<Double> temp = new ArrayList();
        ArrayList<Double> old = corrstrength.get(contenttype);
        for(int i = 0 ; i < 256 ; i++){
            Double NCS = ((old.get(i) * PNF) + corrfactor.get(i))/ (PNF+1);
            temp.add(i, NCS);
        }
        corrstrength.replace(contenttype,temp);
    }
    public void JsonConnect(){
        try{
            JSONGenerator.generateJSON("E:\\Sem 2\\CSCI 599\\fingerprint.json",avgfrequency, corrstrength);
        }
        catch(Exception e){
            System.out.println("File Exception");
        }
    }
    public static void displayFingerprintCorr(){
        Iterator<String> itr = ContentType.iterator();
        while(itr.hasNext()){
            String content = itr.next();
            System.out.println(content);
            System.out.println("Avg frequency");
            for(int i = 0 ; i < 256 ; i++){
                System.out.print(avgfrequency.get(content).get(i) + " ");
                if(i%8 == 0)System.out.println("");
            }
            System.out.println("");
            System.out.println("Correlation");
            for(int i = 0 ; i < 256 ; i++){
                System.out.print(corrstrength.get(content).get(i) + " ");
                if(i%8 == 0)System.out.println("");
            }
            System.out.println("");
        }
    }
}
