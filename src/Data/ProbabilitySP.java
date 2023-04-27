package Data;

import java.io.BufferedWriter;
import java.io.FileWriter;

import ilog.concert.*;
import ilog.cplex.*;

public class ProbabilitySP {
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
	

	public static double Obj2 = 0;
	public static int nodes2 = 0;

	public static void solve() {
		
		String fileName = "RSPP_100_1_PSP";
		String filePath = "C:\\\\Users\\\\``\\\\eclipse-workspace\\\\cplex\\\\result\\\\";
		String fileType = ".txt";
		try {
			GetData.main(null);
			App.solveMe();
			int s, t = 0;
			s = GetData.s; //출발지
			t = GetData.d; //도착지
			int n = GetData.nodenum, m = GetData.nodenum;
			IloCplex cplex = new IloCplex();
			
			//결정변수 및 비음제약식
			IloNumVar[][] x = new IloNumVar[n][m];
			for(int i = 0; i < n; i++) {
				x[i] = cplex.numVarArray(m, 0, 1, IloNumVarType.Int);
			}
			IloNumVar v = cplex.numVar(0, Double.MAX_VALUE, "v");
			IloNumVar Y = cplex.numVar(0, Double.MAX_VALUE, "Y");
			IloNumVar[][] A = new IloNumVar[n][m];
			for(int i=0; i < n; i++) {
				A[i] = cplex.numVarArray(m, 0, Double.MAX_VALUE);
			}
			
			//제약식
			IloLinearNumExpr const1 = cplex.linearNumExpr();
			for(int i=0; i<m; i++) {
				if(i!=(s-1)) {
					const1.addTerm(1.0, x[s-1][i]);
					const1.addTerm(-1, x[i][s-1]);
				}
			}
			cplex.addEq(const1, 1);
			
			IloLinearNumExpr const2 = cplex.linearNumExpr();
			for(int i=0; i<n; i++) {
				if(i!=(t-1)) {
					const2.addTerm(1.0, x[i][t-1]);
					const2.addTerm(-1, x[t-1][i]);
				}
			}
			cplex.addEq(const2, 1);
			
			for(int i=0; i<n; i++) {
				if(i!=(s-1) && i!=(t-1)) {
					IloLinearNumExpr const3 = cplex.linearNumExpr();
					for(int j=0; j<m; j++) {
						if(i!=j) {
							const3.addTerm(1.0, x[i][j]);
							const3.addTerm(-1.0, x[j][i]);
						}
					}
					cplex.addEq(const3, 0);
				}
			}
			
			double b = App.Obj * 1.1;
			IloLinearNumExpr const4 = cplex.linearNumExpr();
			for(int i=0; i<n;i++) {
				for(int j=0; j<m; j++) {
					const4.addTerm(GetData.TravelTime[i][j], x[i][j]);
				}
			}
			
			cplex.addLe(cplex.sum(const4, v),b);
			
			IloNumExpr const5 = cplex.quadNumExpr();
			//numexpr로 선언을 하고 quadNumExpr로 
			for(int i=0; i<n; i++) {
				for(int j=0; j<m; j++) {
					double st = GetData.StandardDeviation[i][j] * GetData.StandardDeviation[i][j];
					const5 = cplex.sum(const5, cplex.prod(st, A[i][j], A[i][j]));
				}
			}
			const5 = cplex.sum(const5, cplex.prod(-1.0, v,v));
			cplex.addLe(const5, 0);
						
			for(int i=0; i<n; i++) {
				for(int j=0; j<m; j++) {
					cplex.addLe(cplex.sum(A[i][j],cplex.prod(-1.0, Y)),0);
				}
			}
			
			for(int i=0; i<n; i++) {
				for(int j=0; j<m; j++) {
					cplex.addLe(cplex.sum(A[i][j], cplex.prod(-1e5, x[i][j])),0);
				}
			}
			
			for(int i=0; i<n; i++) {
				for(int j=0; j<m; j++) {
					cplex.addGe(A[i][j], cplex.sum(Y, cplex.prod(-1e5, cplex.sum(1, cplex.prod(-1.0, x[i][j])))));
				}
			}

			IloLinearNumExpr obj = cplex.linearNumExpr();
			obj.addTerm(1, Y);
			
			//목적함수
			cplex.addMaximize(obj);
			
			
			//solve
			cplex.exportModel("Test.lp");
			long start = System.currentTimeMillis();
			cplex.setParam(IloCplex.DoubleParam.TimeLimit, 3600);
			cplex.solve();
			for (int i = 0; i < n; i++) {
		        for (int j = 0; j < n; j++) {
		            if (i != j && cplex.getValue(x[i][j]) != 0) {
		               System.out.println("i = " + i + ", j = " + j);
		              
		            }
		        }
		    }
			Obj2 = cplex.getObjValue();
			nodes2 = cplex.getNnodes();
			cplex.end();
			long end = System.currentTimeMillis();
			double time = (end - start) / 1000.0;
			
			System.out.println("실행시간 = " + time + "초");
			System.out.println("목적함수 값 = " + Obj2);
			System.out.println("노드수 = " + nodes2);
			
			String fileName2 = "RSPP_100_1_psp";
			result(filePath, fileName2, fileType, time, Obj2, nodes2);
		}
		catch(IloException exc) {
			exc.printStackTrace();
		}
	}
	

}