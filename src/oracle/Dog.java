//������ Ŭ������ �ν��Ͻ��� ���� 1���� ����� 
//UseDog ���� new �ѹ��� �ϰ� �ι�° new ���� ����! 

/*
 * java SE,
 * java EE ��ޱ�� (java SE�� �����Ͽ� ����� ���ø����̼� ���ۿ� ����)
 * 
 * 
 * */

package oracle;

public class Dog {
	static private Dog instance; //Dog���� ������ �� �ְ� �������  //instance���� �����ü��ְ� ���� -> static  -- �ι�°
	
	//new �� ���� ������ ��¥! 
	private Dog(){ //������ private Dog�� �� Ŭ���� �ȿ����� ��밡��  -- ù��° 
						  
		
		
	}
	
	static public Dog getInstance() { //�����ڸ� ���� ���������� �޼��带 ���� �������� //�ν��Ͻ��޼��� new �� ���Ƽ� �� �޼��嵵 �Ұ� ���� static 
		if(instance==null){
			instance=new Dog();
			//null�̸� dog�Ѹ��� �ö� heap ���� 
			//static�̴ϱ� ������ ������ �Ѹ����� ����Ŵ 
			//������ �� ȣ���Ѵ� �׷��� null�� �ƴ� �Ѹ��� ���� �ֱ� ������ �θ��� �ȸ�������� ��� �Ѹ��� ���� 
			//�̷� ���� ����� SingleTon pattern �̶� �Ѵ�(���� ���� �� �ϳ�!)
			//	private Dog(){} �̶� public �� X 
			
		
		}
		return instance;
	}
}
