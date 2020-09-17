import java.util.*;


class LinkedList<E>{
	Node head;
	Node cur_ptr;//will by default be the same as the head
	int sz = 0;
	
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

class Queue<E>{
	LinkedList<E> llm = new LinkedList();
	int M_SIZE;
	int c_size;
	public Queue(int M_SIZE){
		this.M_SIZE = M_SIZE;
		c_size = 0;
	}
	public void insert(E e){
		while(c_size>=M_SIZE){
			llm.removeFront();
			--c_size;
		}
		llm.addToFront(e);
		++c_size;
	}
	public void add(E e){
		llm.addToFront(e);
		++c_size;
		
	}
	public void remove(E e){
		llm.removeFront();
		--c_size;
	}
	public E get(int idx){
		Node<E> cur = llm.head;
		int c_idx = 0;
		while(cur.nxt != null){
			if(c_idx==idx){break;}
			++c_idx;
			cur=cur.nxt;
		}
		return cur.vl;
	}
	public boolean isFull(){return c_size==M_SIZE;}
	public void printContents(){llm.printContents();}
	public boolean contains(E e){return llm.contains(e);}
	public int getSize(){return c_size;}

}


public class LinkedListTester{
	public static void main(String[] args){
		Queue<Integer> q = new Queue(10);
		int START_V = (int)(Math.random() * 20);
		//LinkedList<Integer> tst = new LinkedList<Integer>();
		for(int i = 0; i < 20; i++){
			int rm = (int)(Math.random() * 20);
			q.add(rm);
			q.printContents();
		}
		q.printContents();
		q.insert(12);
		q.printContents();
		int value = q.get(4);
		System.out.println(value);

		/*
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
		boolean cont = tst.contains(50);
		String rs = cont?"Yes":"No";
		System.out.println(rs);
		*/
	}
}

