package book;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale.Category;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookMain extends JFrame implements ItemListener,ActionListener{
	DBManager manager=DBManager.getInstance();
	Connection con;
	JPanel p_west; //좌측 등록폼
	JPanel p_content; //우측 영역 전체
	JPanel p_north; //우측 선택 모드 영역
	JPanel p_center; //Flow가 적용되어 p_table, p_grid를 모두 존재시켜놓을 컨테이너 역할
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
	File file;
	FileInputStream fis=null;
	FileOutputStream fos=null;
	//String[][] subcategory;//html option과는 다르므로 choice 컴포넌트의 값을 미리 받아놓자 ->배열로! 
										//new못하는 이유 : DB크기 모르니까 메모리에 들어오 시점은 국내냐 외국이냐 선택했을때
	//이제배열따위론 하지 않겠음 arraylist로
	//이 컬렉션은 rs객체를 대체할 것이다. 그럼으로써 얻는 장점 : 더이상 rs.last , rs.getRow, metadatacolumn이런거  할 필요 없다 
	ArrayList<SubCategory> subcategory=new ArrayList<SubCategory>(); //하나하나가 subcategory
	
	
	
	public BookMain() {
		//con=manager.getConnection(); init 에서
		
		p_west =new JPanel();
		p_content=new JPanel();
		p_north=new JPanel();
		p_center=new JPanel();
		
		p_table=new TablePanel();
		p_grid=new GridPanel();
		
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
		
		bt_regist=new JButton("등록");
		group=new CheckboxGroup();
		ch_table=new Checkbox("테이블형식", group, true);
		ch_grid=new Checkbox("갤러리형식",group, false);
		
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
		p_north.setPreferredSize(new Dimension(600, 40));
	
		p_north.add(ch_table);
		p_north.add(ch_grid);
		
		p_center.setBackground(Color.YELLOW);
		p_center.add(p_table);
		p_center.add(p_grid);
		
		p_content.setLayout(new BorderLayout());
		p_content.add(p_north, BorderLayout.NORTH);
		
		p_content.add(p_center);
		
		
		add(p_west,BorderLayout.WEST);
		add(p_content);
		
		init();
		
		ch_top.addItemListener(this);
		
		
		can.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				openFile();
			}
			
		});
		
		bt_regist.addActionListener(this);
		//초이스 컴포넌트와 리스너 연결
		ch_table.addItemListener(this);
		ch_grid.addItemListener(this);
		
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

			//rs.last(); //여기는 커서 마지막까지 오는 것 필요 없음
			while(rs.next()){
				ch_top.add(rs.getString("category_name"));
			}	
			//rs쓰지 말고, 어레이리스트 쓰자 각 레코드별로 클래스 만들어서 뉴한다음에리스트에 넣자
			//rs 에 담겨진 레코드 1개는subcategory 클래스의 인스턴스 1개로 받자 
			
			
			
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
		//테이블 패널과 그리드 패널에게 Connection 전달
		((TablePanel)p_table).setConnection(con);
		((GridPanel)p_grid).setConnection(con);
		//메서드는 자식에게만 있으니까 setConnection을 받아주기위해 자식형으로 변신
	
	}
	//하위 카테고리 가져오기
	public void getSub(String v){
		//기존에 이미 채워진 아이템이 있다면 ,먼저 싹 지운다.
		ch_sub.removeAll();
		
		//String sql="select *from subcategory";
		//sql+="where topcategory_id=(";  ----> 길어짐 stringbuffer
		StringBuffer sb=new StringBuffer();
		sb.append("select * from subcategory");
		sb.append(" where topcategory_id=(");
		sb.append(" select topcategory_id from");
		sb.append(" topcategory where category_name='"+v+"')"); //국내도서 대신에 변수  ->'"+변수+"')
		
		System.out.println(sb.toString());
		PreparedStatement pstmt=null;
		ResultSet rs=null; //try위에올리는 이유는 finally에서 닫으려고
		try {
			pstmt=con.prepareStatement(sb.toString());
			rs=pstmt.executeQuery();
			//rs.last(); 끝을 알려고 하는데 이거는 굉장히 번거로운 따라서 새로운 좋은거배워보자 
			//테이블이 하나만들어지면 테이블을 표현한 클래스를 염두에둔다 select 날리면 인스턴스 8개 생성 
			
			//select 날리면 rs가 받음 
			//
			//전체는 클래스고 카테고리 카테고리_name 탑카테고리 클래스의 멤버변수 
			//데이터만을 담는 클래스  vo 또는 dto?
			
			
			//서브 카테고리의 정보를 2차원배열에 담기 + 출력
			//subcategory=new String[][];  //[레코드수][컬럼수]
			
			while(rs.next()){
				SubCategory dto=new SubCategory();
				//int subcategory_id=rs.getInt("subcategory_id"); 기니까 한번에 받자 
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				dto.setCategory_name(rs.getString("category_name"));
				dto.setTopcategory_id(rs.getInt("topcategory_id"));
				
				subcategory.add(dto);//컬렉션에 담기!!
				ch_sub.add(dto.getCategory_name());
			}
			//while(rs.next()){
			//	ch_sub.add(rs.getString("category_name"));
				
			//}
			
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
	//상품등록 메서드
	public void regist(){
		//내가 지금 선택한 서브 카테고리 초이스의 index를구해서 그 index로 어레이리스트를 접근하여 
		//객체를 반환받으면 정보를 유용하게 쓸 수 있다.
		int index=ch_sub.getSelectedIndex();
		SubCategory dto=subcategory.get(index);
		
		String book_name=t_name.getText();//책이름 
		int price=Integer.parseInt(t_price.getText());
		String img=file.getName(); // 파일명
		
		StringBuffer sb=new StringBuffer();
		sb.append("insert into book(book_id,subcategory_id,book_name, price ,img )"); 
		//오라클에 이미지정보가 자체적으로들어가나? ㅇㅇ 바이트형으로 Blob라한다 근데 데이터아까워서 구냥 URL(이름)로 찾아가자 
		sb.append(" values(seq_book.nextval, "+dto.getSubcategory_id()+",'"+book_name+"',"+price+",'"+img+"')");
		
		System.out.println(sb.toString());
		
		PreparedStatement pstmt=null; //인터페이스라 new 안됨  //얘 왜가져옴????
		try {
			pstmt=con.prepareStatement(sb.toString());
			
			//SQL문이 DML(insert ,delete ,update)같은 경우일때 
			int result=pstmt.executeUpdate(); 
			//위의 메서드는 숫자값을 반환하며, 이 숫자값은 이 쿼리에 의해 영향을 받는 레코드수를 반환
			//insert의 경우는 언제나 ?? 반환될까? 1
			//들어갔다 안들어갔다를 판단 
			if(result!=0){
				//System.out.println(book_name+"등록 성공");
				copy();
				
				((TablePanel)p_table).init();//조회일으킴
				((TablePanel)p_table).table.updateUI(); //UI갱신
				
			}else{
				System.out.println(book_name+"등록 실패");					
			}
		} catch (SQLException e) {
		
			e.printStackTrace();
		}finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}		
		}
	}
	
	//테이블 또는 그리드 선택하는 것 
	public void itemStateChanged(ItemEvent e) {
		Object obj=e.getSource();
		if(obj==ch_top){
			Choice ch=(Choice)e.getSource(); //언제나 초이스가 아니라 체크박스일수도 있자나 따라서 제한!! 
			getSub(ch.getSelectedItem());
		}else if(obj==ch_table){
			//System.out.println("테리블로볼레");
			p_table.setVisible(true);
			p_grid.setVisible(false);
		}else if(obj==ch_grid){
			//System.out.println("그리드로볼레");
			p_table.setVisible(false);
			p_grid.setVisible(true);
			
		}
	}
	public void openFile(){
		int result=chooser.showOpenDialog(this);
		
		if(result==JFileChooser.APPROVE_OPTION){
			//선택한 이미지를 캔버스에 그릴것이다
			file=chooser.getSelectedFile();  //file은 경로 이미지는 toolkit
			img=kit.getImage(file.getAbsolutePath());
			can.repaint();	
		}
	}
	//이미지 복사하기 
	//유저가 선택한 이미지를, 개발자가 지정한 위치로 복사를 해놓자! ->data 파일에 사진 복사하기 
	public void copy(){
	
		try {
			String fileneme=file.getName();
			String dest="C:/java_workspace2/DBproject2/data/"+fileneme;
			fis=new FileInputStream(file);
			fos=new FileOutputStream(dest);
			
			int data; //읽어들인 데이터가 아니고 갯수다..!!실제 가지고있는 데이터 있다없다 
			byte[] b=new byte[1024]; //한번 읽을떄 1024한방에 읽겠다 . 실제 데이터의 양, 값 
			while(true){
				data=fis.read(b);
				if(data==-1)break;
				fos.write(b);
			}
			JOptionPane.showMessageDialog(this,"등록 성공" );
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		//e.getSource(); //object 로 반환하는 이유는 버튼일 수도 있고, 이미지일 수도 있으니까
		//System.out.println("눌렀니?");
		regist();
	}
	
	public static void main(String[] args) {
		new BookMain();
	}



}
