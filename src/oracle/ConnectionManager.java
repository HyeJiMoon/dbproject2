/*(�ϴ� �� �Ʒ������ ���� empModel���� finally���ϸ� ������ ������ �����༭ ������ �Ǿ��µ� �ϴ� )
 * �� TableModel ���� ���� ������ ���Ӱ�ü�� �ΰ� �Ǹ�, 
 * ���������� �ٲ� �� ��� Ŭ������ �ڵ嵵 �����ؾ��ϴ� ������������ �����Ӹ� �ƴ϶�,
 * ��TableModel ���� Connection�� �����ϱ� ������ ������ ������ �߻��Ѵ�.
 * �ϳ��� ���ø����̼��� ����Ŭ�� �δ� ������ 1�������ε� ����ϴ�.
 * �׸��� ������ �������̸� �ϳ��� ���ǿ��� �߻���Ű�� ����DML�۾��� ���ϵ��� ���ϰ� �ȴ�.
 * ��ٸ� ������� �νĵȴ�.
 * 
 * 
 * 
 * ��ü�� �ν��Ͻ��� �޸� ���� 1���� ����� ��� 
 * */

package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	static private ConnectionManager instance; //----2 �ٸ� �̸��ᵵ���µ� instance�̸� ���̽�! + null�� ����
	String driver="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:XE";
	String user="batman";
	String password="1234";
	
	Connection con;
	
	//�����ڰ� �����ϴ� ��� �̿��� ������ �ƿ� ��������
	//����ڿ� ���� ���� ������ ����! �� new �� ����! ---1 
	private ConnectionManager() { //�����ڿ� �־��⶧���� �ϳ��� Ŀ�ؼ��̸������ 
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(url, user, password);
		}catch (ClassNotFoundException e){
			e.printStackTrace();
			
		}	catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	//�ν��Ͻ��� �������̵� �ܺο��� �޼��带 ȣ���Ͽ� �� ��ü�� �ν��Ͻ��� ������ �� �ֵ��� getter �� �������ش�! ---3  ȣ���� �� �ִ� ��ȸ �ο� static---4 ������ 
	static public ConnectionManager getInstance(){
		if(instance==null){
			instance=new ConnectionManager(); //Ŭ���� ������ �پ��ִ� Ŭ���������� static���� ����Ǿ��ִ� instance�� ����Ŵ  
			
		}
		return instance;
	
	}
	
	//�� �޼��� ȣ���ڴ� Connection��ü�� ��ȯ�ް� �ȴ� 
	public Connection getConnection(){
		return con;
		
	}
	//connection�� �� ����� �ݱ� 
	public void disConnect(Connection con){ //����� ���� �� con�� �Ѱ���
		try {
			con.close();
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
	}
}
