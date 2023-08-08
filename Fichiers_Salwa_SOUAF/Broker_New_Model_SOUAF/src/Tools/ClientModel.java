package Tools;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Broker.Broker;
import Components.Component;
import Components.Relations;
import Customer.Customer;
import Provider.Providers;
import kodkod.ast.Relation;
import kodkod.engine.Solution;
import kodkod.instance.Bounds;
import kodkod.instance.Tuple;
import kodkod.instance.Universe;

@SuppressWarnings("serial")
public class ClientModel extends JPanel implements ActionListener {

	DBConnect dbConnection = new DBConnect();
	JPanel prefirstPanel1 = new JPanel();
    JPanel firstPanel1 = new JPanel();
    JPanel firstPanelBis1 = new JPanel();
    JPanel secondPanel1 = new JPanel();
    JPanel thirdPanel1 = new JPanel();
    JPanel forthPanel1 = new JPanel();
    JPanel fifthPanel1 = new JPanel();

    JScrollPane prefirstPanel = new JScrollPane(prefirstPanel1);
    JScrollPane firstPanel = new JScrollPane(firstPanel1);
    JScrollPane firstPanelBis = new JScrollPane(firstPanelBis1);
    JScrollPane secondPanel = new JScrollPane(secondPanel1);
    JScrollPane thirdPanel = new JScrollPane(thirdPanel1);
    JScrollPane forthPanel = new JScrollPane(forthPanel1);
    JPanel FP = new JPanel();
    JScrollPane fifthPanel = new JScrollPane(fifthPanel1);
    
    
    
