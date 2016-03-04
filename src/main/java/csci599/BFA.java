package csci599;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BFA {
    static HashMap<String,ArrayList<Double>> avgfrequency = new HashMap<>();
    static HashMap<String,ArrayList<Double>> corrstrength = new HashMap<>();
    static HashMap<String,Integer> no_of_files = new HashMap<>();
    static List<String> ContentType;
    static int newtypecount = 0;
    static HashMap<String,Integer> Type_Count = new HashMap<>();
    static HashMap<String,String> FileType = new HashMap<>();
    static HashMap<String,Double> Assurancelvl = new HashMap<>();
    
    BFA(final File sortFolder){
        //Adding Content types
        ContentType = FileTypeFilter.getMIMETypes(sortFolder);
        //Initializing no_of-files,avgfrequency,correlation strength
        Iterator<String> content_itr = ContentType.iterator();
        ArrayList<Double> arr = new ArrayList<>();
        for(int i = 0 ; i < 256;i++){
            arr.add(0.0);
        }
        while(content_itr.hasNext()){
            String s = content_itr.next();
            // ok that we put the same ArrayList reference for every HashMap value because the ArrayList will never be mutated
            avgfrequency.put(s,arr);
            corrstrength.put(s,arr);            
        }
    }
    //Analysing each files in a folder
    public void listFilesForFolder(final File folder){
        System.out.println("Creating fingerprints:");
        for (String contentType : FileTypeFilter.getMIMETypes(folder)) {
            no_of_files.put(contentType, 0);
        }
        FileTypeFilter.forEach(folder, (file, contentType) -> {
            //System.out.println(contentType);
            double[] fingerprint = freqAnalysis(file);   //Calculating fingerprint for a file
            avg(contentType,fingerprint);                //Adding fingerprint to its avg
            int num = no_of_files.get(contentType);      //Updating total no of files
            no_of_files.put(contentType, num+1);
        });
        System.out.println("Determining correlation strengths:");
        for (String contentType : FileTypeFilter.getMIMETypes(folder)) {
            no_of_files.put(contentType, 0);
        }
        FileTypeFilter.forEach(folder, (file, contentType) -> {
            double[] fingerprint = freqAnalysis(file);   //Calculating fingerprint for a file
            ArrayList<Double> corrfactor = findcorrelation(fingerprint,contentType); //Calulating correlation factor
            addCorrelation(corrfactor,contentType);
            int num = no_of_files.get(contentType);      //Updating total no of files
            no_of_files.put(contentType, num+1);
        });
        //displayFingerprintCorr();
        JsonConnect();

   }
   public void listFilesForFolder1(final File folder) throws IOException{     
        Map<String, BFAFingerprint> json = null;
        try {
            json = JSONGenerator.readJSON("bfa.json");
            //System.out.println(json);
        } catch (IOException ex) {
            System.out.println("Error reading bfa.json. Make sure it exists.");
        }
        for(String key:json.keySet())
        {
            avgfrequency.put(key, json.get(key).BFD);
            ContentType.add(key);
            Type_Count.put(key,0);
        }
        FileTypeFilter.forEach(folder, (file, contentType) ->{
            try {    
                detectUnknown(file);
            } catch (IOException ex) {
                System.out.println("Exception Occured");;
            }
        });
        new ObjectMapper().writeValue(new File("Detected_Mime_type_count.json"), Type_Count);
        new ObjectMapper().writeValue(new File("File_and_mimetype.json"), FileType);
        new ObjectMapper().writeValue(new File("File_and_alevel.json"), Assurancelvl);
        
    }
    public static double[] freqAnalysis(File f){
        double[] fingerprint = new double[256];
        double[] frequency = new double[256];
        for(int i = 0; i < 256;i++){
            frequency[i] = 0;
        }        
        try(FileInputStream in = new FileInputStream(f)){
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
            System.out.println("Error analyzing file: " + f.getPath());
        }
        return fingerprint;
    }
    public void avg(String Contenttype,double[] fingerprint){
            //Updating avg fingerprint
            ArrayList<Double> arr = avgfrequency.get(Contenttype);
            ArrayList<Double> newavg = new ArrayList<>();
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
        ArrayList<Double> corrfactor = new ArrayList<>();
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
        ArrayList<Double> temp = new ArrayList<>();
        ArrayList<Double> old = corrstrength.get(contenttype);
        for(int i = 0 ; i < 256 ; i++){
            Double NCS = ((old.get(i) * PNF) + corrfactor.get(i))/ (PNF+1);
            temp.add(i, NCS);
        }
        corrstrength.replace(contenttype,temp);
    }
    public void JsonConnect(){
        try{
            JSONGenerator.generateJSON("bfa.json",avgfrequency, corrstrength);
        }
        catch(Exception e){
            System.out.println("Error saving bfa.json");
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
    public static void detectUnknown(File f) throws IOException{
        double[] newfingerprint = freqAnalysis(f);
        //Boolean isNew = true;
        double maxalevel = 0;
        String mimetype = "";
        //Computing correlation factor score for each file type
        for(String s : ContentType){
            ArrayList<Double> corscore = findcorrelation(newfingerprint,s);
            Double sum = 0.0;
            for(Double d: corscore){
                sum += d;
            }
            //Computing and adding assurance level Assurance level
            Double alevel = sum/256;
            if(alevel >= maxalevel){
                maxalevel = alevel;
                mimetype = s;
            }
        }        //isNew = false;
        Integer newcount = Type_Count.get(mimetype) + 1;
        Type_Count.put(mimetype,newcount);
        FileType.put(f.getPath(),mimetype);
        Assurancelvl.put(f.getPath(),maxalevel);
        
        /*if(isNew == true){
            System.out.println("It is new type");
            newtypecount++;
            String d = "NewType_" + newtypecount;
            ContentType.add(d);
            ArrayList<Double> al = new ArrayList();
            for(int i = 0 ; i < 256;i++){
                al.add(newfingerprint[i]);
            }
                 */     
    }            
}
