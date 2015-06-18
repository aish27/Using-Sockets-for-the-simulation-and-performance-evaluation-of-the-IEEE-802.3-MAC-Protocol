import java.util.Arrays;


public class nodeTest {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test(4);
		test(6);
		test(8);
		test(10);
		test(12);
		test(14);
		test(16);
		test(18);
		test(20);
	}
	
	static void test(double lambda){
		int N=50;
		double sum=0;
		double [] l=new double[N];
		
		for (int i=0; i<N; i++){
			double num= run(lambda);
			l[i]=num;
			sum = sum+ num;
		}
		Arrays.sort(l);
			
		System.out.println(lambda + ":        "+sum/N+ "        "+l[0]);		
	}
	
	static double run(double lambda){
		node n = new node(lambda);	
		for(int i=0;i<20;i++){
			if(n.check()){
				break;
			}
			n=n.getNext(lambda);
		}	
		return n.getNow();
	}
}
