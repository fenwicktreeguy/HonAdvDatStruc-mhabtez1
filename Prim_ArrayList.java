import java.util.*;

public class Prim_ArrayList<E>{
	
	int s_size;
	Object[] simul_list;

	public Prim_ArrayList(int s_size){
		this.s_size=s_size;
		simul_list = new Object[s_size];
	}

	public void initialize(){
		for(int i = 0; i < s_size; i++){
			simul_list[i]= (int)((Math.random())*10);
		}
	}
	public void getArraylist(){
		for(int j = 0; j < s_size; j++){
			System.out.print(simul_list[j] + " ");
		}
		System.out.println();
	}

	public void add(Object val){
		try {
			Object[] simul_nw = new Object[++s_size];
			for(int j = 0; j < s_size-1; j++){
				simul_nw[j]=simul_list[j];
			}
			simul_nw[s_size-1]=val;
			simul_list = new Object[s_size];
			simul_list = simul_nw;
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public void add(int idx, Object val){
		//System.out.println(s_size);
		try {
			Object[] simul_nw = new Object[++s_size];
			for(int i = 0; i <= idx-1; i++){
				simul_nw[i]=simul_list[i];
			}
			for(int i = idx+1; i < s_size; i++){
				simul_nw[i]=simul_list[i-1];
			}
			simul_nw[idx]=val;
			simul_list = new Object[s_size];
			simul_list = simul_nw;
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public void set(int idx, Object val){
		try {
			simul_list[idx]=val;
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public int getSize(){
		return s_size;
	}
	public void remove(int idx){
		try {
			Object[] simul_nw = new Object[--s_size];
			for(int j = 0; j <= idx; j++){
				simul_nw[j]=simul_list[j];
			}

			for(int j = idx+1; j <= s_size; j++){
				simul_nw[j-1]=simul_list[j];
			}
			simul_list= new Object[s_size];
			simul_list=simul_nw;
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}

class Runner{
	public static void main(String[] args){
		Prim_ArrayList<Integer> pm = new Prim_ArrayList<Integer>(10);
		pm.initialize();
		pm.getArraylist();
		pm.add(2,3);
		pm.getArraylist();
		pm.add(3,10);
		pm.getArraylist();
		pm.set(4,13);
		pm.getArraylist();
		pm.set(6,22);
		pm.getArraylist();
		pm.remove(5);
		pm.getArraylist();
		pm.remove(9);
		pm.getArraylist();


	}

}


