/*
JTable 이 얹혀질 패널 */
package book;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GridPanel extends JPanel{
	private Connection con;
	ArrayList<Book> list=new ArrayList<Book>();
	
	
	String path="C:/java_workspace2/DBproject2/data/";
	
	
	public GridPanel() {
	
		//init(); 디자인보려고 잠깐 호ㅜ출한것 
		this.setVisible(false); //테이블패널이 먼저 보여지게 false
		this.setBackground(Color.RED);
		setPreferredSize(new Dimension(650, 550));
	
	}
	
	public void setConnection(Connection con){
		this.con=con;
		loadData();
		
	}
	public void loadData(){
		String sql="select * from book order by book_id asc";
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery(); //쿼리 실행이 일어난다 ! //dto랑 dto 못쓴이유
			
			while(rs.next()){
				Book dto=new Book(); //레코드 1건 담기위한 인스턴스 
				dto.setBook_id(rs.getInt("book_id"));
				dto.setBook_name(rs.getString("book_name"));
				dto.setImg(rs.getString("img"));
				dto.setPrice(rs.getInt("price"));
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				list.add(dto);
				
			} //;?
			
			//데이터베이스를 모두 가져왔으므로 , 디자인에 반영
			init();
			
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
	}
	
	public void init(){
		//list 에 들어있는 Book객체만큼 BookItem 을 생성해서 화면에 보여주자 
		for(int i=0;i<list.size();i++){
			Book book=list.get(i); //반환형 Book ! Book도 객체 
			try {
				Image img=ImageIO.read(new File(path+book.getImg()));
				String name=book.getBook_name();
				String price=Integer.toString(book.getPrice());
				
				BookItem item=new BookItem(img, name, price);
				add(item);
				
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		}

	}
}
