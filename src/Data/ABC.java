package Data;

import java.io.IOException;

public class ABC {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//App.solveMe();
		//SUB.solveMe();
		for(int i=1; i<=5; i++) {
			for(int j=1; j<=5; j++) {
				long start = System.currentTimeMillis();
				ProbabilitySP.solve(i,j);
				long end = System.currentTimeMillis();
				double time = (end - start) / 1000.0;
				
				System.out.println("실행시간2 = " + time + "초");
			}
		}
	}

}