    JTextField jEdit1,jEdit1bis, jEditIso, jEditColl1,jEditColl2,jEditflow1,jEditflow2,jEditConc1,jEditConc2,
    	jEditIsodel, jEditColldel1,jEditColldel2,jEditflowdel1,jEditflowdel2,jEditConcdel1,jEditConcdel2,jDelete;
    JTextField FedEdit;
    JTextField[][] entries ;
    JLabel choices = new JLabel();
    JLabel status = new JLabel();
    JTextArea proof = new JTextArea();
    JLabel ID = new JLabel("CompID");
    JLabel cpu = new JLabel("vCPU");
    JLabel memory = new JLabel("Memory");
    JLabel os = new JLabel("OS");
    JLabel Loc = new JLabel("Location");
    JTextArea Jplace = new JTextArea();
    JButton preB1,B1,B1bis,B2,Bcoll,Bconc,BIso,Bflow,Bcolldel,Bconcdel,BIsodel,Bflowdel,BModif,BDel,Brecheck,BPlace,Bback;
    JButton Bcolladd2,Bconcadd2,Bflowadd2,BIsoadd2,BAdd;
    public ClientModel() {
        super(new BorderLayout());

        B1 = new JButton("Next");
        preB1 = new JButton("Next");
        B1bis = new JButton("Next");
        preB1.addActionListener(this);
        B1.addActionListener(this);
        B1bis.addActionListener(this);
        jEdit1 = new JTextField("Enter the number of Components");
        JLabel jLabel1 =new JLabel("Nbr of Componentss : "); 
        jLabel1.setLabelFor(jEdit1);
        jEdit1bis = new JTextField("Enter Customer ID");
        JLabel jLabel1bis =new JLabel("Customer ID : "); 
        jLabel1bis.setLabelFor(jEdit1bis);

        jEditIso = new JTextField("Comp1 Comp3 ...");
        JLabel jLabelIso =new JLabel("Components to be hosted Alone : ");
        BIso = new JButton("Set Alone");
        BIso.addActionListener(this);
        jLabelIso.setLabelFor(jEditIso);
        
        
        jEditflow1 = new JTextField("Compi");
        jEditflow2 = new JTextField("Compj");
        JLabel jLabelflow =new JLabel("Flow between Comps : "); 
        jLabelflow.setLabelFor(jEditflow1);
        jLabelflow.setLabelFor(jEditflow2);
        Bflow = new JButton("Add flow");
        Bflow.addActionListener(this);
        
        jEditConc1 = new JTextField("Compi");
        jEditConc2 = new JTextField("Compj");
        JLabel jLabelConc =new JLabel("Concurrent Comps : "); 
        jLabelConc.setLabelFor(jEditConc1);
        jLabelConc.setLabelFor(jEditConc2);
        Bconc = new JButton("Add Concurrence");
        Bconc.addActionListener(this);
        
        jEditIsodel = new JTextField("Compi");
        JLabel jLabelIsodel =new JLabel("Alone : ");
        BIsodel = new JButton("Remove Alone");
        BIsodel.addActionListener(this);
        BIsoadd2 = new JButton("Add Alone");
        BIsoadd2.addActionListener(this);
        jLabelIsodel.setLabelFor(jEditIsodel);
      
        
        jEditflowdel1 = new JTextField("Compi");
        jEditflowdel2 = new JTextField("Compj");
        JLabel jLabelflowdel =new JLabel("flow : "); 
        jLabelflowdel.setLabelFor(jEditflowdel1);
        jLabelflowdel.setLabelFor(jEditflowdel2);
        Bflowdel = new JButton("Remove flow");
        Bflowdel.addActionListener(this);
        Bflowadd2 = new JButton("Add flow");
        Bflowadd2.addActionListener(this);
        
        jEditConcdel1 = new JTextField("Compi");
        jEditConcdel2 = new JTextField("Compj");
        JLabel jLabelConcdel =new JLabel("Concurrence : "); 
        jLabelConcdel.setLabelFor(jEditConcdel1);
        jLabelConcdel.setLabelFor(jEditConcdel2);
        Bconcdel = new JButton("Remove Concurrence");
        Bconcdel.addActionListener(this);
        Bconcadd2 = new JButton("Add Concurrence");
        Bconcadd2.addActionListener(this);
        
        B2 = new JButton("Check");
        B2.addActionListener(this);

        
        
        prefirstPanel1.add(jLabel1bis);
        prefirstPanel1.add(jEdit1bis);
        prefirstPanel1.add(preB1);
        firstPanel1.add(jLabel1);
        firstPanel1.add(jEdit1);
        firstPanel1.add(B1);
        
        GridLayout grid1 = new GridLayout(3, 3, 5, 5);
        secondPanel1.setLayout(grid1);
        secondPanel1.applyComponentOrientation( ComponentOrientation.LEFT_TO_RIGHT);

        secondPanel1.add(jLabelflow);
        secondPanel1.add(jEditflow1);
        secondPanel1.add(jEditflow2);
        secondPanel1.add(Bflow);
        secondPanel1.add(jLabelConc);
        secondPanel1.add(jEditConc1);
        secondPanel1.add(jEditConc2);
        secondPanel1.add(Bconc);
        secondPanel1.add(jLabelIso);
        secondPanel1.add(jEditIso);
        secondPanel1.add(BIso);
        B2.setForeground(Color.red);
        secondPanel1.add(B2);
        
        GridLayout grid2 = new GridLayout(4, 1, 5, 5);
        thirdPanel1.setLayout(grid2);
        thirdPanel1.add(choices);
        thirdPanel1.add(status);
        thirdPanel1.add(proof);
        
        /*GridBagLayout grid4 = new GridBagLayout();
        forthPanel1.setLayout(grid4);
        
        GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.ipady = gc.anchor = GridBagConstraints.CENTER;
		gc.weightx = 5;
		gc.weighty = 6;
        gc.gridx = 0;
		gc.gridy = 1;
        forthPanel1.add(jLabelflowdel,gc);
        gc.gridx = 1;
		gc.gridy = 1;
        forthPanel1.add(jEditflowdel1,gc);
        gc.gridx = 2;
		gc.gridy = 1;
        forthPanel1.add(jEditflowdel2,gc);
        gc.gridx = 3;
		gc.gridy = 1;
        forthPanel1.add(Bflowdel,gc);
        gc.gridx = 4;
		gc.gridy = 1;
        forthPanel1.add(Bflowadd2,gc);
        gc.gridx = 0;
		gc.gridy = 2;
        forthPanel1.add(jLabelConcdel,gc);
        gc.gridx = 1;
		gc.gridy = 2;
        forthPanel1.add(jEditConcdel1,gc);
        gc.gridx = 2;
		gc.gridy = 2;
        forthPanel1.add(jEditConcdel2,gc);
        gc.gridx = 3;
		gc.gridy = 2;
        forthPanel1.add(Bconcdel,gc);
        gc.gridx = 4;
		gc.gridy = 2;
        forthPanel1.add(Bconcadd2,gc);
        gc.gridx = 0;
		gc.gridy = 3;
        forthPanel1.add(jLabelIsodel,gc);
        gc.gridx = 1;
		gc.gridy = 3;
        forthPanel1.add(jEditIsodel,gc);
        gc.gridx = 3;
		gc.gridy = 3;
        forthPanel1.add(BIsodel,gc);
        gc.gridx = 4;
		gc.gridy = 3;
        forthPanel1.add(BIsoadd2,gc);
        jDelete = new JTextField("ID of the Comp to delete");
        BDel = new JButton("Delete Component");
        BDel.addActionListener(this);
        BAdd = new JButton("Add Component");
        BAdd.addActionListener(this);
        Brecheck = new JButton("Recheck");
        Brecheck.setForeground(Color.red);
        Brecheck.addActionListener(this);
        gc.gridx = 1;
		gc.gridy = 4;
        forthPanel1.add(jDelete,gc);
        gc.gridx = 3;
		gc.gridy = 4;
        forthPanel1.add(BDel,gc);
        gc.gridx = 4;
		gc.gridy = 4;
        forthPanel1.add(BAdd,gc);
        gc.gridx = 3;
		gc.gridy = 5;
        forthPanel1.add(Brecheck,gc);
        Bback = new JButton("Back");
        Bback.addActionListener(this);*/
        
        
        add(prefirstPanel);
    }

