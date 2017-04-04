/*
JTable �� ������ �г� 
main�� �ʹ� �����ϱ� 
*/
package book;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class TablePanel extends JPanel{
	Connection con;
	JTable table;
	JScrollPane scroll;
	TableModel model;//���̺�𵨵��ʿ��� 
	//Vector �� ArrayList�� �Ѵ� ���� ! ������ : ����ȭ ���� ���� !
	Vector list=new Vector(); //vo dto 
	Vector<String> columnName=new Vector<String>();
	int cols;
	
	
	public TablePanel() {//bookmain���ִ� connection�� �������� ���ؼ� ������ (�����ڸ޼����) �ٵ� �ȸ���������ϱ� �Ϲݸ޼���� 
		//db���� 
		this.con=con;
		table = new JTable();
		scroll= new JScrollPane(table);
		this.setLayout(new BorderLayout());
		this.add(scroll);
		
		this.setBackground(Color.PINK);
		setPreferredSize(new Dimension(650, 550));
		
		
	}
	
	public void setConnection(Connection con){
		this.con=con;
		
		init();
		
		//���̺� ���� JTable�� ����
		model=new AbstractTableModel() {
			public int getRowCount() {	
				return list.size();
			}
			public int getColumnCount() {
			
				return cols;
			}
			public Object getValueAt(int row, int col) {
				Vector vec=(Vector)list.get(row);
				return vec.elementAt(col);
			}
		};
		//���̺� ������
			table.setModel(model);
	}
	//������ ���̽� ��������(Book ���̺��� ���ڵ� ��������)
	public void init(){
		String sql="select * from book order by book_id asc";
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			cols=rs.getMetaData().getColumnCount();
			
			//���������͸�� �����
			list.removeAll(list);
			
			
			
			//rs�� ������ �÷����� DTO�� �Űܴ���  rs �� ��� å���� �� ������ �ִ� �ϳ��ϳ��� �ν��Ͻ�
			while(rs.next()){
				Vector<String> data =new Vector<String>();
				
				data.add(Integer.toString(rs.getInt("book_id"))); //set �迭�� �� ��� �׷��� Ŀ�������� �� 
				data.add(rs.getString("book_name"));
				data.add(Integer.toString(rs.getInt("price")));
				data.add(rs.getString("img"));
				data.add(Integer.toString(rs.getInt("subcategory_id")));
				
				list.add(data); //�������Ϳ� �����߰� vector of vector
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
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
}

