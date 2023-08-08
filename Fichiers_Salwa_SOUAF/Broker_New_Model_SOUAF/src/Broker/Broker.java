package Broker;

import java.util.HashSet;
import java.util.Set;

import Components.Component;
import Components.Relations;
import Customer.Customer;
import Provider.InstanceType;
import Provider.Provider;
import Provider.Providers;
import Tools.Compatible;
import kodkod.ast.Formula;
import kodkod.ast.Relation;
//import kodkod.engine.Proof;
import kodkod.engine.Solution;
import kodkod.engine.Solver;
import kodkod.engine.satlab.SATFactory;
//import kodkod.engine.ucore.RCEStrategy;
import kodkod.instance.Bounds;
import kodkod.instance.TupleSet;
//import kodkod.util.nodes.Nodes;
//import kodkod.util.nodes.PrettyPrinter;

public class Broker {
	private Set<Customer> Customers;
	private Providers Federation;
	final Solver solver;
	
	public Broker() {
		super();
		Customers = new HashSet<Customer>();
		Federation = new Providers();
		solver = new Solver();
		//solver.options().setSolver(SATFactory.MiniSatProver);
		solver.options().setSolver(SATFactory.Glucose);
		solver.options().setLogTranslation(2);
		solver.options().setCoreGranularity(3);
	}
	
	public Set<Customer> getCustomers() {
		return Customers;
	}
	public void setCustomers(Set<Customer> customers) {
		Customers = customers;
	}
	
	public Providers getFederation() {
		return Federation;
	}

	public void setFederation(Providers federation) {
		Federation = federation;
	}

	public Formula SysDeclarations(Relations relations) {
		return relations.ComponentsDecls().and(relations.RelationsDecls());
	}
	
	public Formula AppVerifyFormula(Relations relations) {
		return relations.Appformulas();
	}
	
	public Formula SysVerifyFormula(Relations relations) {
		return relations.SystemInvariant();
	}
	
	
	public Bounds Customers_bounds(Relations relations, Bounds b, Set<Customer> Cs) {
		final TupleSet myclientVms = b.universe().factory().noneOf(2);
		final TupleSet flows = b.universe().factory().noneOf(2);
		final TupleSet alones = b.universe().factory().noneOf(1);
		final TupleSet concs = b.universe().factory().noneOf(2);
		for(Customer c: Cs) {
		b.boundExactly(relations.Customer, b.universe().factory().range(b.universe().factory().tuple("Customer"+ c.getCID()),b.universe().factory().tuple("Customer"+ c.getCID())));

		
		TupleSet vms = b.universe().factory().noneOf(1);
		while( c.getComps().iterator().hasNext()) {
			vms.add(b.universe().factory().tuple("CustomerCmp"+ c.getCID()+"-"+c.getComps().iterator().next().getCmpID()));
		}
		//vms.add(f.tuple(""));
		if(!b.relations().contains(relations.Comp)){
			b.boundExactly(relations.Comp, vms);		
			}
		
		for(Component v: c.getComps()){
			//myclientVms.add(b.universe().factory().tuple("Customer"+ c.getCID(), "CustomerCmp"+ c.getCID()+"-"+v.getCmpID()));
			myclientVms.add(b.universe().factory().tuple("Customer"+ c.getCID(), v.getCmpID()));
		}
		
		
		if(c.getConcurrence() != null){
			concs.addAll(c.setConcurrence(b.universe(), c.getConcurrence()));
			}
		
		if(c.getFlow() != null){
			flows.addAll(c.setFlow(b.universe(), c.getFlow()));
			}
		
		if(c.getAlone() != null){
			alones.addAll(c.setAlone(b.universe(), c.getAlone()));
			}
		
		}
		
		b.boundExactly(relations.owns, myclientVms);

		b.boundExactly(relations.Concurrence, concs);
		//System.out.println("******************** Concurrence TupleSet :"+this.setConc(C, R, this.ConcVms).toString());
		b.boundExactly(relations.Flow, flows);
		b.boundExactly(relations.Alone, alones);
			

		//System.out.println("Bounded relations in customer"+b.relations().toString());
		return b;
	}
	
	
	
	
	
	public Solution VerifyCustomer(Relations R, Bounds b,Customer c) {
		
		
		Solution sol = solver.solve(R.CustDecls().and(this.AppVerifyFormula(R)), c.Customer_bounds(R,b));
		//System.out.println("Debug : " + sol.toString());
		if (sol.instance()==null) { 	
			/*final Proof proof = sol.proof();
			//System.out.println("initial core: " + proof.highLevelCore().size());
			//System.out.print("\nminimizing core ... ");
			proof.minimize(new RCEStrategy(proof.log()));
			final Set<Formula> core = Nodes.minRoots(R.CustDecls().and(this.AppVerifyFormula(R)), proof.highLevelCore().values());
			System.out.println("minimal core: " + core.size());
			Set<Formula> qs = new HashSet<Formula>();
			for(Formula u : core) { 
				System.out.println(PrettyPrinter.print(u, 2, 100));
				if(u.getClass().getName().contains("Quantif"))
					qs.add(u);
			}
			*/
			
			System.out.println("No Instance");
		} else {
			System.out.println(sol);
		}
		
		return sol;
	}
	
	
	public Set<String> Vms_LowerBounds(Providers fed){
		Set<String> vms = new HashSet<>();
		
		return vms;
	}
	Relation cust = Relation.unary("Customer");
	
	public String ExportCompData() {
		String data = new String();
		data = "param Alpha:= \n";
		for(Customer Cus : this.Customers){
			for(Component c : Cus.getComps()) {
				System.out.println("DEBUG inside Broker I am a component looking for compatibility : " + c.getCmpID() + " " + c.getVcpu() + " " + c.getMemory());
				Compatible comp = new Compatible(c);
				Set<InstanceType> cit = comp.getITs();
				for(Provider p : this.Federation.getProviders()) {
					for(int i = 1 ; i < p.getITs().size() ; i ++) {					
						data = data + "["+c.getCmpID() +"," + p.getPID() + "," + i + "] := " ;
							if(cit.contains(p.getITs().get(i))) {
								System.out.println(" Contained : " + p.getITs().get(i).toString());
								data = data + "1 \n";
							}else{
								data = data + "0 \n";
							}
							}
						
					
				}
				 
			}
		}
		data = data + "; \n";
		
		return data;
	}
	
}
