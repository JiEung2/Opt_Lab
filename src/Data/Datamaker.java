package Data;
import java.util.Random;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;


public class Datamaker {
	
	public static void main(String[] args) {
				
		Random random = new Random();
		
		int nodenum = 300; //노드의 개수
		int x[]= new int[nodenum]; //노드의 x좌표
		int y[]= new int[nodenum]; //노드의 y좌표
		double TT[][] = new double[nodenum][nodenum]; //Travel Time
		double ST[][] = new double[nodenum][nodenum]; //표준편차
		int speed;
		int s, d;
		s = (int)(Math.random()*300) + 1;
		d = (int)(Math.random()*300) + 1;
		
		for(int i=0; i<nodenum; i++) {
			x[i] = random.nextInt(10001);
			y[i] = random.nextInt(10001);
		}
		for(int i=0; i<nodenum; i++) {
			for(int j=0; j<nodenum; j++) {
				//d[i][j]= (int) Math.sqrt((x[i]-x[j])*(x[i]-x[j]) + (y[i]-y[j])*(y[i]-y[j]));
				speed = (int) (Math.random() * 90) + 10;
				TT[i][j] = Math.round((double)Math.sqrt(Math.pow(x[i]-x[j], 2.0)+ Math.pow(y[i]-y[j], 2.0))*100)/100.0; //소수 둘째자리까지 표현
				TT[i][j] /= speed;
				ST[i][j] = (double)(Math.random()*0.9) + 0.1;
			}
		}
		
		//텍스트 파일 저장
		String fileName = "RSPP_300_5";
		String fileType = ".txt";
		String filePath = "C:\\\\Users\\\\``\\\\eclipse-workspace\\\\cplex\\\\data\\\\";
		//String fileName = "C:\\\\Users\\\\``\\\\eclipse-workspace\\\\cplex\\\\data\\\\RSPP_50_1.txt";
		
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter(filePath + fileName + fileType, true));
			fw.write(Integer.toString(nodenum));
			fw.newLine();
			fw.write(s+" "+d);
			fw.newLine();
			
			
			for(int i=0; i<nodenum; i++) {
				for(int j=0; j<nodenum; j++) {
					fw.write(TT[i][j]+"\t");
				}
				fw.newLine();
			}
			fw.newLine();
			
			for(int i=0; i<nodenum; i++) {
				for(int j=0; j<nodenum; j++) {
					fw.write(ST[i][j]+"\t");
				}
				fw.newLine();
			}
			
			fw.flush();
			fw.close();
			
		}
		catch(Exception e) {
			e.getStackTrace();
		}
		
	}
	
}