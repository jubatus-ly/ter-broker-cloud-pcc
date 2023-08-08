package Provider;

public class InstanceType {
	
	private String InstTypeID;
	private int vCPU;
	private int Memory;
	private String OS;
	private String Location;
	private double Price;
	
	public InstanceType(String pNID) {
		super();
		InstTypeID = pNID;
	}
	
	public InstanceType(String instTypeID, int vCPU, int memory, String oS, String location, double price) {
		super();
		InstTypeID = instTypeID;
		this.vCPU = vCPU;
		Memory = memory;
		OS = oS;
		Location = location;
		Price = price;
	}

	public int getvCPU() {
		return vCPU;
	}

	public void setvCPU(int vCPU) {
		this.vCPU = vCPU;
	}

	public int getMemory() {
		return Memory;
	}

	public void setMemory(int memory) {
		Memory = memory;
	}

	public String getOS() {
		return OS;
	}

	public void setOS(String oS) {
		OS = oS;
	}
	
	public String getInstTypeID() {
		return InstTypeID;
	}


	public void setInstTypeID(String instTypeID) {
		InstTypeID = instTypeID;
	}

	public String getLocation() {
		return Location;
	}


	public void setLocation(String location) {
		Location = location;
	}

	public double getPrice() {
		return Price;
	}

	public void setPrice(double price) {
		this.Price = price;
	}
	
	@Override
	public boolean equals(Object o){
		  if(o instanceof InstanceType){
		    InstanceType obj = (InstanceType) o;
		    return this.getInstTypeID().equals(((InstanceType) obj).getInstTypeID()) 
					& this.getLocation().equals(((InstanceType) obj).getLocation())
					& this.getOS().equals(((InstanceType) obj).getOS());
		  }
		  return false;
		}

	
	

	@Override
	public String toString() {
		String out = "Instance ID: " + this.getInstTypeID() + " vCPU: " + this.getvCPU() + " Memory: " + this.getMemory() + " OS: " + this.getOS() + " Location: " + this.getLocation() + " Price(/h): " + this.getPrice() + "\n";
		return out;
	}
	
	@Override
	public int hashCode() {
	    return InstTypeID.hashCode();
	}
	

	
	

}
