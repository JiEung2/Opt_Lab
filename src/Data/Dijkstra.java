package Data;
import java.util.*;
//뭔가 다 되는데 26 ~ 5만 안됨
class Edge implements Comparable<Edge>{
	public int vertex; //정점까지의
	public double d; //거리
	Edge(int vertex, double d){
		this.vertex = vertex;
		this.d = d;
	}
	@Override
	public int compareTo(Edge ob) {
		return Double.compare(this.d, ob.d);
	}
}


public class Dijkstra {
	static int n;
	static int s;
	static int t;
	static double [] D;
	static ArrayList<ArrayList<Edge>> graph;
	
	public void dijkstra(int v){
		PriorityQueue<Edge> pQ = new PriorityQueue<>();
		pQ.offer(new Edge(v,0));
		D[v] = 0;
		while(!pQ.isEmpty()) {
			Edge tmp = pQ.poll();
			int cn = tmp.vertex; //현재 방문 중인 노드 current node
			double cd = tmp.d; //현재 거리
			if(cd>D[cn]) continue;
			for(Edge ob : graph.get(cn)) {
				if(D[ob.vertex]>cd+ob.d) {
					D[ob.vertex] = cd+ob.d;
					pQ.offer(new Edge(ob.vertex, cd+ob.d));
				}
			}
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetData.main(null);
		Dijkstra di = new Dijkstra();
		graph = new ArrayList<ArrayList<Edge>>();
		n = GetData.nodenum;
		s = GetData.s - 1;
		t = GetData.d - 1;
		
		for(int i=0; i<n; i++) {
			graph.add(new ArrayList<Edge>());
		}
		
		D = new double[n];
		Arrays.fill(D, Double.MAX_VALUE);
		double[][] tt = GetData.TravelTime;
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				if(i!=j)graph.get(i).add(new Edge(j,tt[i][j]));
			}
		}
		di.dijkstra(s);
		System.out.println(D[t]);
	}

}
