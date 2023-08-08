package Components;



import kodkod.ast.Formula;
import kodkod.ast.Relation;
import kodkod.ast.Variable;


public class Relations {
	
	public Relation Comp;
	
	public Relation Flow;
	public Relation Concurrence;
	public Relation Alone;
	
	public Relation Customer;
	public Relation owns;
	
	public Relation CurrentCustomer;
	
	public Relation Location;
	
	
	
	
	public Relations() {
		super();
		Customer = Relation.unary("Customer");
		CurrentCustomer = Relation.unary("CurrentCustomer");
		owns = Relation.binary("owns");
		Comp = Relation.unary("Comp");
		Location = Relation.unary("Location");
		Alone = Relation.unary("Alone");
		Flow = Relation.binary("Flow");
		Concurrence = Relation.binary("Concurrence");
	}
	
	
	public Relations(Relation c, Relation own) {
		super();
		Customer = c; 
		CurrentCustomer = Relation.unary("CurrentCustomer");
		owns = own;
		Comp = Relation.unary("Comp");
		Location = Relation.unary("Location");
		Alone = Relation.unary("Alone");
		Flow = Relation.binary("Flow");
		Concurrence = Relation.binary("Concurrence");
	}
	
	public Formula CustDecls() {
		
		return (owns.in(Customer.product(Comp))).and(Concurrence.in(Comp.product(Comp)))
				.and(Alone.in(Comp))
				.and(Flow.in(Comp.product(Comp)));
			
	}
	
	public Formula ComponentsDecls() {
		return (owns.in(Customer.product(Comp)));
	}
	
	public Formula RelationsDecls() {
		return (Concurrence.in(Comp.product(Comp))).and(Flow.in(Comp.product(Comp))).and(Alone.in(Comp));
	}
	
	
	public Formula Concurrence_rules(){
		final Variable cmpi = Variable.unary("cmpi");
		final Variable cmpj = Variable.unary("cmpj");
		return (cmpi.product(cmpj).in(Concurrence)).implies(cmpj.in(cmpi.join(Flow.closure())).not().
						and(cmpi.in(cmpj.join(Flow.closure())).not()))
						.forAll(cmpi.oneOf(Comp).and(cmpj.oneOf(Comp)));
	}
	
	public Formula Flow_rule() {
		final Variable cmpi = Variable.unary("cmpi");
		final Variable cmpj = Variable.unary("cmpj");
		return (cmpi.product(cmpj).in(Flow)
				.and(cmpj.product(cmpi).in(Flow).not())).implies(cmpi.in(cmpj.join(Flow.closure())).not())				
				.forAll(cmpi.oneOf(Comp).and(cmpj.oneOf(Comp)));
	}
	
	public Formula InverseDeterministic(Relation v, Relation c, Relation R) {
		final Variable ci = Variable.unary("ci");
		final Variable cj = Variable.unary("cj");
		final Variable cmp = Variable.unary("cmp");
		return (ci.product(cmp).in(R)).and(cj.product(cmp).in(R)).implies(ci.eq(cj))
				.forAll(cmp.oneOf(v).and(ci.oneOf(c)).and(cj.oneOf(c)));
	}
	
	public Formula Deterministic(Relation v, Relation t, Relation R) {
		final Variable ti = Variable.unary("ti");
		final Variable tj = Variable.unary("tj");
		final Variable cmp = Variable.unary("cmp");
		return (cmp.product(ti).in(R)).and(cmp.product(tj).in(R)).implies(ti.eq(tj))
				.forAll(cmp.oneOf(v).and(ti.oneOf(t)).and(tj.oneOf(t)));	
	}
	
	public Formula SameOwner(Relation R) {
		final Variable cmpi = Variable.unary("cmpi");
		final Variable cmpj = Variable.unary("cmpj");
		final Variable c = Variable.unary("c");
		return (cmpi.product(cmpj).in(R)).implies((c.product(cmpi).in(owns)).and(c.product(cmpj).in(owns)))
				.forAll(cmpi.oneOf(Comp).and(cmpj.oneOf(Comp)).and(c.loneOf(Customer)));
	}
	
	public Formula CurrentOwner(Relation R) {
		final Variable cmpi = Variable.unary("cmpi");
		final Variable cmpj = Variable.unary("cmpj");
		return (cmpi.product(cmpj).in(R)).implies((CurrentCustomer.product(cmpi).in(owns))
				.and(CurrentCustomer.product(cmpj).in(owns)))
				.forAll(cmpi.oneOf(Comp).and(cmpj.oneOf(Comp)));
	}
	
	public Formula SystemInvariant() {
		return SameOwner(Flow).and(SameOwner(Concurrence));
		
	}
	
	public Formula Appformulas() {
		return CurrentOwner(Flow).and(CurrentOwner(Concurrence))
				.and(Concurrence_rules()).and(Flow_rule());
	}
	
	

}
