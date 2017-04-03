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
	Connection con; //모든 객체간 공유하기위해서 
	JTable table;
	JScrollPane scoll;
	JPanel p_west, p_center;
	Choice choice;
	String[][] item={
			{"▼테이블을 선택하세요.",""},
			{"사원테이블","emp"},
			{"부서테이블","dept"},
		
			
	};
	
	
//로직의 분리가 필요 MVC 또한 중복코드는 유지보수가 떨어지는 개발 
//쌤정리 : 디자인과 로직을 분리시키기위한 중간자(Controller)의 존재가 필요하다
//JTable에서는 이 컨트롤러의 역할을 TableModel이 해준다
//TableModel을 사용할 경우 JTable 은 자신이 보여줘야할 데이터를 TableModel 로부터
//정보를 얻어와 출력한다
//getColumnCount()
//getRowCount()
//getValueAt()
	//결국!! TableModel model; 하고 table=new JTable(model); 이렇게 해서 getColumnCount 등등씀!
	
//	//테이블에 데이터 넣기 (~에 데이터 넣기 setValueAt)
//	table.setValueAt("사과", 0, 0);
//	table.setValueAt("배", 0, 1);
//	table.setValueAt("장미", 1, 0);
//	table.setValueAt("튤립", 1, 1);
//	table.setValueAt("잉어", 2, 0);
//	table.setValueAt("붕어", 2, 1);

	TableModel[] model=new TableModel[item.length]; //인스턴스의 배열화 ->받을 공간만 활용 새로 new 한것 아니야
	
	public Appmain() {
		manager=ConnectionManager.getInstance();
		con=manager.getConnection(); //이 con 은 윈도우창 닫을 때 죽음
		
		table= new JTable();//결정은 유저가 choice 바뀔때 
		scoll= new JScrollPane(table);
		p_west=new JPanel();
		p_center=new JPanel();
		choice=new Choice();
		
		//테이블 모델들을 올려놓자 
		model[0]=new DefaultTableModel();
		model[1]=new EmpModel(con); //emp  if문 쓰기 싫으니까! 
		//model[2]=new DefaultTableModel(); //dept
		
		//초이스 구성
		for(int i=0;i<item.length;i++){
			choice.add(item[i][0]);
		}
		
		p_west.add(choice);//west 영역의 패널에 초이스부착 
		p_center.add(scoll);
		
		add(p_west, BorderLayout.WEST);
		add(p_center);
	
		//초이스와 리스너 연결
		choice.addItemListener(this);
		
		//윈도우 창 닫을 때 오라클 접속 끊기 
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				manager.disConnect(con);//커넥션 닫기 			
				System.exit(0);//프로그램 종료
			}
			
		});
		
		pack();
		
		setVisible(true);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
	
	}
	//해당 되는 테이블 보여주기
	public void showData(int index){
		System.out.println("당신이 보게 될 테이블은"+item[index][1]);
		table.setModel(model[index]); //index가 결정 0번째면 빈것 1이면 emp 2 이면 dept
		
		//해당되는 테이블 모델을 사용하면 된다 
		//emp --> EmpModel
		//dept --> deptModel
		//아무것도 아니면 --> DefaultTableModel
		
	}
	
	public void itemStateChanged(ItemEvent e) {
		Choice ch=(Choice)e.getSource();
		
		int index=ch.getSelectedIndex();//현재내가 몇번째 옵션 선택했는지
		showData(index);
	}
	public static void main(String[] args) {
		new Appmain();
	}

}
