package Data;
import ilog.concert.*;
import ilog.cplex.*;

import java.io.*;
import java.util.*;


class EXP{
	double m, s;
	public EXP(double m, double s) {
		this.m = m;
		this.s = s;
	}
}

public class Algorithm23 {
	static String fileName = "RSPP_100_1_SUB";
	static String filePath = "C:\\Users\\``\\eclipse-workspace\\cplex\\result\\";
	static String fileType = ".txt";
	
	static ArrayList<EXP> list;
	static LinkedHashSet<EXP> set;
	
	public static void main(String[] args) throws IOException{
		long start = System.currentTimeMillis();
		set = new LinkedHashSet<>();
		App.solveMe();
		double m1, m2, s1, s2;
		
		m1 = SUB(1,0)[0];
		s1 = SUB(1,0)[1];
		set.add(new EXP(m1,s1));
		m2 = SUB(0,1)[0];
		s2 = SUB(0,1)[1];
		set.add(new EXP(m2,s2));
		
		NEWSOL(m1,s1,m2,s2);
		
		Iterator<EXP> iter = set.iterator();
		while(iter.hasNext()) {
			EXP e = iter.next();
			try {
				BufferedWriter fw = new BufferedWriter(new FileWriter(filePath+fileName+fileType, true));
				fw.newLine();
				fw.write(Double.toString(e.m));
				fw.write(" ");
				fw.write(Double.toString(e.s));
				fw.write(" ");
				fw.write(Double.toString((App.Obj*1.1 - e.m)/Math.sqrt(e.s)));
				//fw.newLine();
				
				
				fw.flush();
				fw.close();
			}
			catch(Exception a) {
				a.getStackTrace();
			}
			System.out.println(e.m +" "+e.s+" "+ (App.Obj*1.1 - e.m)/Math.sqrt(e.s) );
		}
		long end = System.currentTimeMillis();
		double time = (end - start) / 1000.0;
		
		System.out.println("실행시간 = " + time + "초");
		System.exit(0);
	}
	
	public static void NEWSOL(double m1, double s1, double m2, double s2) throws IOException {
		double m3=0, s3=0;
		if(m1!=m2) {
			double tmp = -((s2-s1)/(m2-m1));
			m3 = SUB(tmp, 1)[0];
			s3 = SUB(tmp, 1)[1];
		}
		else {
			m3 = SUB(1,0)[0];
			s3 = SUB(1,0)[1];
		}
		if((m3!=m1|| s3!=s1) && (m3!=m2 ||s3!=s2)) {
			set.add(new EXP(m3,s3));
			NEWSOL(m1,s1,m3,s3);
			NEWSOL(m2,s2,m3,s3);
		}
		
	}
	public static void result(String filePath, String fileName, String fileType, double time, double Obj, int nodes) {
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter(filePath+fileName+fileType, true));
			fw.write(Double.toString(time));
			fw.newLine();
			fw.write(Double.toString(Obj));
			fw.newLine();
			fw.write(Integer.toString(nodes));
			fw.newLine();
			
			fw.flush();
			fw.close();
		}
		catch(Exception e) {
			e.getStackTrace();
		}
	}

	public static double Obj = 0;
	public static int nodes = 0;
	
	public static double[] SUB(double c1, double c2) throws IOException{
		
		double[] answer = new double[2];
		try {
			GetData.main(null);
		
			int s = GetData.s-1;
			int d = GetData.d-1;
			int n = GetData.nodenum;
			int m = GetData.nodenum;
			
			IloCplex cplex = new IloCplex();
			
			//결정변수 및 비음제약식
			IloNumVar[][] x = new IloNumVar[n][m];
			for(int i = 0; i < n; i++) {
				x[i] = cplex.numVarArray(m, 0, 1, IloNumVarType.Int);
			}

			//목적함수
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for(int i=0; i<n; i++) {
				for(int j=0; j<n; j++) {
					if(i!=j) {
							objective.addTerm(c1*GetData.TravelTime[i][j], x[i][j]);
							objective.addTerm(c2*GetData.StandardDeviation[i][j]*GetData.StandardDeviation[i][j], x[i][j]);
					}
				}
			}
			cplex.addMinimize(objective);
			
			//제약식
			IloLinearNumExpr const1 = cplex.linearNumExpr();
			for(int j=0; j<n; j++){
				if(j!=s){
					const1.addTerm(1.0, x[s][j]);
					const1.addTerm(-1, x[j][s]);
				}} 
			cplex.addEq(const1, 1);
			
			IloLinearNumExpr const2 = cplex.linearNumExpr();
			for(int j=0; j<n; j++){
				if(j!=d){
					const2.addTerm(1.0, x[j][d]);
					const2.addTerm(-1,x[d][j]);
				}}
			cplex.addEq(const2, 1);
			

			for(int i=0; i<n; i++){
				IloLinearNumExpr const3 = cplex.linearNumExpr();
				if(i!=s && i!=d){
					for(int j=0; j<n; j++){
						if(i!=j){
							const3.addTerm(1.0, x[i][j]);
							const3.addTerm(-1.0, x[j][i]);
						}
					}
				cplex.addEq(const3, 0);
				}
			}
			
			//solve
			double m1=0, s1=0;
			long start = System.currentTimeMillis();
			cplex.solve();
			Obj = cplex.getObjValue();
			nodes = cplex.getNnodes();
			for (int i = 0; i < n; i++) {
		        for (int j = 0; j < n; j++) {
		            if (i != j && cplex.getValue(x[i][j]) > 1-1e-5) {
		               //System.out.println("i = " + i + ", j = " + j);
		               m1 += GetData.TravelTime[i][j];
		               s1 += GetData.StandardDeviation[i][j]*GetData.StandardDeviation[i][j];
		            }
		        }
		    }
			//list.add(new EXP(c1, c2, m1, s1));
			answer[0]=m1;
			answer[1]=s1;
			cplex.end();
			long end = System.currentTimeMillis();
			double time = (end - start) / 1000.0;
			
			System.out.println("m = "+ m1 + ", s = "+s1);
			System.out.println("실행시간 = " + time + "초");
			System.out.println("목적함수 값 = " + Obj);
			System.out.println("노드수 = " + nodes);
			 
			
			//파일 생성
			result(filePath, fileName, fileType, time, Obj, nodes);
			
			
		
		}
		catch(IloException exc) {
			exc.printStackTrace();
		}
		return answer;
	}
	
	
}
