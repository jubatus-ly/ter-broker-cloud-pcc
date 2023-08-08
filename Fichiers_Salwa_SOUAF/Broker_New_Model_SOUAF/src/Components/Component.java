package Components;


public class Component {
	
	private String CmpID;
	private int vcpu;
	private int memory;
	private String os;
	private String location;
	
	public Component(String cID, int vcpu, int memory, String os, String location) {
		super();
		this.CmpID = cID;
		this.vcpu = vcpu;
		this.memory = memory;
		this.os = os;
		this.location = location;
	}

	public Component() {
		super();
	}

	public String getCmpID() {
		return CmpID;
	}

	public void setCmpID(String cmpID) {
		CmpID = cmpID;
	}

	public int getVcpu() {
		return vcpu;
	}

	public void setVcpu(int vcpu) {
		this.vcpu = vcpu;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/*public TupleSet setHasType(Universe U, Set<String> Vms, List<InstanceType> ITs) {
		TupleSet vmInstanceType = U.factory().noneOf(2);
		for(String v : Vms){
			//System.out.println("Debug : " + set.toString());
			for(InstanceType IT : ITs)
				vmInstanceType.add(U.factory().tuple(v,IT.getInstTypeID()));
		}
		return vmInstanceType;
	}*/

	
	
	
}
