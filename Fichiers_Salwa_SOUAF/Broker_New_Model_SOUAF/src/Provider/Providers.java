package Provider;

import java.util.Set;

import Components.Relations;

public class Providers {
	
	private Set<Provider> Providers;
	
	Relations R = new Relations();
	
	public Providers() {
		super();
		
	}
	
	
	public Set<Provider> getProviders() {
		return Providers;
	}
	public void setProviders(Set<Provider> providers) {
		Providers = providers;
	}
	

	public String ExportProvidersData() {
		String data = new String();
		data = data + "param nbinstances := 10 ; \n"; 
		data = data + "param nbp := " + this.getProviders().size() + "; \n";
		data = data + "set P := ";
		for(Provider p : this.getProviders()) {
			data = data + p.getPID() + " \n";
		}
		data = data + "; \n";
		
		data = data + "param nbti := ";
		for(Provider p : this.getProviders()) {
			data = data + p.getPID() + " " + (p.getITs().size()-1) + " \n";
		}
		data = data + "; \n";
		
		
		for(Provider p : this.getProviders()) {
			for(int i = 1 ; i < p.getITs().size() ; i ++) {
				data = data + "set idti [" + p.getPID() + "," + i + "] := "
						+ p.getITs().get(i).getInstTypeID() + "; \n";
			}

		}
		
		
		data = data + "param InstancesPrices := ";
		for(Provider p : this.getProviders()) {
			for(int i = 1 ; i < p.getITs().size() ; i ++) {
				data = data + p.getPID() + " " + i + " " + p.getITs().get(i).getPrice() + " \n";
			}
			
		}
		data = data + "; \n";
		
		data = data + " param VPC := Amz 0.005\n" + 
				"             Google 0.004\n" + 
				"             Alibaba 0.005\n" + 
				"             Azure 0.004;\n" + 
				/*"             IBM 0.005\n" + 
				"             ProvX 0.004\n" +
				"             ProvW 0.004\n" +
				"             ProvZ 0.004\n" +
				"             ProvU 0.004\n" +
				"             ProvY 0.005;\n" + */
				"\n" + 
				"\n" + 
				"/* pricing here are per amount of data transfered transfered */\n" + 
				"param VPT := Amz 0.05\n" + 
				"             Google 0.05\n" + 
				"             Alibaba 0.05\n" + 
				"             Azure 0.05;\n" + 
				/*"             IBM 0.05\n" + 
				"             ProvX 0.05\n" +
				"             ProvW 0.05\n" +
				"             ProvZ 0.05\n" +
				"             ProvU 0.05\n" +
				"             ProvY 0.05;\n" + */
				"\n" + 
				"param CEL := 6;\n" + 
				"param CIL := 3; \n";
		
		
		
		return data;
	}

}
