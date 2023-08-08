package Broker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Customer.Customer;
import Provider.Providers;
import Tools.DBConnect;
import kodkod.ast.Relation;
import kodkod.instance.Bounds;
import kodkod.instance.Universe;
import Components.Component;
import Components.Relations;

public class Test {
	
public static void main(String[] args) {
		Test t = new Test();
		t.FillData();
	}
    @SuppressWarnings("serial")
	public void FillData() {
		
		Set<Component> MyVms = new HashSet<Component>(){{
			add(new Component("CustomerCmp3-1",8,16,"linux","EU"));
			add(new Component("CustomerCmp3-2",8,16,"linux","EU"));
			add(new Component("CustomerCmp3-3",4,8,"linux","EU"));
			add(new Component("CustomerCmp3-4",4,8,"linux","EU"));
			add(new Component("CustomerCmp3-5",8,16,"linux","EU"));
			add(new Component("CustomerCmp3-6",4,8,"windows","US"));
			add(new Component("CustomerCmp3-7",4,8,"windows","US"));
		}};
		Customer C = new Customer("3");
		Set<Customer> Customers = new HashSet<Customer>();
		Customers.add(C);
		C.setComps(MyVms);
		
		ArrayList<ArrayList<String>> Conc = new ArrayList<ArrayList<String>>();
		ArrayList<String> ConcSet1 = new ArrayList<String>(){{
			add("CustomerCmp3-1");add("CustomerCmp3-3");}};
		ArrayList<String> ConcSet2 = new ArrayList<String>(){{
				add("CustomerCmp3-3");add("CustomerCmp3-6");}};
		Conc.add(ConcSet1);
		Conc.add(ConcSet2);
		C.setConcurrence(Conc);
		
		ArrayList<ArrayList<String>> flow = new ArrayList<ArrayList<String>>();
		ArrayList<String> flowSet = new ArrayList<String>(){{
			add("CustomerCmp3-3");add("CustomerCmp3-5");}};
		ArrayList<String> collSet1 = new ArrayList<String>(){{
			add("CustomerCmp3-1");add("CustomerCmp3-2");}};
		ArrayList<String> collSet2 = new ArrayList<String>(){{
			add("CustomerCmp3-2");add("CustomerCmp3-6");}};
			ArrayList<String> collSet3 = new ArrayList<String>(){{
				add("CustomerCmp3-7");add("CustomerCmp3-6");}};
		flow.add(flowSet);
		flow.add(collSet1);
		flow.add(collSet2);
		flow.add(collSet3);
		C.setFlow(flow);
		
		Set<String> alone = new HashSet<String>();
		alone.add("CustomerCmp3-4");
		C.setAlone(alone);
		
		
		
		DBConnect DBC = new DBConnect();
		Providers F2 = new Providers();
		F2.setProviders(DBC.GetProviders());

		Broker b = new Broker();
		b.setCustomers(Customers);

		b.setFederation(F2);
		Relations R = new Relations(Relation.unary("Customer"),Relation.binary("Owns"));
		//Component  v =  new Component();
		System.out.println("===========> Customer Verification ");
		List<String> uCl = C.Customer_universe();

				
		Bounds bound = new Bounds(new Universe(uCl));
	
		
		if(b.VerifyCustomer(R,bound,C)!=null) {
		
		System.out.println("Customer's model is consistant");
		
		BufferedWriter br = null;
		try {
			 String filename = "LM/Customer"+ C.getCID() +  ".data";
			 File file = new File(filename); 	         
			 br = new BufferedWriter(new FileWriter(file)); 
			 br.write("data;");
			 br.append(C.ExportCustomerData());
			 br.append(F2.ExportProvidersData());
			 br.append(b.ExportCompData());
			 br.append("end;");
		 } 
		 catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}finally {

            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }}
		
		
		
		
	}
}
