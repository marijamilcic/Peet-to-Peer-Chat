import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestKlijent {

	static Socket soketZaKomunikacijuS = null;
	static Socket soketZaKomunikacijuK = null;
	int portS;
	static int portK;
	int portCS;

	public static void main(String[] args) {
		
		KlijentServertest klijent = new KlijentServertest();
		System.out.println("kreiran objekat KlijentServertest");
		klijent.inicijalizuj(2345, 9999); // startovanje klijenta za konverziju

		System.out.println("pokrenuta nit");
		// startovanje klijenta kao servera
		Socket klijentSoket = null;

		try {

			ServerSocket serverSoket = new ServerSocket(9999);
			System.out.println("kreiran serverSocket");
			
			while (true) {
				System.out.println("ocekuje druge klijente...");
				klijentSoket = serverSoket.accept();
				KlijentNit sn = new KlijentNit(klijentSoket);
				sn.start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
