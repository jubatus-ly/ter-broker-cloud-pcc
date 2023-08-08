package Provider;

import java.util.List;
import java.util.Set;



public class Provider {
	
	private List<InstanceType> ITs;
	private Set<String> hosted;
	private String PID;
	
	public Provider(String pID) {
		super();
		PID = pID;
	}
	
	
	
	public Provider( String pID,List<InstanceType> cs) {
		super();
		ITs = cs;
		PID = pID;
	}
	

	public List<InstanceType> getITs() {
		return ITs;
	}
	
	/*public TupleSet setITs(Universe U, List<InstanceType> ITs) {
		TupleSet ITsTuples = U.factory().noneOf(1);
		for(InstanceType set : ITs){
			//System.out.println("Debug : " + set.toString());
			ITsTuples.add(U.factory().tuple(set.getInstTypeID()));
		}
		return ITsTuples;
	}*/

	public void setITs(List<InstanceType> iTs) {
		ITs = iTs;
	}

	public Set<String> getHosted() {
		return hosted;
	}

	public void setHosted(Set<String> hosted) {
		this.hosted = hosted;
	}

	public String getPID() {
		return PID;
	}
	public void setPID(String pID) {
		PID = pID;
	}
	
	/*public List<String> Provider_universe() {
		final List<String> atoms = new ArrayList<String>();
		atoms.add("Provider"+this.getPID());
		for(InstanceType pi : this.getITs()){
			atoms.add(pi.getInstTypeID());
		}
		
		return atoms;
	}
	
	
	public TupleSet HostedVms(Universe U,Set<String> vms){
		TupleSet hostedvms = U.factory().noneOf(1);
		for(String set : vms){
			hostedvms.add(U.factory().tuple(set));
		}
		return hostedvms;
	}*/
	
	

}
