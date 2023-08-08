package Tools;
import java.io.*;
import java.util.ArrayList;

import Broker.Test;

public class RunGLPSol {

	public static void main(String[] args) {
		Test t = new Test();
		//t.FillData();
		RunGLPSol run = new RunGLPSol();
		String CID = "2";
		run.RunCommand(CID);
		run.ReadResult(CID);
		
	}
	
	Boolean found = false;
	
	void RunCommand(String CID) {
		String s = null;
        try {
        	String Datafile = "LM/Customer"+ CID + ".data";
        	String Resultfile = "LM/ResultCustomer"+ CID + ".txt";
        	String Query = "/usr/local/bin/glpsol --math LM/ModelOneCustomer.mod --data " 
        	+  Datafile + " --display " + Resultfile;;
        	//String Query = "/usr/local/bin/glpsol --math LM/ModelOneCustomer.mod --data LM/Customer1.data --display LM/ResultCustomer.txt";
        	Process p = Runtime.getRuntime().exec(Query);
        	
        	 // read the output from the command
             BufferedReader stdInput = new BufferedReader(new 
                InputStreamReader(p.getInputStream()));
             while ((s = stdInput.readLine()) != null) {
            		 System.out.println(s);
            		 if(s.contains("OPTIMAL SOLUTION FOUND"))
            			 found = true;
             }
        	
            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));
            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
    
		
	}
	
	ArrayList<String[]> results = new ArrayList<String[]>();
	
	@SuppressWarnings("resource")
	String ReadResult(String CID) {
		 System.out.println("Here is the standard output of the command:\n");
		 String out ="";
		 try {
			 String filename =  "LM/ResultCustomer"+ CID + ".txt";
			 File file = new File(filename); 	         
			 BufferedReader br = new BufferedReader(new FileReader(file)); 	            
			 String st; 
			 
			 while ((st = br.readLine()) != null) {
				 String[] r = st.split("\\s+");
				 results.add(r);
				 out = out + st + "\n";
				 System.out.println(st); 
				 //System.out.println(r[2]);
			 }
		 } 
		 catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		 
		 return out;
        // read the output from the command
       // BufferedReader stdInput = new BufferedReader(new 
        //   InputStreamReader(p.getInputStream()));
        /*while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }*/
	}

}
