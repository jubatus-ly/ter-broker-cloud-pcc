package Customer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Components.Component;
import Components.Relations;
import kodkod.instance.Bounds;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;


public class Customer {
	
	
	private String CID;
	private Set<Component> Comps;
	private List<ArrayList<String>> Flow;
	private List<ArrayList<String>> Concurrence;
	private Set<String> Alone;
	
	public Customer(String cID) {
		super();
		CID = cID;
		Comps = new HashSet<Component>();
		Flow = new ArrayList<ArrayList<String>>();
		Concurrence = new ArrayList<ArrayList<String>>();
		Alone = new HashSet<String>();
	}

	public Set<String> getAlone() {
		return Alone;
	}


	public void setAlone(Set<String> alone) {
		Alone = alone;
	}


	public String getCID() {
		return CID;
	}

	public void setCID(String cID) {
		CID = cID;
	}

	public Set<Component> getComps() {
		return Comps;
	}

	public void setComps(Set<Component> comps) {
		Comps = comps;
	}

	public List<ArrayList<String>> getFlow() {
		return Flow;
	}

	public void setFlow(List<ArrayList<String>> flow) {
		Flow = flow;
	}

	public List<ArrayList<String>> getConcurrence() {
		return Concurrence;
	}

	public void setConcurrence(List<ArrayList<String>> concurrence) {
		Concurrence = concurrence;
	}
	
	public List<String> Customer_universe() {
		final List<String> atoms = new ArrayList<String>();
		atoms.add("Customer"+this.getCID());
		for(Component cmp : this.getComps()) {
			atoms.add(cmp.getCmpID());
			//atoms.add("CustomerCmp"+this.getCID()+"-"+cmp.getCmpID());
		}
		return atoms;
	}
	
	public TupleSet setAlone(Universe U, Set<String> Vms) {
		TupleSet clientAlone = U.factory().noneOf(1);
		for(String set : Vms){
			System.out.println("Debug : " + set.toString());
			clientAlone.add(U.factory().tuple(set));
		}
		
		return clientAlone;
	}
	
	public TupleSet setFlow(Universe U, List<ArrayList<String>> Vms) {
		TupleSet clientFlow = U.factory().noneOf(2);
		for(ArrayList<String> set : Vms){
			System.out.println("Debug : " + set.toString());
			clientFlow.add(U.factory().tuple(set.get(0),set.get(1)));
		}
		
		return clientFlow;
	}
	
	public TupleSet setConcurrence(Universe U, List<ArrayList<String>> Vms) {
		TupleSet clientconc = U.factory().noneOf(2);
		for(ArrayList<String> set : Vms){
			//System.out.println("Debug : " + set.toString());
			clientconc.add(U.factory().tuple(set.get(0),set.get(1)));
		}
		return clientconc;
	}
	
	@SuppressWarnings("serial")
	public void AddConc(String vmi, String vmj){
		//String Vmi = "CustomerCmp"+this.getCID()+"-"+vmi;
		//String Vmj = "CustomerCmp"+this.getCID()+"-"+vmj;
		ArrayList<String> Couple = new ArrayList<String>(){{
			add(vmi);
			add(vmj);}};
		
		if(!this.Concurrence.contains(Couple))
			this.Concurrence.add(Couple);
	}
	
	@SuppressWarnings("serial")
	public void DelConc(String vmi, String vmj){
		//String Vmi = "CustomerCmp"+this.getCID()+"-"+vmi;
		//String Vmj = "CustomerCmp"+this.getCID()+"-"+vmj;
		ArrayList<String> Couple = new ArrayList<String>(){{
			add(vmi);
			add(vmj);}};
			ArrayList<String> Couple2 = new ArrayList<String>(){{
			add(vmj);
			add(vmi);}};
		if(this.Concurrence.contains(Couple))
			this.Concurrence.remove(Couple);
		if(this.Concurrence.contains(Couple2))
			this.Concurrence.remove(Couple2);
	}
	
