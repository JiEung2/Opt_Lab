package Data;

import java.util.*;
import java.io.*;

class Point{
	double m, s;
	public Point(double m, double s) {
		this.m = m;
		this.s = s;
	}
}

public class Dijkstra2 {
	static ArrayList<Point> list;
	static LinkedHashSet<Point> set;
	static int n;
	static int s;
	static int t;
	static double[][] sd;
	static double[][] tt;
	
	static String fileName = "RSPP_500_5_Dijkstra";
	static String filePath = "C:\\Users\\``\\eclipse-workspace\\cplex\\result\\";
	static String fileType = ".txt";
	
	//가장 짧은 거리를 가지고 있는 노드를 찾음
	static int findMinDistance(double d[], boolean T[], int n) {
		double min = Double.MAX_VALUE;
		int min_index = -1;
		
		for(int v=0; v<n; v++) {
			if(T[v] == false && d[v]<=min) {
				min = d[v];
				min_index = v;
			}
		}
		return min_index;
	}
	
	static double[] dijkstra(double c1, double c2) {

		double d[] = new double[n];
		boolean T[] = new boolean[n];
		int p[] = new int[n];
		double[][] obj = new double[n][n];
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				obj[i][j] = c1*tt[i][j] + c2*sd[i][j];
			}
		}
		Arrays.fill(d, Double.MAX_VALUE);
		Arrays.fill(T, false);
		Arrays.fill(p, -1);
		
		d[s] = 0;
		
		for(int i = 0; i<n-1; i++) {
			int u = findMinDistance(d, T, n);
			T[u] = true;
			//가장 짧은 거리를 가진 노드를 추가
			for(int v = 0; v <n; v++) {
				if(!T[v] && obj[u][v]!=0 && d[u]!=Double.MAX_VALUE && d[u]+obj[u][v] < d[v]) {
					d[v] = d[u] + obj[u][v];
					p[v] = u;
				}
			}
		}
		System.out.println("최단거리는");
		System.out.println(d[t]);
		
		//최단경로 추적
		ArrayList<Integer> path = new ArrayList<Integer>();
        int current = t;
        while (current != s) {
            path.add(current);
            current = p[current];
        }
        path.add(s);

        // 경로 뒤집기
        Collections.reverse(path);
        
        //double m1 = 0, s1 = 0;
        
        int[] arr = new int[path.size()];
        
        // 경로 출력
        System.out.print("Path: ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            arr[i] = path.get(i);
            if (i != path.size() - 1) {
                System.out.print(" -> ");
            }
        }
        
        double answer[] = new double[2];
        for(int i=1; i<arr.length; i++) {
        	answer[0] += tt[arr[i-1]][arr[i]];
        	answer[1] += sd[arr[i-1]][arr[i]];
        }
        System.out.println();
        return answer;
    }
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		set = new LinkedHashSet<>();
		GetData.main(null);
		App.solveMe();

		double m1, m2, s1, s2;
		n = GetData.nodenum;
		s = GetData.s-1;
		t = GetData.d-1;
		sd = new double[n][n];
		tt = new double[n][n];
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				sd[i][j] = GetData.StandardDeviation[i][j]*GetData.StandardDeviation[i][j];	
			}
		}
		tt = GetData.TravelTime;
		
		m1 = Dijkstra2.dijkstra(1, 0)[0];
		s1 = Dijkstra2.dijkstra(1, 0)[1];
		set.add(new Point(m1,s1));
		//System.out.println(Dijkstra2.dijkstra(1, 0)[0]);
		//System.out.println(m1);
		m2 = Dijkstra2.dijkstra(0, 1)[0];
		s2 = Dijkstra2.dijkstra(0, 1)[1];
		set.add(new Point(m2,s2));
		System.out.println(m2+" "+ s2);
		NEWSOL(m1,s1,m2,s2);
		
		Iterator<Point> iter = set.iterator();
		
		while(iter.hasNext()) {
			Point e = iter.next();
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
		
		System.out.println(set.size());
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter(filePath+fileName+fileType, true));
			fw.newLine();
			fw.write(Integer.toString(set.size()));
			fw.newLine();
			fw.write(Double.toString(time));
			
			fw.flush();
			fw.close();
		}
		catch(Exception a) {
			a.getStackTrace();
		}
		System.out.println("실행시간 = " + time + "초");
		System.exit(0);
	}
	public static void NEWSOL(double m1, double s1, double m2, double s2) throws IOException {
		double m3=0, s3=0;
		if(m1!=m2) {
			double tmp = -((s2-s1)/(m2-m1));
			m3 = Dijkstra2.dijkstra(tmp, 1)[0];
			s3 = Dijkstra2.dijkstra(tmp, 1)[1];
		}
		else {
			m3 = Dijkstra2.dijkstra(1,0)[0];
			s3 = Dijkstra2.dijkstra(1,0)[1];
		}
		if((m3!=m1|| s3!=s1) && (m3!=m2 ||s3!=s2)) {
			set.add(new Point(m3,s3));
			NEWSOL(m1,s1,m3,s3);
			NEWSOL(m2,s2,m3,s3);
		}
	}	

}
