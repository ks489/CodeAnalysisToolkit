package converter.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CSVConverter {
	
	public static void convertMapToCSV(String[] headers, Map<String, Integer> ccMap,  String outputFileName, String fileDirectory){
		try {

			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileDirectory + outputFileName + ".csv"));
			
			//Writing the headers
			for (String header : headers) {
				//Last item in the list so no need to print the comma
				if(headers[headers.length - 1].equals(header)){
					fileWriter.append(header);
				}else{
					fileWriter.append(header + ",");
				}
			}
			fileWriter.newLine();
			
			Iterator it = ccMap.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        fileWriter.append(pair.getKey() + ",");
		        fileWriter.append(pair.getValue().toString());
		        fileWriter.newLine();
		        it.remove(); 
		    }
			
			fileWriter.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
