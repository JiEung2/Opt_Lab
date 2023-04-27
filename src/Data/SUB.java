//package Data;
//import ilog.concert.*;
//import ilog.cplex.*;
//
//import java.io.*;
//import java.util.*;
//
//class EXP{
//	int c1, c2;
//	double m1, s1;
//	public EXP(int c1, int c2, double m1, double s1) {
//		this.c1 = c1;
//		this.c2 = c2;
//		this.m1 = m1;
//		this.s1 = s1;
//	}
//}
//
//public class SUB {
//	
//	static int c1, c2; 
//	static ArrayList<EXP> list;
//	
//	public static void main(String[] args){
//		Scanner sc = new Scanner(System.in);
//		list = new ArrayList<>();
//		/*while(true) {
//			c1 = sc.nextInt();
//			if(c1 == -1) break;
//			c2 = sc.nextInt();
//			
//			try {
//				solveMe();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}*/
//		
//		
//		for(EXP e : list) {
//			System.out.println("c1: "+e.c1+", c2: "+e.c2+" 일 때");
//			System.out.println("m1: "+e.m1 +", s1: "+e.s1);
//		
//		}
//		System.exit(0);
//	}
//	public static void result(String filePath, String fileName, String fileType, double time, double Obj, int nodes) {
//		try {
//			BufferedWriter fw = new BufferedWriter(new FileWriter(filePath+fileName+fileType, true));
//			fw.write(Double.toString(time));
//			fw.newLine();
//			fw.write(Double.toString(Obj));
//			fw.newLine();
//			fw.write(Integer.toString(nodes));
//			fw.newLine();
//			
//			fw.flush();
//			fw.close();
//		}
//		catch(Exception e) {
//			e.getStackTrace();
//		}
//	}
//
//	public static double Obj = 0;
//	public static int nodes = 0;
//	
//	public static void solveMe(int c1, int c2) throws IOException{
//		String fileName = "RSPP_50_1";
//		String filePath = "C:\\Users\\``\\eclipse-workspace\\cplex\\result\\";
//		String fileType = ".txt";
//		try {
//			GetData.main(null);
//		
//			int s = GetData.s-1;
//			int d = GetData.d-1;
//			int n = 50;
//			int m = 50;
//			
//			IloCplex cplex = new IloCplex();
//			
//			//결정변수 및 비음제약식
//			IloNumVar[][] x = new IloNumVar[n][m];
//			for(int i = 0; i < n; i++) {
//				x[i] = cplex.numVarArray(m, 0, 1, IloNumVarType.Int);
//			}
//
//			//목적함수
//			IloLinearNumExpr objective = cplex.linearNumExpr();
//			for(int i=0; i<n; i++) {
//				for(int j=0; j<n; j++) {
//					if(i!=j) {
//							objective.addTerm(c1*GetData.TravelTime[i][j], x[i][j]);
//							objective.addTerm(c2*GetData.StandardDeviation[i][j]*GetData.StandardDeviation[i][j], x[i][j]);
//					}
//				}
//			}
//			cplex.addMinimize(objective);
//			
//			//제약식
//			IloLinearNumExpr const1 = cplex.linearNumExpr();
//			for(int j=0; j<n; j++){
//				if(j!=s){
//					const1.addTerm(1.0, x[s][j]);
//					const1.addTerm(-1, x[j][s]);
//				}} 
//			cplex.addEq(const1, 1);
//			
//			IloLinearNumExpr const2 = cplex.linearNumExpr();
//			for(int j=0; j<n; j++){
//				if(j!=d){
//					const2.addTerm(1.0, x[j][d]);
//					const2.addTerm(-1,x[d][j]);
//				}}
//			cplex.addEq(const2, 1);
//			
//
//			for(int i=0; i<n; i++){
//				IloLinearNumExpr const3 = cplex.linearNumExpr();
//				if(i!=s && i!=d){
//					for(int j=0; j<n; j++){
//						if(i!=j){
//							const3.addTerm(1.0, x[i][j]);
//							const3.addTerm(-1.0, x[j][i]);
//						}
//					}
//				cplex.addEq(const3, 0);
//				}
//			}
//			
//			//solve
//			Double m1=0., s1=0.;
//			long start = System.currentTimeMillis();
//			cplex.solve();
//			Obj = cplex.getObjValue();
//			nodes = cplex.getNnodes();
//			for (int i = 0; i < n; i++) {
//		        for (int j = 0; j < n; j++) {
//		            if (i != j && cplex.getValue(x[i][j]) > 1-1e-5) {
//		               //System.out.println("i = " + i + ", j = " + j);
//		               m1 += GetData.TravelTime[i][j];
//		               s1 += GetData.StandardDeviation[i][j]*GetData.StandardDeviation[i][j];
//		            }
//		        }
//		    }
//			list.add(new EXP(c1, c2, m1, s1));
//			cplex.end();
//			long end = System.currentTimeMillis();
//			double time = (end - start) / 1000.0;
//			
//			System.out.println("m1 = "+ m1 + ", s1 = "+s1);
//			System.out.println("실행시간 = " + time + "초");
//			System.out.println("목적함수 값 = " + Obj);
//			System.out.println("노드수 = " + nodes);
//			 
//			
//			//파일 생성
//			result(filePath, fileName, fileType, time, Obj, nodes);
//			
//		
//		}
//		catch(IloException exc) {
//			exc.printStackTrace();
//		}
//	}
//	
//	
//}
