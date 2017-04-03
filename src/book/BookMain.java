package book;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookMain extends JFrame implements ItemListener,ActionListener{
	DBManager manager=DBManager.getInstance();
	JPanel p_west; //좌측 등록폼
	JPanel p_content; //우측 영역 전체
	JPanel p_north; //우측 선택 모드 영역
	JPanel p_table; //JPanel이 붙여질 패널
	JPanel p_grid; //그리드방식으로 보여질 패널
	Choice ch_top; //상위 카테고리
	Choice ch_sub; //하위 카테고리
	JTextField t_name; 
	JTextField t_price;
	Canvas can;
	JButton bt_regist;
	CheckboxGroup group;
	Checkbox ch_table, ch_grid;
	Toolkit kit=Toolkit.getDefaultToolkit(); //이것도 일종의 singleTon
	Image img;
	JFileChooser chooser;
	
	
	Connection con;
	
	
	public BookMain() {
		//con=manager.getConnection(); init 에서
		
		p_west =new JPanel();
		p_content=new JPanel();
		p_north=new JPanel();
		p_table=new JPanel();
		p_grid=new JPanel();
		
		ch_top=new Choice();
		ch_sub=new Choice();
		t_name=new JTextField(12);
		t_price=new JTextField(12);
	
		URL url=this.getClass().getResource("/pica.jpg");
			try {
				img=ImageIO.read(url);
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		can=new Canvas(){
			public void paint(Graphics g) { 
				g.drawImage(img, 0,0,140,140,this);
			}
			
		};
		
		bt_regist=new JButton("버툰");
		group=new CheckboxGroup();
		ch_table=new Checkbox("테이블목록", group, true);
		ch_grid=new Checkbox("그리드목록",group, false);
		
		//파일 추저 올리기
		chooser=new JFileChooser("C:/html_workspace/images");
	
		ch_top.setPreferredSize(new Dimension(140, 20));	//초이스 컴포넌트의 크기 폭 조정	
		ch_sub.setPreferredSize(new Dimension(140, 20));
		
		
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(can);
		can.setPreferredSize(new Dimension(140, 140));
		p_west.setPreferredSize(new Dimension(140, 140));
		p_west.add(bt_regist);
		
		p_west.setPreferredSize(new Dimension(150, 600));
		
	
		p_north.add(ch_table);
		p_north.add(ch_grid);
		p_north.setPreferredSize(new Dimension(600, 40));
		
		p_content.setLayout(new BorderLayout());
		p_content.add(p_north, BorderLayout.NORTH);
		p_content.add(p_table);
		
		add(p_west,BorderLayout.WEST);
		add(p_content);
		
		init();
		
		ch_top.addItemListener(this);
	
		
		can.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				openFile();
			}
			
		});
		
		setSize(800,600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public void init(){
		//초이스 컴포넌트에 최상위 목록 보이기 
		con=manager.getConnection();
		
		String sql="select * from topcategory";
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();

			//rs.last(); 여기는 커서 마지막까지 오는 것 필요 없음
			while(rs.next()){
				ch_top.add(rs.getString("category_name"));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
				
					e.printStackTrace();
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
	//하위 카테고리 가져오기
	public void getSub(String v){
		//기존에 이미 채워진 아이템이 있다면 ,먼저 싹 지운다.
		ch_sub.removeAll();
		
		
		
		//String sql="select *from subcategory";
		//sql+="where topcategory_id=(";  ----> 길어짐 stringbuffer
		StringBuffer sb=new StringBuffer();
		sb.append("select *from subcategory");
		sb.append(" where topcategory_id=(");
		sb.append(" select topcategory_id from");
		sb.append(" topcategory where category_name='"+v+"')"); //국내도서 대신에 변수  ->'"+변수+"')
		
		System.out.println(sb.toString());
		PreparedStatement pstmt=null;
		ResultSet rs=null; //try위에올리는 이유는 finally에서 닫으려고
		try {
			pstmt=con.prepareStatement(sb.toString());
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				ch_sub.add(rs.getString("category_name"));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
				
					e.printStackTrace();
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
	public void itemStateChanged(ItemEvent e) {
		Choice ch=(Choice)e.getSource();
		getSub(ch.getSelectedItem());
	}
	public void openFile(){
		int result=chooser.showOpenDialog(this);
		
		if(result==JFileChooser.APPROVE_OPTION){
			//선택한 이미지를 캔버스에 그릴것이다
			File file=chooser.getSelectedFile();  //file은 경로 이미지는 toolkit
			img=kit.getImage(file.getAbsolutePath());
			can.repaint();
			
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		e.getSource(); //object 로 반환하는 이유는 버튼일 수도 있고, 이미지일 수도 있으니까
		System.out.println("눌렀니?");
	}
	
	public static void main(String[] args) {
		new BookMain();
	}



}
