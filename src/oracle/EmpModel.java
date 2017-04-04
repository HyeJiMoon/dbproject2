//emp ���̺��� �����͸� ó���ϴ� ��Ʈ�ѷ�! 
package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

public class EmpModel extends AbstractTableModel{
//���⼭ ä��°� �ƴ϶� ��񿡼� �����Ǵ� �����ͷ� ä��� ��
	//String driver="oracle.jdbc.driver.OracleDriver";
	//String url="jdbc:oracle:thin:@localhost:1521:XE";
	//String user="batman";
	//String password="1234";
//���� �� �ڵ�� �ܺο��ο����� �ٲ� �� �ִ� ���� �μ��� ������̺��� ���� �ٸ� �������� ���� ���̴� .��� ���ʿ��� ������ �Ͼ�� �� ���� �������־���Ѵ�
	//���α׷��� �ϳ��� ���ӵ� �ϳ������Ѵ� ���� connection�� �и��� �����! class

	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	String[] column; //�÷��� ���� �迭
	String[][] data; //���ڵ带 ���� �迭    ���⼭�� �Ѵ� ��� �𸣴ϱ� new�ϸ� �ȵ� �ؿ��� �÷����������ϰ� !
	
	
	//db���� ����@! ->�����ڿ���
	/*
	 * 1.����̹� �ε�
	 * 2.����
	 * 3.������ ����
	 * 4.���Ӵݱ�
	 * */
	public EmpModel(Connection con) {
		this.con=con;
		//manager=ConnectionManager.getInstance(); //dog �ϳ� �ִ°� ó�� 
		try {
			
			//����
			
			//con=manager.getConnection();
			
			if(con!=null){
				String sql="select *from emp";
				
				//�Ʒ��� pstmt�� ���� �����Ǵ� rs�� Ŀ���� �����ο� �� �ִ� 
				pstmt=con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				
				//scroll_insentive�� ��ĭ�� ���� �ִ� Ŀ���� ������ �������ִ¿뵵 , read_only�� �׳� �б� ����
				
				//��� ���� ��ȯ!!
				rs=pstmt.executeQuery();
				
				//�÷��� ���غ���!! cf) meta �ý������ΰ� �÷����������ų� ���������°�
				ResultSetMetaData meta=rs.getMetaData();
				int count=meta.getColumnCount(); // �÷�����
				
				column=new String[count]; //count��ŭ �迭 ����!
				//�÷����� ä����
				for(int i=0;i<column.length;i++){
					column[i]=meta.getColumnName(i+1); //ù��°�÷��� 1�̶�� �����ϹǷ� i+1		
				}		
				rs.last();//���ϸ��������� ����
				int total=rs.getRow(); //���ڵ� ��ȣ
				rs.beforeFirst();
				
				//�� ���ڵ� ���� �˾�����, ������ �迭 �����غ���
				data = new String[total][column.length];
				
				//���ڵ带 ������ �迭�� data �� ä�� �ֱ� ! !
				for(int a=0;a<data.length;a++){//���� (�ٱ��� ĭĭ ��������)
					rs.next();
					for(int i=0;i<data[a].length;i++){//ȣ�� �ȿ��� 101 102����
						data[a][i]=rs.getString(column[i]);//������ ȣ�� �������� �ѹ��������� �ָ��Ҷ� String Jtable�� ��Ʈ���� ����!�׷��� �׳� string����
					
					}				
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally { //�ݴ� �۾� --> �޸� ������ ����!! 
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}	
			}
		}	
	}
	
	//���̺� �� ���̺��� ��ü���ݿ� 
	public int getRowCount() {
		return data.length;
	}

	public int getColumnCount() {	
		return column.length;
	}
	public String getColumnName(int index) {		
		return column[index];
	}
	
	public Object getValueAt(int row, int col) {
	
		return data[row][col];
	}
	

}
