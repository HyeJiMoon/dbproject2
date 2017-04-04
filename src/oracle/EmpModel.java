//emp 테이블의 데이터를 처리하는 컨트롤러! 
package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

public class EmpModel extends AbstractTableModel{
//여기서 채우는게 아니라 디비에서 연동되는 데이터로 채우는 것
	//String driver="oracle.jdbc.driver.OracleDriver";
	//String url="jdbc:oracle:thin:@localhost:1521:XE";
	//String user="batman";
	//String password="1234";
//위의 네 코드는 외부요인에의해 바뀔 수 있다 또한 부서와 사원테이블이 서로 다른 접속으로 들어온 것이다 .어느 한쪽에서 수정이 일어나면 또 따로 수정해주어야한다
	//프로그램이 하나면 접속도 하나여야한다 따라서 connection의 분리를 배우자! class

	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	String[] column; //컬럼을 넣을 배열
	String[][] data; //레코드를 넣을 배열    여기서는 둘다 몇갠지 모르니까 new하면 안돼 밑에서 컬럼갯수를구하고 !
	
	
	//db연동 먼저@! ->생성자에서
	/*
	 * 1.드라이버 로드
	 * 2.접속
	 * 3.쿼리문 수행
	 * 4.접속닫기
	 * */
	public EmpModel(Connection con) {
		this.con=con;
		//manager=ConnectionManager.getInstance(); //dog 하나 주는거 처럼 
		try {
			
			//접속
			
			//con=manager.getConnection();
			
			if(con!=null){
				String sql="select *from emp";
				
				//아래의 pstmt에 의해 생성되는 rs는 커서가 자유로울 수 있다 
				pstmt=con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				
				//scroll_insentive는 한칸씩 갈수 있는 커서를 끝으로 가게해주는용도 , read_only는 그냥 읽기 전용
				
				//결과 집합 반환!!
				rs=pstmt.executeQuery();
				
				//컬럼을 구해보자!! cf) meta 시스템적인것 컬럼을ㅇ얻어오거나 설정얻어오는것
				ResultSetMetaData meta=rs.getMetaData();
				int count=meta.getColumnCount(); // 컬럼갯수
				
				column=new String[count]; //count만큼 배열 생성!
				//컬럼명을 채우자
				for(int i=0;i<column.length;i++){
					column[i]=meta.getColumnName(i+1); //첫번째컬럼을 1이라고 생각하므로 i+1		
				}		
				rs.last();//제일마지막으로 보냄
				int total=rs.getRow(); //레코드 번호
				rs.beforeFirst();
				
				//총 레코드 수를 알았으니, 이차원 배열 생성해보자
				data = new String[total][column.length];
				
				//레코드를 이차원 배열인 data 에 채워 넣기 ! !
				for(int a=0;a<data.length;a++){//층수 (바깥쪽 칸칸 내려오기)
					rs.next();
					for(int i=0;i<data[a].length;i++){//호수 안에서 101 102돌기
						data[a][i]=rs.getString(column[i]);//층수와 호수 데이터의 넘버라할지라도 애매할때 String Jtable은 스트링을 원헤!그래서 그냥 string으로
					
					}				
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally { //닫는 작업 --> 메모리 누수가 없움!! 
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
	
	//테이블 모델 테이블을 형체만반영 
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
