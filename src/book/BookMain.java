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
	JPanel p_west; //���� �����
	JPanel p_content; //���� ���� ��ü
	JPanel p_north; //���� ���� ��� ����
	JPanel p_center; //Flow�� ����Ǿ� p_table, p_grid�� ��� ������ѳ��� �����̳� ����
	JPanel p_table; //JPanel�� �ٿ��� �г�
	JPanel p_grid; //�׸��������� ������ �г�
	
	Choice ch_top; //���� ī�װ�
	Choice ch_sub; //���� ī�װ�
	JTextField t_name; 
	JTextField t_price;
	Canvas can;
	JButton bt_regist;
	CheckboxGroup group;
	Checkbox ch_table, ch_grid;
	Toolkit kit=Toolkit.getDefaultToolkit(); //�̰͵� ������ singleTon
	Image img;
	JFileChooser chooser;
	File file;
	FileInputStream fis=null;
	FileOutputStream fos=null;
	//String[][] subcategory;//html option���� �ٸ��Ƿ� choice ������Ʈ�� ���� �̸� �޾Ƴ��� ->�迭��! 
										//new���ϴ� ���� : DBũ�� �𸣴ϱ� �޸𸮿� ���� ������ ������ �ܱ��̳� ����������
	//�����迭������ ���� �ʰ��� arraylist��
	//�� �÷����� rs��ü�� ��ü�� ���̴�. �׷����ν� ��� ���� : ���̻� rs.last , rs.getRow, metadatacolumn�̷���  �� �ʿ� ���� 
	ArrayList<SubCategory> subcategory=new ArrayList<SubCategory>(); //�ϳ��ϳ��� subcategory
	
	
	
	public BookMain() {
		//con=manager.getConnection(); init ����
		
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
		
		bt_regist=new JButton("���");
		group=new CheckboxGroup();
		ch_table=new Checkbox("���̺�����", group, true);
		ch_grid=new Checkbox("����������",group, false);
		
		//���� ���� �ø���
		chooser=new JFileChooser("C:/html_workspace/images");
	
		ch_top.setPreferredSize(new Dimension(140, 20));	//���̽� ������Ʈ�� ũ�� �� ����	
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
		//���̽� ������Ʈ�� ������ ����
		ch_table.addItemListener(this);
		ch_grid.addItemListener(this);
		
		setSize(800,600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public void init(){
		//���̽� ������Ʈ�� �ֻ��� ��� ���̱� 
		con=manager.getConnection();
		
		String sql="select * from topcategory";
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();

			//rs.last(); //����� Ŀ�� ���������� ���� �� �ʿ� ����
			while(rs.next()){
				ch_top.add(rs.getString("category_name"));
			}	
			//rs���� ����, ��̸���Ʈ ���� �� ���ڵ庰�� Ŭ���� ���� ���Ѵ���������Ʈ�� ����
			//rs �� ����� ���ڵ� 1����subcategory Ŭ������ �ν��Ͻ� 1���� ���� 
			
			
			
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
		//���̺� �гΰ� �׸��� �гο��� Connection ����
		((TablePanel)p_table).setConnection(con);
		((GridPanel)p_grid).setConnection(con);
		//�޼���� �ڽĿ��Ը� �����ϱ� setConnection�� �޾��ֱ����� �ڽ������� ����
	
	}
	//���� ī�װ� ��������
	public void getSub(String v){
		//������ �̹� ä���� �������� �ִٸ� ,���� �� �����.
		ch_sub.removeAll();
		
		//String sql="select *from subcategory";
		//sql+="where topcategory_id=(";  ----> ����� stringbuffer
		StringBuffer sb=new StringBuffer();
		sb.append("select * from subcategory");
		sb.append(" where topcategory_id=(");
		sb.append(" select topcategory_id from");
		sb.append(" topcategory where category_name='"+v+"')"); //�������� ��ſ� ����  ->'"+����+"')
		
		System.out.println(sb.toString());
		PreparedStatement pstmt=null;
		ResultSet rs=null; //try�����ø��� ������ finally���� ��������
		try {
			pstmt=con.prepareStatement(sb.toString());
			rs=pstmt.executeQuery();
			//rs.last(); ���� �˷��� �ϴµ� �̰Ŵ� ������ ���ŷο� ���� ���ο� �����Ź������ 
			//���̺��� �ϳ���������� ���̺��� ǥ���� Ŭ������ ���ο��д� select ������ �ν��Ͻ� 8�� ���� 
			
			//select ������ rs�� ���� 
			//
			//��ü�� Ŭ������ ī�װ� ī�װ�_name žī�װ� Ŭ������ ������� 
			//�����͸��� ��� Ŭ����  vo �Ǵ� dto?
			
			
			//���� ī�װ��� ������ 2�����迭�� ��� + ���
			//subcategory=new String[][];  //[���ڵ��][�÷���]
			
			while(rs.next()){
				SubCategory dto=new SubCategory();
				//int subcategory_id=rs.getInt("subcategory_id"); ��ϱ� �ѹ��� ���� 
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				dto.setCategory_name(rs.getString("category_name"));
				dto.setTopcategory_id(rs.getInt("topcategory_id"));
				
				subcategory.add(dto);//�÷��ǿ� ���!!
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
	//��ǰ��� �޼���
	public void regist(){
		//���� ���� ������ ���� ī�װ� ���̽��� index�����ؼ� �� index�� ��̸���Ʈ�� �����Ͽ� 
		//��ü�� ��ȯ������ ������ �����ϰ� �� �� �ִ�.
		int index=ch_sub.getSelectedIndex();
		SubCategory dto=subcategory.get(index);
		
		String book_name=t_name.getText();//å�̸� 
		int price=Integer.parseInt(t_price.getText());
		String img=file.getName(); // ���ϸ�
		
		StringBuffer sb=new StringBuffer();
		sb.append("insert into book(book_id,subcategory_id,book_name, price ,img )"); 
		//����Ŭ�� �̹��������� ��ü�����ε���? ���� ����Ʈ������ Blob���Ѵ� �ٵ� �����;Ʊ���� ���� URL(�̸�)�� ã�ư��� 
		sb.append(" values(seq_book.nextval, "+dto.getSubcategory_id()+",'"+book_name+"',"+price+",'"+img+"')");
		
		System.out.println(sb.toString());
		
		PreparedStatement pstmt=null; //�������̽��� new �ȵ�  //�� �ְ�����????
		try {
			pstmt=con.prepareStatement(sb.toString());
			
			//SQL���� DML(insert ,delete ,update)���� ����϶� 
			int result=pstmt.executeUpdate(); 
			//���� �޼���� ���ڰ��� ��ȯ�ϸ�, �� ���ڰ��� �� ������ ���� ������ �޴� ���ڵ���� ��ȯ
			//insert�� ���� ������ ?? ��ȯ�ɱ�? 1
			//���� �ȵ��ٸ� �Ǵ� 
			if(result!=0){
				//System.out.println(book_name+"��� ����");
				copy();
				
				((TablePanel)p_table).init();//��ȸ����Ŵ
				((TablePanel)p_table).table.updateUI(); //UI����
				
			}else{
				System.out.println(book_name+"��� ����");					
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
	
	//���̺� �Ǵ� �׸��� �����ϴ� �� 
	public void itemStateChanged(ItemEvent e) {
		Object obj=e.getSource();
		if(obj==ch_top){
			Choice ch=(Choice)e.getSource(); //������ ���̽��� �ƴ϶� üũ�ڽ��ϼ��� ���ڳ� ���� ����!! 
			getSub(ch.getSelectedItem());
		}else if(obj==ch_table){
			//System.out.println("�׸���κ���");
			p_table.setVisible(true);
			p_grid.setVisible(false);
		}else if(obj==ch_grid){
			//System.out.println("�׸���κ���");
			p_table.setVisible(false);
			p_grid.setVisible(true);
			
		}
	}
	public void openFile(){
		int result=chooser.showOpenDialog(this);
		
		if(result==JFileChooser.APPROVE_OPTION){
			//������ �̹����� ĵ������ �׸����̴�
			file=chooser.getSelectedFile();  //file�� ��� �̹����� toolkit
			img=kit.getImage(file.getAbsolutePath());
			can.repaint();	
		}
	}
	//�̹��� �����ϱ� 
	//������ ������ �̹�����, �����ڰ� ������ ��ġ�� ���縦 �س���! ->data ���Ͽ� ���� �����ϱ� 
	public void copy(){
	
		try {
			String fileneme=file.getName();
			String dest="C:/java_workspace2/DBproject2/data/"+fileneme;
			fis=new FileInputStream(file);
			fos=new FileOutputStream(dest);
			
			int data; //�о���� �����Ͱ� �ƴϰ� ������..!!���� �������ִ� ������ �ִپ��� 
			byte[] b=new byte[1024]; //�ѹ� ������ 1024�ѹ濡 �аڴ� . ���� �������� ��, �� 
			while(true){
				data=fis.read(b);
				if(data==-1)break;
				fos.write(b);
			}
			JOptionPane.showMessageDialog(this,"��� ����" );
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
		//e.getSource(); //object �� ��ȯ�ϴ� ������ ��ư�� ���� �ְ�, �̹����� ���� �����ϱ�
		//System.out.println("������?");
		regist();
	}
	
	public static void main(String[] args) {
		new BookMain();
	}



}
