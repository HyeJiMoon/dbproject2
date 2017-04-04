/*
JTable 이 얹혀질 패널 
main에 너무 많으니까 
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
	TableModel model;//테이블모델도필요해 
	//Vector 와 ArrayList는 둘다 같다 ! 차이점 : 동기화 지원 여부 !
	Vector list=new Vector(); //vo dto 
	Vector<String> columnName=new Vector<String>();
	int cols;
	
	
	public TablePanel() {//bookmain에있는 connection을 가져오기 위해서 얻어오기 (생성자메서드로) 근데 안만들어졌으니까 일반메서드로 
		//db연동 
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
		
		//테이블 모델을 JTable에 적용
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
		//테이블에 모델적용
			table.setModel(model);
	}
	//데이터 베이스 가져오기(Book 테이블의 레코드 가져오기)
	public void init(){
		String sql="select * from book order by book_id asc";
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			cols=rs.getMetaData().getColumnCount();
			
			//기존데이터모두 지우기
			list.removeAll(list);
			
			
			
			//rs의 정보를 컬렉션의 DTO로 옮겨담자  rs 는 모든 책들으 다 가지고 있다 하나하나가 인스턴스
			while(rs.next()){
				Vector<String> data =new Vector<String>();
				
				data.add(Integer.toString(rs.getInt("book_id"))); //set 계열로 막 담아 그러고 커서내리고 막 
				data.add(rs.getString("book_name"));
				data.add(Integer.toString(rs.getInt("price")));
				data.add(rs.getString("img"));
				data.add(Integer.toString(rs.getInt("subcategory_id")));
				
				list.add(data); //기존벡터에 벡터추가 vector of vector
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

