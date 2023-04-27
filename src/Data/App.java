package Data;
import ilog.concert.*;
import ilog.cplex.*;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class App {
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
	
	public static void solveMe(int index1, int index2) {
//		String fileName = "RSPP_100_1";
//		String filePath = "C:\\Users\\``\\eclipse-workspace\\cplex\\result\\";
//		String fileType = ".txt";
		try {
			GetData.main(null, index1, index2);
		
			int s = GetData.s-1;
			int d = GetData.d-1;
			int n = GetData.nodenum;
			int m = GetData.nodenum;
			//System.out.println(s+" "+d);
			
			IloCplex cplex = new IloCplex();
			
			//결정변수 및 비음제약식
			IloNumVar[][] x = new IloNumVar[n][m];
			for(int i = 0; i < n; i++) {
				x[i] = cplex.numVarArray(m, 0, 1, IloNumVarType.Int);
			}
			/*for(int i = 0; i < n; i++) {
				for(int j=0; j<n; j++) {
					x[i][j] = cplex.intVar(0, 1);
				}
			}*/
			//목적함수
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for(int i=0; i<n; i++) {
				for(int j=0; j<n; j++) {
					if(i!=j)
							objective.addTerm(GetData.TravelTime[i][j], x[i][j]);
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
			long start = System.currentTimeMillis();
			cplex.solve();
			Obj = cplex.getObjValue();
			nodes = cplex.getNnodes();
			int sum=0;
			for (int i = 0; i < n; i++) {
		        for (int j = 0; j < n; j++) {
		            if (i != j && cplex.getValue(x[i][j]) != 0) {
		               System.out.println("i = " + i + ", j = " + j);
		               sum+= GetData.TravelTime[i][j];
		            }
		        }
		    }
			//System.out.println(sum);
			cplex.end();
			long end = System.currentTimeMillis();
			double time = (end - start) / 1000.0;
			
			System.out.println("실행시간 = " + time + "초");
			System.out.println("목적함수 값 = " + Obj);
			System.out.println("노드수 = " + nodes);
			//System.out.println(s+" "+ d);
			 
			
			//파일 생성
			//result(filePath, fileName, fileType, time, Obj, nodes);
			
		
		}
		catch(IloException exc) {
			exc.printStackTrace();
		}
	}
	
	
}
