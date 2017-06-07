import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class UserStore {

	private ArrayList<String> users = new ArrayList<String>();
	private ArrayList<String> pass = new ArrayList<String>();
	//private String[] users = {"guest","ian"};
	//private String[] pass = {"guest","ian"};
	String userFileName = "users.txt";
	String passFileName = "pass.txt";
	
	UserStore(){
		readUsers();
		readPass();
	}
	public boolean verfifyUser(String userIn, String passIn){
		
		for(int i=0;i<users.size();i++){
			if(users.get(i).equalsIgnoreCase(userIn)){
				if(pass.get(i).equalsIgnoreCase(passIn))
					return true;
				else
					return false;
			}
		}
		return registerUser(userIn, passIn);
		
	}

	private boolean registerUser(String userIn, String passIn) {
	    users.add(userIn);
	    pass.add(passIn);
	    writeUser(userIn);
	    writePass(passIn);
		return true;
	}
	private void writeUser(String userIn){
		
		try {
			FileWriter fileWriter = new FileWriter(userFileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for(int i=0;i<users.size();i++){
				bufferedWriter.write(users.get(i));
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void writePass(String passIn){
		try {
			FileWriter fileWriter = new FileWriter(passFileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for(int i=0;i<pass.size();i++){
				bufferedWriter.write(pass.get(i));
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void readUsers(){
		String line  =null;
		try{
			FileReader fileReader = new FileReader(userFileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine())!=null)
				users.add(line);
			bufferedReader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void readPass(){
		String line  =null;
		try{
			FileReader fileReader = new FileReader(passFileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine())!=null)
				pass.add(line);
			bufferedReader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
