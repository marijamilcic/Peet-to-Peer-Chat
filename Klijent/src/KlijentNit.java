import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class KlijentNit extends Thread {

	PrintStream izlazKlijentServer = null;
	BufferedReader ulazKlijentServer = null;
	int portK;

	Konvertovanje k = new Konvertovanje();

	Socket soketZaKonverziju = null;

	public KlijentNit(Socket soket2) {
		soketZaKonverziju = soket2;
	}

	public void run() {
		String broj;
		String odabranaKonverzija;
		
		
		
		try {
			ulazKlijentServer = new BufferedReader(new InputStreamReader(
					soketZaKonverziju.getInputStream()));
			izlazKlijentServer = new PrintStream(
					soketZaKonverziju.getOutputStream());
			

			while (true) {

				String linijaOdKlijenta= ulazKlijentServer.readLine();
				
				if (linijaOdKlijenta.startsWith("CONVERT")) {
				String [] poruka = linijaOdKlijenta.split("|");
				
				odabranaKonverzija = poruka[2];
				broj = poruka[1];

					if (odabranaKonverzija.contains("HexToDec")) {
						izlazKlijentServer.println(k.HexToDec(broj));
					}
					if (odabranaKonverzija.contains("OctToFour")) {
						izlazKlijentServer.println(k.OctToFour(broj));
					}
					if (odabranaKonverzija.contains("BinToDec")) {
						izlazKlijentServer.println(k.BinToDec(broj));
					}
					if (odabranaKonverzija.contains("FiveToSeven")) {
						izlazKlijentServer.println(k.FiveToSeven(broj));
					}
					if (odabranaKonverzija.contains("DecToHex")) {
						izlazKlijentServer.println(k.HexToDec(broj));
					}
					if (odabranaKonverzija.contains("FourToOct")) {
						izlazKlijentServer.println(k.HexToDec(broj));
					}
					if (odabranaKonverzija.contains("DecToBin")) {
						izlazKlijentServer.println(k.HexToDec(broj));
					}
					if (odabranaKonverzija.contains("SevenToFive")) {
						izlazKlijentServer.println(k.HexToDec(broj));
					}
						break;
				}
				izlazKlijentServer.println("Uneli ste pogresno!");

			
			}
	

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
