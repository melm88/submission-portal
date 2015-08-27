package com.portal.assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Evaluator {
	
	private CodeObject cob;

	public Evaluator(){
		cob= new CodeObject();
	}
	
	public ArrayList<String> getOutput() {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add(String.valueOf(cob.exitCode));
		arr.add(cob.outputMSG);
		arr.add(cob.errorMSG);
		return arr;
	}
	
	public void processCode(String directoryPATH, String testFile){
		
		//POJO object
		//String directoryPATH = File.separator+"tmp"+File.separator+"AssignmentSubmissions"+File.separator+"ExtractedFiles"+File.separator;
		//String filePATH = "";		
		//System.out.println("CODE: "+codeHolder);
		
		try {
            
			//Compile All Java code
			//int compileOP = 0;
			String compile_command = "javac "+directoryPATH+File.separator+"*.java";
			System.out.println("Compile: "+compile_command);
            int compileOP = runProcess("javac "+directoryPATH+File.separator+"*.java", cob);
            //If EQUALS 0 then successful compilation
            //Run the test file (java)
            
            if(compileOP == 0){
            	
            	String run_command = "java -cp "+directoryPATH+" "+testFile.split("\\.")[0];
            	System.out.println("RUN: "+run_command);
            	
            	runProcess("java -cp "+directoryPATH+" "+testFile.split("\\.")[0], cob);
            	//out.println("Output: "+cob.outputMSG);     
            	System.out.println("OUTPUT: "+cob.outputMSG);
            	System.out.println("ExitCode: "+cob.exitCode);
            	System.out.println("Error: "+cob.errorMSG);
            	System.out.println("Wrote JSON SUCCESS state");
            }
            else {		            	
            	//out.println("Error: "+cob.errorMSG);
            	System.out.println(cob.errorMSG);
            	System.out.println(cob.exitCode);
            	System.out.println("Wrote JSON FAIL state");
            	
            }	            
                   
            //Get .class File
            /*File classFile = new File(directoryPATH + File.separator+"TaraQuiz.class");
            if(classFile.exists()){
            	classFile.delete();		            	
            }*/
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}


	private static void printLines(String cmd, InputStream ins, CodeObject cob) throws Exception {
		String line = null;
		BufferedReader in = new BufferedReader(
				new InputStreamReader(ins));
		while ((line = in.readLine()) != null) {
			if(cmd.equals("Output"))
				cob.outputMSG += line +"\n";
			else
				cob.errorMSG += line +"\n";
		}
	}	


	private static int runProcess(String command, CodeObject cob) throws Exception {
		Process pro = Runtime.getRuntime().exec(command);
		//printLines(command + " stdout:", pro.getInputStream(), out);
		//printLines(command + " stderr:", pro.getErrorStream(), out);
		pro.waitFor();
		//out.println(command + " exitValue() " + pro.exitValue());

		if(pro.exitValue() == 0){
			printLines("Output", pro.getInputStream(), cob);
		} else {
			printLines("Error", pro.getErrorStream(), cob);
		}   
		cob.exitCode = pro.exitValue();
		return pro.exitValue();
	}
	
	

}

class CodeObject {		
	String errorMSG;
	String outputMSG;
	int exitCode;

	public CodeObject(){
		errorMSG = "";
		outputMSG = "";
		exitCode = -1; // MAKE IT -1 (when doing adding evaluation logic.
	}		
}