    Set<Set<String>> CollVms = new HashSet<Set<String>>();
    Set<LinkedHashSet<String>> flowVms = new HashSet<LinkedHashSet<String>>();
    Set<Set<String>> ConcVms = new HashSet<Set<String>>();
    Set<String> IsoVms = new HashSet<String>();
    Set<Component> VmIDs = new HashSet<Component>();
    int NumbrVm = 0;
    String CustID;
    Customer C = null;
    Broker model = null;
    Set<Customer> clients = new HashSet<Customer>();
    Set<PreparedStatement> PSs = new HashSet<PreparedStatement>();
    Set<PreparedStatement> PSDels = new HashSet<PreparedStatement>();
    Set<PreparedStatement> PSAdds = new HashSet<PreparedStatement>();
	JScrollPane fP;
    /** Listens to the buttons and perfomr the swap. */
    public void actionPerformed(ActionEvent e) {

    		if (e.getSource().equals(preB1)){
    			CustID = this.jEdit1bis.getText();
    			if(! dbConnection.CustExist(CustID)){
    				C = new Customer(CustID);
    				remove(prefirstPanel);
                    add(firstPanel);
                    repaint();
                    revalidate();
    			}else{
    				System.out.println("Customer exists");
    				C = dbConnection.GetCustomerByID(CustID);
    				
    				

            		Set<Customer> Customers = new HashSet<Customer>();
            		Customers.add(C);
            		Providers F2 = new Providers();
            		F2.setProviders(dbConnection.GetProviders());

            		Broker b = new Broker();
            		b.setCustomers(Customers);

            		b.setFederation(F2);
            		Relations R = new Relations(Relation.unary("Customer"),Relation.binary("Owns"));
            		Component  v =  new Component();
            		System.out.println("===========> Customer Verification ");
            		List<String> uCl = C.Customer_universe();

            		
            		Bounds bound = new Bounds(new Universe(uCl));
    				
    				System.out.println("Customer's model is consistant");
            		//long stopTime1 = System.currentTimeMillis();
            		BufferedWriter br = null;
            		RunGLPSol run = new RunGLPSol();
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
            		 catch (IOException ex1) {
            			ex1.printStackTrace();
            			System.exit(-1);
            		}finally {

                        try {
                            if (br != null)
                                br.close();
                        } catch (IOException ex) {

                            ex.printStackTrace();

                        }

                    }
            		run.RunCommand(C.getCID());
            		this.choices.setText("Alone : "+C.getAlone().toString()+"\n"+
                    		"Flow : "+C.getFlow().toString()+"\n"+"Concurrent : "+C.getConcurrence().toString());
            		if(run.found) {
            			this.proof.setText(run.ReadResult(C.getCID()));

            			//dbConnection.InsertCust(C);
            			for(Component cmp : C.getComps()) {
            				for(String[] rs : run.results) {
            					if(rs[0].contains(cmp.getCmpID())) {
            						dbConnection.UpdateComponent(cmp.getCmpID(), rs[2]);
            					}
            				}
            			}
            			/*for(PreparedStatement ps : PSs){
            				try {					
            					ps.executeUpdate();
        		
        					} catch (SQLException e1) {
        						// TODO Auto-generated catch block
        						e1.printStackTrace();
        					}finally {
        						try {
        							ps.getConnection().close();
        						} catch (SQLException e1) {
        							// TODO Auto-generated catch block
        							e1.printStackTrace();
        						}
        					}
            			}*/
            		}
    				
    				remove(prefirstPanel);
        			add(thirdPanel);
                    repaint();
                    revalidate();
    			}
    			
    		}
            if (e.getSource().equals(B1)) {
                System.out.println(this.jEdit1.getText());
                String in = this.jEdit1.getText();
                NumbrVm = Integer.parseInt(in);
                GridBagLayout grid = new GridBagLayout();
                firstPanelBis1.setLayout(grid);                   
                GridBagConstraints gc = new GridBagConstraints();
        		gc.fill = GridBagConstraints.BOTH;
        		gc.insets = new Insets(6, 6, 6, 6);
        		gc.ipady = gc.anchor = GridBagConstraints.CENTER;
        		gc.weightx = 5;
        		gc.weighty = NumbrVm+2;
        		gc.gridx = 0;
        		gc.gridy = 0;
                firstPanelBis1.add(ID,gc);
                gc.gridx = 1;
        		gc.gridy = 0;
                firstPanelBis1.add(cpu,gc);
                gc.gridx = 2;
        		gc.gridy = 0;
                firstPanelBis1.add(memory,gc);
                gc.gridx = 3;
        		gc.gridy = 0;
        		firstPanelBis1.add(os,gc);
                gc.gridx = 4;
        		gc.gridy = 0;
                firstPanelBis1.add(Loc,gc);
                entries = new JTextField[NumbrVm][5];
                for(int i=0; i<NumbrVm; i++){
                	for(int j=0; j<5 ; j++){
                		entries[i][j] = new JTextField();
                		gc.gridx = j;
                		gc.gridy = i+1;
                        firstPanelBis1.add(entries[i][j],gc);
                	}  	 
                }
                gc.gridx = 4;
        		gc.gridy = NumbrVm+1;
                firstPanelBis1.add(B1bis,gc);
              
                remove(firstPanel);
                add(firstPanelBis);
                repaint();
                revalidate();

            }
            if(e.getSource().equals(B1bis)){
            	remove(firstPanelBis);
                add(secondPanel);
            	for(int i=0; i<NumbrVm; i++){         		
                	VmIDs.add(new Component("CustomerCmp"+C.getCID()+"-"+entries[i][0].getText(),Integer.parseInt(entries[i][1].getText()),Integer.parseInt(entries[i][2].getText()),entries[i][3].getText(),entries[i][4].getText())); 
                }
            	C.setComps(VmIDs);
            }
         
            
            if(e.getSource().equals(Bflow)){
           		String vm1 = "CustomerCmp"+C.getCID()+"-"+jEditflow1.getText();
           		String vm2 = "CustomerCmp"+C.getCID()+"-"+jEditflow2.getText();
           		LinkedHashSet<String> flow = new LinkedHashSet<String>(){{
           			add(vm1);
           			add(vm2);
           		}};
           		C.AddFlow(vm1, vm2);
           		flowVms.add(flow);
           		PSs.add(dbConnection.InsertFlow(vm1, vm2));
           		System.out.println(flow.toString());
           	}
            
            if(e.getSource().equals(Bflowdel)){
           		String vm1 = "CustomerCmp"+C.getCID()+"-"+jEditflowdel1.getText();
           		String vm2 = "CustomerCmp"+C.getCID()+"-"+jEditflowdel2.getText();
           		C.DelFlow(vm1, vm2);
           		//PSDels.add( dbConnection.DeleteFlow(vm1, vm2));
           	}
            
            if(e.getSource().equals(Bflowadd2)){
           		String vm1 = "CustomerCmp"+C.getCID()+"-"+jEditflowdel1.getText();
           		String vm2 = "CustomerCmp"+C.getCID()+"-"+jEditflowdel2.getText();
           		C.AddFlow(vm1, vm2);
           		//PSAdds.add( dbConnection.InsertFlow(vm1, vm2));
           	}
            	
            	
           	if(e.getSource().equals(Bconc)){
           		String vm1 = "CustomerCmp"+C.getCID()+"-"+jEditConc1.getText(); 
           		String vm2 = "CustomerCmp"+C.getCID()+"-"+jEditConc2.getText();
            	Set<String> conc = new HashSet<String>(){{
            		add(vm1);
            		add(vm2);
            	}};
            	C.AddConc(vm1, vm2);
            	ConcVms.add(conc);
            	PSs.add( dbConnection.InsertConc(vm1, vm2));
            	PSs.add( dbConnection.InsertConc(vm2, vm1));
            	System.out.println(conc.toString());
            }
           	
           	if(e.getSource().equals(Bconcdel)){
           		String vm1 = "CustomerCmp"+C.getCID()+"-"+jEditConcdel1.getText();
           		String vm2 = "CustomerCmp"+C.getCID()+"-"+jEditConcdel2.getText();
           		C.DelConc(vm1, vm2);
           		//PSDels.add( dbConnection.DeleteConc(vm1, vm2));
           		//PSDels.add( dbConnection.DeleteConc(vm2, vm1));
           	}
           	
           	if(e.getSource().equals(Bconcadd2)){
           		String vm1 = "CustomerCmp"+C.getCID()+"-"+jEditConcdel1.getText();
           		String vm2 = "CustomerCmp"+C.getCID()+"-"+jEditConcdel2.getText();
           		C.AddConc(vm1, vm2);
           		//PSAdds.add( dbConnection.InsertConc(vm1, vm2));
            	//PSAdds.add( dbConnection.InsertConc(vm2, vm1));
           	}
            	
            	
            if(e.getSource().equals(this.BIso)){
            	for(int i=0 ; i< this.jEditIso.getText().split(" ").length; i++){
            		IsoVms.add(this.jEditIso.getText().split(" ")[i]);
            	}
            	for(String s : IsoVms){
            		String alone = "CustomerCmp"+C.getCID()+"-"+s;
            		C.AddAlone(alone);
            		PSs.add( dbConnection.InsertAlone(alone));
            		System.out.println("Iso "+alone+" ");
            	}
            }
            
            if(e.getSource().equals(this.BIsodel)){
            	String[] isos = this.jEditIsodel.getText().split(" ");
            	//C.DelIsolation(isos);
            	for(int i=0;i<isos.length;i++){
            		//PSDels.add( dbConnection.DeleteIsol(isos[i]));
            	}
            }
            
            if(e.getSource().equals(this.BIsoadd2)){
            	String[] isos = this.jEditIsodel.getText().split(" ");
            	//C.AddIsolation(isos);
            	for(int i=0;i<isos.length;i++){
            		//PSAdds.add( dbConnection.InsertIsol(isos[i]));
            	}
            }
            
            
            
            if(e.getSource().equals(B2)){
            	

        		Set<Customer> Customers = new HashSet<Customer>();
        		Customers.add(C);
        		Providers F2 = new Providers();
        		F2.setProviders(dbConnection.GetProviders());

        		Broker b = new Broker();
        		b.setCustomers(Customers);

        		b.setFederation(F2);
        		Relations R = new Relations(Relation.unary("Customer"),Relation.binary("Owns"));
        		Component  v =  new Component();
        		System.out.println("===========> Customer Verification ");
        		List<String> uCl = C.Customer_universe();
        				
        				
        		Bounds bound = new Bounds(new Universe(uCl));
        		
        		
        		if(b.VerifyCustomer(R,bound,C)!=null) {
        			
        		
        		System.out.println("Customer's model is consistant");
        		
        		dbConnection.InsertCust(C);
    			
    			for(PreparedStatement ps : PSs){
    				try {					
    					ps.executeUpdate();
		
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}finally {
						try {
							ps.getConnection().close();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
    			}
        		
        		//long stopTime1 = System.currentTimeMillis();
        		BufferedWriter br = null;
        		RunGLPSol run = new RunGLPSol();
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
        		 catch (IOException ex1) {
        			ex1.printStackTrace();
        			System.exit(-1);
        		}finally {

                    try {
                        if (br != null)
                            br.close();
                    } catch (IOException ex) {

                        ex.printStackTrace();

                    }

                }
        		run.RunCommand(C.getCID());
        		this.choices.setText("Alone : "+IsoVms.toString()+"\n"+
                		"Flow : "+flowVms.toString()+"\n"+"Concurrent : "+ConcVms.toString());
        		if(run.found) {
        			this.proof.setText(run.ReadResult(C.getCID()));
        			
        			for(Component cmp : C.getComps()) {
        				for(String[] rs : run.results) {
        					if(rs[0].contains(cmp.getCmpID())) {
        						dbConnection.UpdateComponent(cmp.getCmpID(), rs[2]);
        					}
        				}
        			}

        			
        		}
                		
        		}
        		
        		remove(secondPanel);
                add(thirdPanel);
                repaint();
                revalidate();
            }
            
          

        repaint();
        revalidate();
    }
    
   
    
    
    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("Customer Model");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create and set up the content pane.
        JComponent newContentPane = new ClientModel();
        newContentPane.setOpaque(true); // content panes must be opaque
        frame.setContentPane(newContentPane);

        // Display the window.
        frame.setPreferredSize(new Dimension(800,800));
        frame.setMinimumSize(new Dimension(600,600));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
