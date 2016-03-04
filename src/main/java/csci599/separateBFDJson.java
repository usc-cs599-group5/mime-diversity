package csci599;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class separateBFDJson
{
    static ObjectMapper mapper = new ObjectMapper();

    separateBFDJson(String filePath) throws IOException
    {
        LinkedHashMap<String, ArrayList<String>> fileNames=new LinkedHashMap<>(mapper.readValue(new File(filePath+"bfd-I-a.json"), new TypeReference<HashMap<String, ArrayList<String>>>() { } ));
        LinkedHashMap<String, double[]> BFDs=new LinkedHashMap<>(mapper.readValue(new File(filePath+"bfd-I-b.json"), new TypeReference<HashMap<String, double[]>>() { } ));
        HashMap<String, double[]> MIMEType0=new HashMap<>();
        HashMap<String, double[]> MIMEType1=new HashMap<>();
        HashMap<String, double[]> MIMEType2=new HashMap<>();
        HashMap<String, double[]> MIMEType3=new HashMap<>();
        HashMap<String, double[]> MIMEType4=new HashMap<>();
        HashMap<String, double[]> MIMEType5=new HashMap<>();
        HashMap<String, double[]> MIMEType6=new HashMap<>();
        HashMap<String, double[]> MIMEType7=new HashMap<>();
        HashMap<String, double[]> MIMEType8=new HashMap<>();
        HashMap<String, double[]> MIMEType9=new HashMap<>();
        HashMap<String, double[]> MIMEType10=new HashMap<>();
        HashMap<String, double[]> MIMEType11=new HashMap<>();
        HashMap<String, double[]> MIMEType12=new HashMap<>();
        HashMap<String, double[]> MIMEType13=new HashMap<>();
        String[] MIMETypes=new String[14];
        
        int i=0;
        for(Object key:fileNames.keySet())
        {
            if(!key.equals("application/octet-stream"))
            {
                MIMETypes[i]=(String) key;
                i++;
            }
        }
        
        i=0;
        for(i=0;i<14;i++)
        {
            ArrayList<String> fileList=new ArrayList(fileNames.get(MIMETypes[i]));
            for(String s:fileList)
            {
                double[] BFD=BFDs.get(s);
                switch(i)
                {
                    case 0:
                    {
                        MIMEType0.put(s, BFD);
                        break;
                    }
                    case 1:
                    {
                        MIMEType1.put(s, BFD);
                        break;
                    }
                    case 2:
                    {
                        MIMEType2.put(s, BFD);
                        break;
                    }
                    case 3:
                    {
                        MIMEType3.put(s, BFD);
                        break;
                    }
                    case 4:
                    {
                        MIMEType4.put(s, BFD);
                        break;
                    }
                    case 5:
                    {
                        MIMEType5.put(s, BFD);
                        break;
                    }
                    case 6:
                    {
                        MIMEType6.put(s, BFD);
                        break;
                    }
                    case 7:
                    {
                        MIMEType7.put(s, BFD);
                        break;
                    }
                    case 8:
                    {
                        MIMEType8.put(s, BFD);
                        break;
                    }
                    case 9:
                    {
                        MIMEType9.put(s, BFD);
                        break;
                    }
                    case 10:
                    {
                        MIMEType10.put(s, BFD);
                        break;
                    }
                    case 11:
                    {
                        MIMEType11.put(s, BFD);
                        break;
                    }
                    case 12:
                    {
                        MIMEType12.put(s, BFD);
                        break;
                    }
                    case 13:
                    {
                        MIMEType13.put(s, BFD);
                        break;
                    }
                }
                
            }
        }
        for(i=0;i<14;i++)
        {
            MIMETypes[i]=MIMETypes[i].replace("/", "");
            MIMETypes[i]=MIMETypes[i].replace("\\", "");
            MIMETypes[i]=MIMETypes[i].replace(":", "");
            MIMETypes[i]=MIMETypes[i].replace("*", "");
            MIMETypes[i]=MIMETypes[i].replace("\"", "");
            MIMETypes[i]=MIMETypes[i].replace("<", "");
            MIMETypes[i]=MIMETypes[i].replace(">", "");
            MIMETypes[i]=MIMETypes[i].replace("|", "");
            switch(i)
                {
                    case 0:
                    {
                        System.out.println(MIMEType0.size());
                        try {
                            System.out.println(MIMETypes[i]);
                            new ObjectMapper().writeValue(new File(MIMETypes[i]+".json"), MIMEType0);
                            System.out.println(MIMETypes[i]+".json created");
                        } catch (IOException ex) {
                            System.out.println(ex);
                        System.err.println("Error writing bfc.json");
                        }
                        break;
                    }
                    case 1:
                    {
                        System.out.println(MIMEType1.size());
                        try {
                            new ObjectMapper().writeValue(new File(MIMETypes[i]+".json"), MIMEType1);
                            System.out.println(MIMETypes[i]+".json created");
                        } catch (IOException ex) {
                        System.err.println("Error writing bfc.json");
                        }
                        break;
                    }
                    case 2:
                    {
                        System.out.println(MIMEType2.size());
                        try {
                            new ObjectMapper().writeValue(new File(MIMETypes[i]+".json"), MIMEType2);
                            System.out.println(MIMETypes[i]+".json created");
                        } catch (IOException ex) {
                        System.err.println("Error writing bfc.json");
                        }
                        break;
                    }
                    case 3:
                    {
                        System.out.println(MIMEType3.size());
                        try {
                            new ObjectMapper().writeValue(new File(MIMETypes[i]+".json"), MIMEType3);
                            System.out.println(MIMETypes[i]+".json created");
                        } catch (IOException ex) {
                        System.err.println("Error writing bfc.json");
                        }
                        break;
                    }
                    case 4:
                    {
                        System.out.println(MIMEType4.size());
                        try {
                            new ObjectMapper().writeValue(new File(MIMETypes[i]+".json"), MIMEType4);
                            System.out.println(MIMETypes[i]+".json created");
                        } catch (IOException ex) {
                        System.err.println("Error writing bfc.json");
                        }
                        break;
                    }
                    case 5:
                    {
                        System.out.println(MIMEType5.size());
                        try {
                            new ObjectMapper().writeValue(new File(MIMETypes[i]+".json"), MIMEType5);
                            System.out.println(MIMETypes[i]+".json created");
                        } catch (IOException ex) {
                        System.err.println("Error writing bfc.json");
                        }
                        break;
                    }
                    case 6:
                    {
                        System.out.println(MIMEType6.size());
                        try {
                            new ObjectMapper().writeValue(new File(MIMETypes[i]+".json"), MIMEType6);
                            System.out.println(MIMETypes[i]+".json created");
                        } catch (IOException ex) {
                        System.err.println("Error writing bfc.json");
                        }
                        break;
                    }
                    case 7:
                    {
                        System.out.println(MIMEType7.size());
                        try {
                            new ObjectMapper().writeValue(new File(MIMETypes[i]+".json"), MIMEType7);
                            System.out.println(MIMETypes[i]+".json created");
                        } catch (IOException ex) {
                        System.err.println("Error writing bfc.json");
                        }
                        break;
                    }
                    case 8:
                    {
                        System.out.println(MIMEType8.size());
                        try {
                            new ObjectMapper().writeValue(new File(MIMETypes[i]+".json"), MIMEType8);
                            System.out.println(MIMETypes[i]+".json created");
                        } catch (IOException ex) {
                        System.err.println("Error writing bfc.json");
                        }
                        break;
                    }
                    case 9:
                    {
                        System.out.println(MIMEType9.size());
                        try {
                            new ObjectMapper().writeValue(new File(MIMETypes[i]+".json"), MIMEType9);
                            System.out.println(MIMETypes[i]+".json created");
                        } catch (IOException ex) {
                        System.err.println("Error writing bfc.json");
                        }
                        break;
                    }
                    case 10:
                    {
                        System.out.println(MIMEType10.size());
                        try {
                            new ObjectMapper().writeValue(new File(MIMETypes[i]+".json"), MIMEType10);
                            System.out.println(MIMETypes[i]+".json created");
                        } catch (IOException ex) {
                        System.err.println("Error writing bfc.json");
                        }
                        break;
                    }
                    case 11:
                    {
                        System.out.println(MIMEType11.size());
                        try {
                            new ObjectMapper().writeValue(new File(MIMETypes[i]+".json"), MIMEType11);
                            System.out.println(MIMETypes[i]+".json created");
                        } catch (IOException ex) {
                        System.err.println("Error writing bfc.json");
                        }
                        break;
                    }
                    case 12:
                    {
                        System.out.println(MIMEType12.size());
                        try {
                            new ObjectMapper().writeValue(new File(MIMETypes[i]+".json"), MIMEType12);
                            System.out.println(MIMETypes[i]+".json created");
                        } catch (IOException ex) {
                        System.err.println("Error writing bfc.json");
                        }
                        break;
                    }
                    case 13:
                    {
                        System.out.println(MIMEType13.size());
                        try {
                            new ObjectMapper().writeValue(new File(MIMETypes[i]+".json"), MIMEType13);
                            System.out.println(MIMETypes[i]+".json created");
                        } catch (IOException ex) {
                        System.err.println("Error writing bfc.json");
                        }
                        break;
                    }
                }
        }
    }
}