	@SuppressWarnings("serial")
	public void AddFlow(String vmi, String vmj){
		//String Vmi = "CustomerCmp"+this.getCID()+"-"+vmi;
		//String Vmj = "CustomerCmp"+this.getCID()+"-"+vmj;
		ArrayList<String> Couple = new ArrayList<String>(){{
			add(vmi);
			add(vmj);}};
		if(!this.Flow.contains(Couple))
			this.Flow.add(Couple);
	}
	
	@SuppressWarnings("serial")
	public void DelFlow(String vmi, String vmj){
		//String Vmi = "CustomerCmp"+this.getCID()+"-"+vmi;
		//String Vmj = "CustomerCmp"+this.getCID()+"-"+vmj;
		ArrayList<String> Couple = new ArrayList<String>(){{
			add(vmi);
			add(vmj);}};
		if(this.Flow.contains(Couple)){
			this.Flow.remove(Couple);
		}
			
	}
	
	public void AddAlone(String Vmi){
		//String vmi = "CustomerCmp"+this.getCID()+"-"+Vmi;
		if(!this.Alone.contains(Vmi))
			this.Alone.add(Vmi);
	}
	
	public void DelAlone(String vmi){
		//String Vmi = "CustomerCmp"+this.getCID()+"-"+vmi;
		if(this.Alone.contains(vmi)){
			this.Alone.remove(vmi);
		}
			
	}

public Bounds Customer_bounds(Relations R, Bounds b) {
		
		b.boundExactly(R.CurrentCustomer, b.universe().factory().range(b.universe().factory().tuple("Customer"+ this.getCID()),b.universe().factory().tuple("Customer"+ this.getCID())));
		b.boundExactly(R.Customer, b.universe().factory().range(b.universe().factory().tuple("Customer"+ this.getCID()),b.universe().factory().tuple("Customer"+ this.getCID())));

		
		TupleSet vms = b.universe().factory().noneOf(1);
		for (Component v : this.getComps()) {
			vms.add(b.universe().factory().tuple(v.getCmpID()));
		}
		
		if(!b.relations().contains(R.Comp)){
			b.boundExactly(R.Comp, vms);		
			}
		
		
		TupleSet myclientVms = b.universe().factory().noneOf(2);
		for(Component v : this.getComps()){
			myclientVms.add(b.universe().factory().tuple("Customer"+ this.getCID(), v.getCmpID()));
		}
		b.boundExactly(R.owns, myclientVms);
		
		if(this.getConcurrence() != null)
		{
		b.boundExactly(R.Concurrence, this.setConcurrence(b.universe(), this.getConcurrence()));
		
		}
		
		if(!this.getFlow().isEmpty()){
			b.boundExactly(R.Flow, this.setFlow(b.universe(), this.getFlow()));
			
			}
		
		if(!this.getAlone().isEmpty()){
			b.boundExactly(R.Alone, this.setAlone(b.universe(), this.getAlone()));
			
			}
		return b;
	}

	public String ExportCustomerData() {
		String data = new String();
		data = "param nbcomp := " + this.getComps().size() + "; \n";
		data = data + "set Comp := ";
		for(Component c : this.getComps()) {
			data = data + c.getCmpID() + " \n";
		}
		data = data + "; \n";
		
		data = data + "set Alone := ";
		for(String a : this.getAlone()) {
			data = data + a + " \n";
		}
		data = data + "; \n";
		
		data = data + "set Flow := ";
		for(ArrayList<String> f : this.getFlow()) {
			data = data + f.get(0) + " " + f.get(1) + " \n";
		}
		data = data + "; \n";
		
		data = data + "set Concur := ";
		for(ArrayList<String> f : this.getConcurrence()) {
			data = data + f.get(0) + " " + f.get(1) + " \n";
		}
		data = data + "; \n";
		return data;
	}

}
