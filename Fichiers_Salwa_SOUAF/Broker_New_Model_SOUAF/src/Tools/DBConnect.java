package Tools;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Components.Component;
import Customer.Customer;
import Provider.InstanceType;
import Provider.Provider;  

public class DBConnect { 
	
public static void main(String args[]){  
	Component C = new Component("Cmp1", 4, 8, "Linux", "EU");
	Compatible comp = new Compatible(C);
	for(InstanceType it : comp.getITs())
		System.out.println(it.toString());
	
	
}  

public Connection OpenConn(){
	String url = "jdbc:mysql://localhost:8889/Broker1";
	//String url = "jdbc:mysql://localhost:8889/Broker_Less_Data";
	String login = "root";
	String pswd = "root";
	Connection cn = null;
	try{
		Class.forName("com.mysql.jdbc.Driver");
		cn = DriverManager.getConnection(url,login,pswd);
	}catch (SQLException e) {
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return cn;
}

public Set<Provider> GetProviders(){
	Set<Provider> providers = new HashSet<Provider>();
	
	try{  
		PreparedStatement pstmt = OpenConn().prepareStatement("select * from Provider");
		ResultSet rs = pstmt.executeQuery();

		while(rs.next())  {
			String PID = rs.getString(1);
			Provider p = new Provider(PID);
			ArrayList<InstanceType> ITs = new ArrayList<InstanceType>();
			PreparedStatement pstmt2 = OpenConn().prepareStatement("select * from InstanceType where PID like ?");
			pstmt2.setString(1, PID);
			ResultSet rs2 = pstmt2.executeQuery();
			while(rs2.next())  {
				InstanceType it = new InstanceType(rs2.getString(1), rs2.getInt(3), rs2.getInt(4), rs2.getString(5), rs2.getString(6),rs2.getDouble(8));
				ITs.add(it);  
				}		
			p.setITs(ITs);
			providers.add(p);
			}
		OpenConn().close();  
		}catch(Exception e){ System.out.println(e);}  
	return providers;
}

public boolean CustExist(String CID){		
	Statement st = null;
	ResultSet rs = null;
	int count = 0;
	try{
		st = OpenConn().createStatement();
		String Query = "SELECT COUNT(*) AS rowcount FROM Customer WHERE CustID='"+CID+"'";
		rs = st.executeQuery(Query);
		rs.next();
		count = rs.getInt("rowcount");
		rs.close();
	}catch (SQLException e) {
		e.printStackTrace();
	} finally {
		try {
			OpenConn().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	if(count == 1){
		return true;
	}else{
		return false;
	}
	
}

public void InsertComponent(String CustID, Component comp){		
	try{		
		PreparedStatement stmt = OpenConn().prepareStatement("INSERT INTO Component(CmpID, CustID, vCPU, Memory, OS, Location) VALUES (?, ?, ?, ?, ?,?)");
		stmt.setString(1,comp.getCmpID());
		stmt.setString(2,CustID);
		stmt.setInt(3,comp.getVcpu());
		stmt.setInt(4,comp.getMemory());
		stmt.setString(5,comp.getOs());
		stmt.setString(6,comp.getLocation());
		stmt.executeUpdate();
	}catch (SQLException e) {
		e.printStackTrace();
	} finally {
		try {
			OpenConn().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

public void UpdateComponent(String CmpId, String ITID){
	try{		
		PreparedStatement stmt = OpenConn().prepareStatement("UPDATE Component SET ITID = ? WHERE CmpID = ?");
		stmt.setString(1, ITID);
		stmt.setString(2, CmpId);
		stmt.executeUpdate();
	}catch (SQLException e) {
		e.printStackTrace();
	} finally {
		try {
			OpenConn().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

public void UpdateInstance(String InstID){
	try{		
		PreparedStatement stmt = OpenConn().prepareStatement("UPDATE Component SET IsUsed = ? WHERE InstID = ?");
		stmt.setBoolean(1, true);
		stmt.setString(2, InstID);
		stmt.executeUpdate();
	}catch (SQLException e) {
		e.printStackTrace();
	} finally {
		try {
			OpenConn().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

public void InsertCust(Customer C){		
	try{
		
		PreparedStatement stmt = OpenConn().prepareStatement("INSERT INTO Customer(CustID) VALUES (?)");
		stmt.setString(1,C.getCID());
		stmt.executeUpdate();
		for(Component v : C.getComps()){
			InsertComponent(C.getCID(), v);
		}
		
		
	}catch (SQLException e) {
		e.printStackTrace();
	} finally {
		try {
			OpenConn().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

public PreparedStatement InsertConc(String ci, String cj){
	PreparedStatement stmt = null;
	try{
		stmt = OpenConn().prepareStatement("INSERT INTO Concurrence (Cmpi,Cmpj) VALUES (?,?)");
		stmt.setString(1,ci);
		stmt.setString(2,cj);
	}catch (SQLException e) {
		e.printStackTrace();
	}
	return stmt;
}

public PreparedStatement DeleteConc(String ci, String cj){
	PreparedStatement stmt = null;
	try{
		stmt = OpenConn().prepareStatement("DELETE FROM Concurrence WHERE Cmpi = ? AND Cmpj = ?");
		stmt.setString(1,ci);
		stmt.setString(2,cj);
	}catch (SQLException e) {
		e.printStackTrace();
	}
	return stmt;
}

public PreparedStatement InsertFlow(String ci, String cj){
	PreparedStatement stmt = null;
	try{
		stmt = OpenConn().prepareStatement("INSERT INTO Flow (Cmpi,Cmpj) VALUES (?,?)");
		stmt.setString(1,ci);
		stmt.setString(2,cj);
	}catch (SQLException e) {
		e.printStackTrace();
	}
	return stmt;
}

public PreparedStatement DeleteFlow(String ci, String cj){
	PreparedStatement stmt = null;
	try{
		stmt = OpenConn().prepareStatement("DELETE FROM UniFlow WHERE Cmpi = ? AND Cmpj = ?");
		stmt.setString(1,ci);
		stmt.setString(2,cj);
	}catch (SQLException e) {
		e.printStackTrace();
	}
	return stmt;
}

public PreparedStatement InsertAlone(String ci){
	PreparedStatement stmt = null;
	try{
		stmt = OpenConn().prepareStatement("INSERT INTO Alone (CmpID) VALUES (?)");
		stmt.setString(1,ci);
	}catch (SQLException e) {
		e.printStackTrace();
	}
	return stmt;
}

public PreparedStatement DeleteAlone(String ci){
	PreparedStatement stmt = null;
	try{
		stmt = OpenConn().prepareStatement("DELETE FROM Alone WHERE CmpID = ?");
		stmt.setString(1,ci);
	}catch (SQLException e) {
		e.printStackTrace();
	}
	return stmt;
}

public Set<Component> GetCustComps(String CID){
	Set<Component> Comps = new HashSet<Component>();
	Statement st = null;
	ResultSet rs = null;
	try{
		st = OpenConn().createStatement();
		String Query = "SELECT * FROM Component WHERE CustID='"+CID+"'";
		rs = st.executeQuery(Query);
		while(rs.next()){
			String cid = rs.getString("CmpID");
			int cpu = rs.getInt("vCPU");
			int memory = rs.getInt("Memory");
			String os = rs.getString("OS");
			String loc = rs.getString("Location");
			Comps.add(new Component(cid, cpu, memory, os, loc));
		}			
	}catch (SQLException e) {
		e.printStackTrace();
	} finally {
		try {
			st.close();
			OpenConn().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return Comps;
}

public Set<String> GetCompIDsByCust(String CID){
	Set<String> Cmps = new HashSet<String>();
	Statement st = null;
	ResultSet rs = null;
	try{
		st = OpenConn().createStatement();
		String Query = "SELECT * FROM Component WHERE CustID='"+CID+"'";
		rs = st.executeQuery(Query);
		while(rs.next()){
			String vid = rs.getString("CmpID");
			Cmps.add(vid);
		}			
	}catch (SQLException e) {
		e.printStackTrace();
	} finally {
		try {
			st.close();
			OpenConn().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return Cmps;
}


public Set<String> GetCustAlone(String CID){
	Set<String> Cmps = new HashSet<String>();
	Statement st = null;
	ResultSet rs = null;
	
		try{
			Set<String> CmpIDS = GetCompIDsByCust(CID);
			st = OpenConn().createStatement();
			for(String ID : CmpIDS){
				String Query = "SELECT * FROM Alone WHERE CmpID='"+ID+"'";
				rs = st.executeQuery(Query);
				while(rs.next()){
					String vid = rs.getString("CmpID");
					Cmps.add(vid);
				}
			}
	}catch (SQLException e) {
		e.printStackTrace();
	} finally {
		try {
			st.close();
			OpenConn().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return Cmps;
}

public ArrayList<ArrayList<String>> GetConcByCust(String CID){
	ArrayList<ArrayList<String>> Cmps = new ArrayList<ArrayList<String>>();
	Statement st = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	try{
		Set<String> CmpIDS = GetCompIDsByCust(CID);
		st = OpenConn().createStatement();
		for(String ID : CmpIDS){
			
			String Query = "SELECT * FROM Concurrence WHERE Cmpi='"+ID+"'";
			String Query2 = "SELECT * FROM Concurrence WHERE Cmpj='"+ID+"'";
			rs = st.executeQuery(Query);
			while(rs.next()){
				ArrayList<String> col = new ArrayList<String>();
				String cmpi = rs.getString("Cmpi");
				String cmpj = rs.getString("Cmpj");
				col.add(cmpi);
				col.add(cmpj);
				if(!Cmps.contains(col)){
					Cmps.add(col);
				}
				
			}
			rs2 = st.executeQuery(Query2);
			while(rs2.next()){
				ArrayList<String> col = new ArrayList<String>();
				String cmpi = rs2.getString("Cmpi");
				String cmpj = rs2.getString("Cmpj");
				col.add(cmpi);
				col.add(cmpj);
				if(!Cmps.contains(col)){
					Cmps.add(col);
				}
				
			}
		}
	}catch (SQLException e) {
		e.printStackTrace();
	} finally {
		try {
			st.close();
			OpenConn().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return Cmps;
}

public List<ArrayList<String>> GetFlowByCust(String CID){
	List<ArrayList<String>> Cmps = new ArrayList<ArrayList<String>>();
	Statement st = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	try{
		Set<String> CmpIDS = GetCompIDsByCust(CID);
		st = OpenConn().createStatement();
		for(String ID : CmpIDS){
			
			String Query = "SELECT * FROM Flow WHERE Cmpi='"+ID+"'";
			String Query2 = "SELECT * FROM Flow WHERE Cmpj='"+ID+"'";
			rs = st.executeQuery(Query);
			while(rs.next()){
				ArrayList<String> col = new ArrayList<String>();
				String cmpi = rs.getString("Cmpi");
				String cmpj = rs.getString("Cmpj");
				col.add(cmpi);
				col.add(cmpj);
				if(!Cmps.contains(col)){
					Cmps.add(col);
				}
				
			}
			rs2 = st.executeQuery(Query2);
			while(rs2.next()){
				ArrayList<String> col = new ArrayList<String>();
				String cmpi = rs2.getString("Cmpi");
				String cmpj = rs2.getString("Cmpj");
				col.add(cmpi);
				col.add(cmpj);
				if(!Cmps.contains(col)){
					Cmps.add(col);
				}
				
			}
		}
	}catch (SQLException e) {
		e.printStackTrace();
	} finally {
		try {
			st.close();
			OpenConn().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return Cmps;
}


public Customer GetCustomerByID(String CID) {
	Customer C = new Customer(CID);
	C.setComps(this.GetCustComps(CID));
	C.setConcurrence(this.GetConcByCust(CID));
	C.setFlow(this.GetFlowByCust(CID));
	C.setAlone(this.GetCustAlone(CID));
	return C;
}

}  

