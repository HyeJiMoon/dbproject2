/*(일단 이 아래문장들 제껴 empModel에서 finally안하면 문제가 데지만 끝내줘서 문제될 건없는데 일단 )
 * 각 TableModel 마다 접속 정보와 접속객체를 두게 되면, 
 * 접속정보가 바뀔 때 모든 클래스의 코드도 수정해야하는 유지보수상의 문제뿐만 아니라,
 * 각TableModel 마다 Connection을 생성하기 때문에 접속이 여러개 발생한다.
 * 하나의 어플리케이션이 오라클과 맺는 접속은 1개만으로도 충분하다.
 * 그리고 접속이 여러개이면 하나의 세션에서 발생시키는 각종DML작업이 통일되지 못하게 된다.
 * 즉다른 사람으로 인식된다.
 * 
 * 
 * 
 * 객체의 인스턴스를 메모리 힙에 1개만 만드는 방법 
 * */

package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	static private ConnectionManager instance; //----2 다른 이름써도데는데 instance이름 많이써! + null인 상태
	String driver="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:XE";
	String user="batman";
	String password="1234";
	
	Connection con;
	
	//개발자가 제공하는 방법 이외의 접근은 아예 차단하자
	//사용자에 의한 임의 생성을 막자! 즉 new 를 막자! ---1 
	private ConnectionManager() { //생성자에 넣었기때문에 하나만 커넥션이만들어짐 
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(url, user, password);
		}catch (ClassNotFoundException e){
			e.printStackTrace();
			
		}	catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	//인스턴스의 생성없이도 외부에서 메서드를 호출하여 이 객체의 인스턴스를 가져갈 수 있도록 getter 를 제공해준다! ---3  호출할 수 있는 기회 부여 static---4 위에도 
	static public ConnectionManager getInstance(){
		if(instance==null){
			instance=new ConnectionManager(); //클래스 원본에 붙어있는 클래스변수인 static으로 선언되어있는 instance를 가리킴  
			
		}
		return instance;
	
	}
	
	//이 메서드 호출자는 Connection객체를 반환받게 된다 
	public Connection getConnection(){
		return con;
		
	}
	//connection을 다 사용후 닫기 
	public void disConnect(Connection con){ //사용후 닫을 떄 con을 넘겨줘
		try {
			con.close();
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
	}
}
