/*
 * 책 1권을 표현하는 UI 컴포넌트 
 * 우리만의 컴포넌트로 커스터마이징 하자!!!! 
 * */
package book;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class BookItem extends JPanel{
	Canvas can;
	JLabel la_name , la_price;
	

	public BookItem(Image img, String name, String price) {
		can = new Canvas(){
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, 120,120,this); //관찰자는 canvas자신이니까
				
		}
			
		};
		la_name=new JLabel(name);
		la_price=new JLabel(price);
	
		add(can);
		add(la_name);
		add(la_price);
		
		can.setPreferredSize(new Dimension(120, 120));
		setPreferredSize(new Dimension(120, 180));
		
		setBackground(Color.GRAY);
	
	
	}
}
