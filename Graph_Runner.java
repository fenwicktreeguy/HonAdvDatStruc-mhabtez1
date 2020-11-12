import java.io.*;
import java.net.*;
import java.util.*;


class Node<E>{
    public E vl;
    public Node nxt;
    public Node(E e, Node nxt){
        this.vl=e;
        this.nxt=nxt;
    }
}

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
            ++sz;
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
    public Node<E> remove_back(){
        int idx = 0;
        Node vls = head;
        while(vls.nxt!=null){
            //System.out.println(idx);
            if(idx==sz-2){break;}
            ++idx;
            vls=vls.nxt;
        }
        Node tmp = vls;
        tmp.nxt=null;
        cur_ptr=tmp;
        --sz;
        return tmp;
    }

}

class Graph<E>{
    LinkedList[] g;
    ArrayList<Integer> best_path;
    int min_path = 1000000000;
    public boolean[] vis;
    public Graph(int num_nodes) {
        g = new LinkedList[num_nodes];
        best_path = new ArrayList<Integer>();
        vis = new boolean[1000001];
        for(int i = 0; i < 1000001; i++){
            vis[i] = false;
        }
        min_path = 1000000000;
    }

    public void init() {
        for(int i = 0; i < g.length; i++){
            g[i] = new LinkedList<E>();
        }
    }

    //undirected edges
    public void addEdge(int u, int v) {
        g[u].add(v);
        g[v].add(u);
    }
    public void shortest_path(ArrayList<Integer> path, int src, int dst){
        for(int i = 0; i < path.size(); i++){
            System.out.print(path.get(i) + " ");
        }
        System.out.println();
        if(src==dst){
            path.add(dst);
             if(Math.min(min_path,path.size())==path.size()){
                 best_path.clear();
                 best_path = path;
                 min_path = path.size();
             }
        }
        if(!vis[src]){
            vis[src]=true;
            LinkedList tmp = g[src];
            path.add(src);
            Node<Integer> srt = tmp.head;
            while(srt != null){
                ArrayList<Integer> cloned = (ArrayList)path.clone();
                shortest_path(cloned, srt.vl, dst);
                srt = srt.nxt;
            }
        } else {
            System.out.println("VISITED");
        }
    }
}

public class Graph_Runner{
    public static void main(String[] args){
        Graph gf = new Graph(8);
        gf.init();
        gf.addEdge(0, 1);
        gf.addEdge(0, 3);
        gf.addEdge(1, 2);
        gf.addEdge(3, 4);
        gf.addEdge(3, 7);
        gf.addEdge(4, 5);
        gf.addEdge(4, 6);
        gf.addEdge(4, 7);
        gf.addEdge(5, 6);
        gf.addEdge(6, 7);
        for(LinkedList l : gf.g) {
            l.printContents();
        }
        ArrayList<Integer> start = new ArrayList<Integer>();
        gf.shortest_path(start, 0, 7);
        for(int i = 0; i < gf.best_path.size(); i++){
            System.out.print(gf.best_path.get(i) + " ");
        }
        System.out.println();

    }
}


