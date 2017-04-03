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
	JPanel p_west; //���� �����
	JPanel p_content; //���� ���� ��ü
	JPanel p_north; //���� ���� ��� ����
	JPanel p_table; //JPanel�� �ٿ��� �г�
	JPanel p_grid; //�׸��������� ������ �г�
	Choice ch_top; //���� ī�װ���
	Choice ch_sub; //���� ī�װ���
	JTextField t_name; 
	JTextField t_price;
	Canvas can;
	JButton bt_regist;
	CheckboxGroup group;
	Checkbox ch_table, ch_grid;
	Toolkit kit=Toolkit.getDefaultToolkit(); //�̰͵� ������ singleTon
	Image img;
	JFileChooser chooser;
	
	
	Connection con;
	
	
	public BookMain() {
		//con=manager.getConnection(); init ����
		
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
		
		bt_regist=new JButton("����");
		group=new CheckboxGroup();
		ch_table=new Checkbox("���̺����", group, true);
		ch_grid=new Checkbox("�׸�����",group, false);
		
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
		//���̽� ������Ʈ�� �ֻ��� ��� ���̱� 
		con=manager.getConnection();
		
		String sql="select * from topcategory";
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();

			//rs.last(); ����� Ŀ�� ���������� ���� �� �ʿ� ����
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
	//���� ī�װ��� ��������
	public void getSub(String v){
		//������ �̹� ä���� �������� �ִٸ� ,���� �� �����.
		ch_sub.removeAll();
		
		
		
		//String sql="select *from subcategory";
		//sql+="where topcategory_id=(";  ----> ����� stringbuffer
		StringBuffer sb=new StringBuffer();
		sb.append("select *from subcategory");
		sb.append(" where topcategory_id=(");
		sb.append(" select topcategory_id from");
		sb.append(" topcategory where category_name='"+v+"')"); //�������� ��ſ� ����  ->'"+����+"')
		
		System.out.println(sb.toString());
		PreparedStatement pstmt=null;
		ResultSet rs=null; //try�����ø��� ������ finally���� ��������
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
			//������ �̹����� ĵ������ �׸����̴�
			File file=chooser.getSelectedFile();  //file�� ��� �̹����� toolkit
			img=kit.getImage(file.getAbsolutePath());
			can.repaint();
			
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		e.getSource(); //object �� ��ȯ�ϴ� ������ ��ư�� ���� �ְ�, �̹����� ���� �����ϱ�
		System.out.println("������?");
	}
	
	public static void main(String[] args) {
		new BookMain();
	}



}