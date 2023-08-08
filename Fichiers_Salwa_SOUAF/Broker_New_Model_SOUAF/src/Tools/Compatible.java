package Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import Components.Component;
import Provider.InstanceType;

public class Compatible {
	
	private Set<InstanceType> ITs;
	private Component Cp;
	
	public Compatible(Component cp) {
		super();
		Cp = cp ;
		ITs = GetCompatibleIT(cp);
	}
	
	public Set<InstanceType> getITs() {
		return ITs;
	}
	public void setIT(Set<InstanceType> iT) {
		ITs = iT;
	}
	public Component getCp() {
		return Cp;
	}
	public void setCp(Component cp) {
		Cp = cp;
	}
	
	public Set<InstanceType> GetCompatibleIT(Component C){
		
		HashSet<InstanceType> CompITs = new HashSet<InstanceType>();
		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://localhost:8889/Broker1","root","root"); 
			PreparedStatement pstmt = con.prepareStatement("select * from InstanceType where vCPU < ? and vCPU >= ? and Memory < ? and Memory >= ? and OS like ? and Location like ?");
			pstmt.setInt(1, 2*C.getVcpu());
			pstmt.setInt(2, C.getVcpu());
			pstmt.setInt(3, 2*C.getMemory());
			pstmt.setInt(4, C.getMemory());
			pstmt.setString(5, C.getOs());
			pstmt.setString(6, C.getLocation()+"%");
			
			ResultSet rs = pstmt.executeQuery();

			while(rs.next())  {
				InstanceType it = new InstanceType(rs.getString(1), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6),rs.getDouble(8));
				CompITs.add(it);
				  
				}
			con.close();  
			}catch(Exception e){ System.out.println(e);}  
		if(CompITs.isEmpty()) {
			System.out.println("DEBUG inside compatibile for Coponent : " + C.getCmpID());
		}
		
		return CompITs;
		
	}
	
	

}
