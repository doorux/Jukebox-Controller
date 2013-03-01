package jukeController;

import java.io.File;
import java.util.ArrayList;

public class QueueThing {
	private ArrayList<File> q;
	private int i;
	
	public QueueThing(){
		i = 0;
		q = new ArrayList<File>();
	}
	
	public QueueThing(File[] j){
		i = 0;
		q = new ArrayList<File>();
		int i=0;
		while(j[i] != null){
			if(j[i].isFile())
				q.add(j[i]);
				//q.add(j[i].getAbsolutePath());
		}
		
	}
	
	public String pop(){
		return q.get(i).getAbsolutePath();
	}
	
	public String getFileLocation(int index){
		return q.get(index).getAbsolutePath();
	}
	
	public String next(){
		if(i>=q.size()-1){
			i=0;
		}else{
			++i;
		}
		return q.get(i).getAbsolutePath();
	}
	
	public String previous(){
		if(i==0){
			i=q.size()-1;
		}else{
			--i;
		}
		return q.get(i).getAbsolutePath();
	}
	
	public void push(File[] j){
		for(int i=0; i<j.length; i++){
			if(j[i].isFile()){
				q.add(j[i]);
				//q.add(j[i].getAbsolutePath());
				System.out.println("Adding "+j[i].getName()+
						"\nSize of q is: "+q.size());
			}
		}
	}
	
	public void push(File o){
		if(o.isFile()){
			System.out.println("Adding "+o.getName());
			q.add(o);
			//q.add(o.getAbsolutePath());
		}
	}
	
	public boolean isEmpty(){
		if (q.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	public String[] getPlaylist(){
		
		String[] lol = new String[q.size()];
		for(int i=0; i<lol.length; i++){
			lol[i] = q.get(i).getName();
		}
		
		return lol;
		//return q.toArray();
	}
	
	
}
