import java.util.*;

class LinkedList<E>{
	Node head;
	Node cur_ptr;//will by default be the same as the head
	int sz = 0;
	
	//just initialize what value of head and cur_ptr will be
	//adds to end, presumably (functional)
	//
	
	/*
	public LinkedList(Node<E> str){
		head=str;
		cur_ptr=str;
	}
	*/
	public void add(E e){
		if(sz==0){
			Node<E> tmp = new Node(e,null);
			head=tmp;
			cur_ptr=tmp;
			++sz;
		} else {
			Node nw = new Node(e,null);
			cur_ptr.nxt = nw;
			cur_ptr= nw;
		}
	}
	//sets value at some given Node to be another value (functional)
	public void set(int index, E e){
		try{
			int c_idx = 0;
			Node cur_vl = head;
			while(cur_vl != null){
				if(c_idx == index - 1){
					break;
				}
				++c_idx;
				cur_vl = cur_vl.nxt; 
			}	
			Node new_one = new Node(e, (cur_vl.nxt).nxt );
			cur_vl.nxt = new_one;
			cur_ptr = cur_vl;
		} catch(Exception exp){
			exp.printStackTrace();
		}
	}
	//adds to front (functional)
	public void addToFront(E e){
		try{
			Node nw = new Node(e, head);
			nw.nxt = head;
			cur_ptr=head;
			head=nw;
			sz++;
		} catch(Exception exp){
			exp.printStackTrace();
		}
	}

	//removes front node(functional)
	public Node<E> removeFront(){
		Node store = head;
		head = head.nxt;
		--sz;
		return store;
	}
	//determines if a value is contained in our linked list(functional)
	public boolean contains(E e){
		Node see = head;
		while(see.nxt != null){
			if(see.vl==e){return true;}
			see = see.nxt;
		}
		return false;
	}
	//adds to some arbitrary index in our linked list (functional)
	public void add(int index, E e){
		try{
			Node cr = head;
			int c_idx = 0;
			while(cr.nxt != null){
				if(c_idx == index-1){
					break;
				}
				++c_idx;
				cr=cr.nxt;
			}	
			Node cr_two = new Node(e, (cr.nxt));
			//System.out.println( ((cr.nxt).nxt).vl );
			cr.nxt = cr_two;
			cur_ptr = cr;
		} catch(Exception exp){
			exp.printStackTrace();
		}
	}
	//prints content of linked list for testing
	public void printContents(){
		Node cur_ptr = head;
		while(cur_ptr != null){
			System.out.print(cur_ptr.vl + " ");
			cur_ptr = cur_ptr.nxt;
		}
		System.out.println();
	}

	public Node<E> remove(int index){
		Node cur_n = head;
		int idx = 0;
		while(cur_n != null){
			if(idx==index-1){break;}
			++idx;
			cur_n = cur_n.nxt;
		}
		Node tmp = cur_n;
		cur_n.nxt = (cur_n.nxt).nxt;
		cur_ptr = cur_n;
		return (tmp.nxt);
	}
	public int getSize(){return sz;}

}

class Node<E>{
	public E vl;
	public Node nxt;
	public Node(E e, Node nxt){
		vl=e;
		this.nxt=nxt;
	}
}


public class LinkedListTester{
	public static void main(String[] args){
		int START_V = (int)(Math.random() * 20);
		LinkedList<Integer> tst = new LinkedList<Integer>();
		for(int i = 0; i < 20; i++){
			int rm = (int)(Math.random() * 20);
			tst.add(rm);
			tst.printContents();
		}
		
		tst.set(3,70);
		tst.printContents();
		tst.set(5,60);
		tst.printContents();
		tst.set(7,50);
		tst.printContents();
		tst.add(6,27);
		tst.printContents();
		tst.add(15,-26);
		tst.printContents();
		tst.removeFront();
		tst.printContents();
		tst.addToFront(127);
		tst.printContents();
		tst.remove(3);
		tst.printContents();
		boolean cont = tst.contains(127);
		String rs = cont?"Yes":"No";
		System.out.println(rs);
	}
}
