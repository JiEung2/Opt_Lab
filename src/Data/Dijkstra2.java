package Data;

import java.util.ArrayList;
import java.util.*;

public class Dijkstra2 {
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
	
	static void dijkstra(double tt[][], int s, int n, int t) {
		double d[] = new double[n];
		boolean T[] = new boolean[n];
		int p[] = new int[n];
		
		Arrays.fill(d, Double.MAX_VALUE);
		Arrays.fill(T, false);
		Arrays.fill(p, -1);
		
		d[s] = 0;
		
		for(int i = 0; i<n-1; i++) {
			int u = findMinDistance(d, T, n);
			T[u] = true;
			//가장 짧은 거리를 가진 노드를 추가
			for(int v = 0; v <n; v++) {
				if(!T[v] && tt[u][v]!=0 && d[u]!=Double.MAX_VALUE && d[u]+tt[u][v] < d[v]) {
					d[v] = d[u] + tt[u][v];
					p[v] = u;
				}
			}
		}
		System.out.println(s+" "+t+"의 최단거리는");
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
        
        double m1 = 0, s1 = 0;
        
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
        
        /*for(int i=0; i<arr.length-1; i++) {
        	m1 += GetData.TravelTime[arr[i]][arr[i+1]];
        	s1 += GetData.StandardDeviation[arr[i]][arr[i+1]];
        }
        System.out.println();
        System.out.println("m1 = "+m1+" s1 = "+ s1);
        int c1 = 1, c2 = 0;
        System.out.println(m1*c2+" "+c1*s1);
        */
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		GetData.main(null);
		int n = GetData.nodenum;
		int s = GetData.s - 1;
		int t = GetData.d - 1;
		double[][] tt = GetData.TravelTime;
		
		Dijkstra2.dijkstra(tt, s, n, t);
	}

}
