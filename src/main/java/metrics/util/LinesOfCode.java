package metrics.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LinesOfCode {
	/**
	 * Calculates the total lines of code within a class
	 * @param Class name to be inspected
	 * @return A int value showing the total lines of code within a class
	 * @throws IOException
     */
	public static int GetTotalLinesOfCode(File file) throws IOException{		
		int counter = 0;
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		
		while(line != null) {
		    counter++;
		    line = br.readLine();
		}
		br.close(); 	
		return counter;
	}
}
