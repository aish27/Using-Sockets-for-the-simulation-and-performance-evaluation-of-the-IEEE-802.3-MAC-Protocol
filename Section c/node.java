
public class node {
	private double pre;
	private double now;
	private double next;
	
	public node(double lam){
		this.pre=0;
		this.now=var(lam);
		this.next=now+var(lam);
	}
	
	public node(double pre, double now, double next ){
		this.pre=pre;
		this.now=now;
		this.next=next;
	}
	
	public boolean check(){
		if(now - pre>1 && next - now>1){
			return true;
		}	
		return false;	
	}
		
	public node getNext(double lam){
		return new node(now,next,next+var(lam));				
	}
	
	private double var(double lambda){
		double val= -Math.log(Math.random())*lambda;
		return val;
	}
	
	public double getNow(){
		return now;
	}
	
}
