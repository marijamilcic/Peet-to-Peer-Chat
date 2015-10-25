import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class CentralServer {

	LinkedList<ServerNit> klijenti = new LinkedList<>();

	public static void main(String[] args) {
		CentralServer server1 = new CentralServer();
		System.out.println("kreiran objekat CentralServera");
		server1.pokreni(2345);
	}

	public void pokreni(int port) {
		Socket klijentSoket = null;

		System.out.println("pokretanje...");
		try {

			ServerSocket serverSoket = new ServerSocket(port);
			while (true) {
				System.out.println("ocekuje klijenta");

				klijentSoket = serverSoket.accept();
				// serverSoket.getInetAddress();
				ServerNit sn = new ServerNit(this, klijentSoket);
				sn.start();
				klijenti.add(sn);

			}
			

		} catch (IOException e) {

			System.out.println(e);
		}
	}

	public LinkedList<ServerNit> getKlijenti() {
		return klijenti;
	}

}
