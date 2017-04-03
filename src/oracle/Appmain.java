package oracle;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Appmain extends JFrame implements ItemListener{
	ConnectionManager manager;
	Connection con; //��� ��ü�� �����ϱ����ؼ� 
	JTable table;
	JScrollPane scoll;
	JPanel p_west, p_center;
	Choice choice;
	String[][] item={
			{"�����̺��� �����ϼ���.",""},
			{"������̺�","emp"},
			{"�μ����̺�","dept"},
		
			
	};
	
	
//������ �и��� �ʿ� MVC ���� �ߺ��ڵ�� ���������� �������� ���� 
//������ : �����ΰ� ������ �и���Ű������ �߰���(Controller)�� ���簡 �ʿ��ϴ�
//JTable������ �� ��Ʈ�ѷ��� ������ TableModel�� ���ش�
//TableModel�� ����� ��� JTable �� �ڽ��� ��������� �����͸� TableModel �κ���
//������ ���� ����Ѵ�
//getColumnCount()
//getRowCount()
//getValueAt()
	//�ᱹ!! TableModel model; �ϰ� table=new JTable(model); �̷��� �ؼ� getColumnCount ��!
	
//	//���̺� ������ �ֱ� (~�� ������ �ֱ� setValueAt)
//	table.setValueAt("���", 0, 0);
//	table.setValueAt("��", 0, 1);
//	table.setValueAt("���", 1, 0);
//	table.setValueAt("ƫ��", 1, 1);
//	table.setValueAt("�׾�", 2, 0);
//	table.setValueAt("�ؾ�", 2, 1);

	TableModel[] model=new TableModel[item.length]; //�ν��Ͻ��� �迭ȭ ->���� ������ Ȱ�� ���� new �Ѱ� �ƴϾ�
	
	public Appmain() {
		manager=ConnectionManager.getInstance();
		con=manager.getConnection(); //�� con �� ������â ���� �� ����
		
		table= new JTable();//������ ������ choice �ٲ� 
		scoll= new JScrollPane(table);
		p_west=new JPanel();
		p_center=new JPanel();
		choice=new Choice();
		
		//���̺� �𵨵��� �÷����� 
		model[0]=new DefaultTableModel();
		model[1]=new EmpModel(con); //emp  if�� ���� �����ϱ�! 
		//model[2]=new DefaultTableModel(); //dept
		
		//���̽� ����
		for(int i=0;i<item.length;i++){
			choice.add(item[i][0]);
		}
		
		p_west.add(choice);//west ������ �гο� ���̽����� 
		p_center.add(scoll);
		
		add(p_west, BorderLayout.WEST);
		add(p_center);
	
		//���̽��� ������ ����
		choice.addItemListener(this);
		
		//������ â ���� �� ����Ŭ ���� ���� 
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				manager.disConnect(con);//Ŀ�ؼ� �ݱ� 			
				System.exit(0);//���α׷� ����
			}
			
		});
		
		pack();
		
		setVisible(true);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
	
	}
	//�ش� �Ǵ� ���̺� �����ֱ�
	public void showData(int index){
		System.out.println("����� ���� �� ���̺���"+item[index][1]);
		table.setModel(model[index]); //index�� ���� 0��°�� ��� 1�̸� emp 2 �̸� dept
		
		//�ش�Ǵ� ���̺� ���� ����ϸ� �ȴ� 
		//emp --> EmpModel
		//dept --> deptModel
		//�ƹ��͵� �ƴϸ� --> DefaultTableModel
		
	}
	
	public void itemStateChanged(ItemEvent e) {
		Choice ch=(Choice)e.getSource();
		
		int index=ch.getSelectedIndex();//���系�� ���° �ɼ� �����ߴ���
		showData(index);
	}
	public static void main(String[] args) {
		new Appmain();
	}

}
