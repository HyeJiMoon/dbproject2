/*
 * 1. 데이터베이스 접속관련 정보를 여러군데 두지 않기 위함
 * 
 * 2. 싱글턴으로 관리함으로써, 인스턴스를 불필요하게 많이 만들지 않아도 된다 
 * 
 * 3. 싱글턴 안의 connection 을 멤버로 보유하고 있으므로 ,connection 을 한번만 생성할 수 있다 
 * 
 * */

package book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	static private DBManager instance;
	private String driver="oracle.jdbc.driver.OracleDriver";

	private String url="jdbc:oracle:thin:@localhost:1521:XE";
	private String user="batman";
	private String password="1234";
	Connection con; //데이터베이스관련된 정보들
	
	private DBManager(){
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(url,user, password);
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	public Connection getConnection(){
		
		return con;
	}
	public void disConnect(Connection con){
		try {
			con.close();
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		
	}
	static public DBManager getInstance() {
		if(instance==null){
			instance=new DBManager();	
		}
		return instance;
	}
}
