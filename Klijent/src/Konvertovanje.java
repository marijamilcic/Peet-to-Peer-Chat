
public class Konvertovanje {

	
		public String HexToDec(String num) {
			int x=Integer.parseInt(num, 16); 
			return Integer.toString(x, 10);
		}
		
		public String OctToFour(String num) {
			int x=Integer.parseInt(num, 8);
			return Integer.toString(x, 4);
		}
		
		public String BinToDec(String num) {
			int x=Integer.parseInt(num, 2);
			return Integer.toString(x, 10);
		}
		
		public String FiveToSeven(String num) {
			int x=Integer.parseInt(num, 5);
			return Integer.toString(x, 7);
		}
		public String DecToHex (String num) {
			int x=Integer.parseInt(num, 10); 
			return Integer.toString(x, 16);
		}
		
		public String FourToOct(String num) {
			int x=Integer.parseInt(num, 4);
			return Integer.toString(x, 8);
		}
		
		public String DecToBin (String num) {
			int x=Integer.parseInt(num, 10);
			return Integer.toString(x, 2);
		}
		
		public String SevenToFive (String num) {
			int x=Integer.parseInt(num, 7);
			return Integer.toString(x, 5);
		}
	
	
}
