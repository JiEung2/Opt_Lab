package Data;

import java.io.IOException;

public class ABC {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//App.solveMe();
		//SUB.solveMe();
		long start = System.currentTimeMillis();
		ProbabilitySP.solve();
		long end = System.currentTimeMillis();
		double time = (end - start) / 1000.0;
		
		System.out.println("실행시간2 = " + time + "초");
	}

}


