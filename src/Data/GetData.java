package Data;
import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GetData {
	public static int nodenum = 0;
	public static double[][] TravelTime = null;
	public static double[][] StandardDeviation = null;
	public static int s = 0, d = 0;

	public static void main(String[] args, int index1, int index2) {
		
		Path path = Paths.get("C:\\Users\\``\\eclipse-workspace\\cplex\\data\\RSPP_"+index1+"00_"+index2+".txt");
		
		
		try {
			List<String> allLines = Files.readAllLines(path);
			nodenum = Integer.parseInt(allLines.get(0));
			TravelTime = new double[nodenum][nodenum];
			StandardDeviation = new double[nodenum][nodenum];
			String[] sd = allLines.get(1).split(" ");
			s = Integer.parseInt(sd[0]);
			d = Integer.parseInt(sd[1]);
			

			for(int i=0; i<nodenum; i++) {
				String[] tmpdata = allLines.get(i+2).split("\t");
				for(int j=0; j<nodenum; j++) {
					TravelTime[i][j] = Double.parseDouble(tmpdata[j]);
					//System.out.print(TravelTime[i][j]+"\t");
				}
				//System.out.println();
			}
			
			for(int i=nodenum; i<nodenum*2; i++) {
				String[] tmpdata = allLines.get(i+3).split("\t");
				for(int j=0; j<nodenum; j++) {
					StandardDeviation[i-nodenum][j] = Double.parseDouble(tmpdata[j]);
				}
			}
			
		}
		catch(IOException ie) {
			ie.printStackTrace();
		}
		
		/*System.out.println(nodenum);
		
		for(int i=0; i<nodenum; i++) {
			for(int j=0; j<nodenum; j++) {
				System.out.print(TravelTime[i][j]+"\t");
			}
			System.out.println();
		}
		System.out.println();
		for(int i=0; i<nodenum; i++) {
			for(int j=0; j<nodenum; j++) {
				System.out.print(StandardDeviation[i][j]+"\t");
			}
			System.out.println();
		}*/
		//System.out.println(s + " "+ d);
	}
	
}
