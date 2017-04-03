//강아지 클래스의 인스턴스를 오직 1개만 만들기 
//UseDog 에서 new 한번만 하고 두번째 new 에선 에러! 

/*
 * java SE,
 * java EE 고급기술 (java SE를 포함하여 기업용 어플리케이션 제작에 사용됨)
 * 
 * 
 * */

package oracle;

public class Dog {
	static private Dog instance; //Dog형을 가져올 수 있게 만들어줌  //instance변수 가져올수있게 하자 -> static  -- 두번째
	
	//new 에 의한 생성을 막짜! 
	private Dog(){ //생성자 private Dog은 이 클래스 안에서만 사용가능  -- 첫번째 
						  
		
		
	}
	
	static public Dog getInstance() { //생성자를 통해 못가져오고 메서드를 통해 가져오게 //인스턴스메서드 new 를 막아서 이 메서드도 불가 따라서 static 
		if(instance==null){
			instance=new Dog();
			//null이면 dog한마리 올라감 heap 으로 
			//static이니까 원본이 강아지 한마리를 가리킴 
			//누군가 또 호출한다 그러면 null이 아닌 한마리 값이 있기 때문에 두마리 안만들어지고 계속 한마리 상태 
			//이런 개발 방법을 SingleTon pattern 이라 한다(개발 패턴 중 하나!)
			//	private Dog(){} 이때 public 은 X 
			
		
		}
		return instance;
	}
}
