import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class KlijentServertest extends Thread {

	Socket soketZaKomunikaciju = null;

	private int portCS;
	private int portZaKonverziju;
	KlijentNit nitZaKonverziju;

	public void inicijalizuj(int portCS, int portZaKonverziju) {
//		try {
			this.portCS = portCS;
			this.portZaKonverziju = portZaKonverziju;
//			new Thread(new KlijentServertest()).start();
			this.start();

//			soketZaKomunikaciju.close();

//		}

//		catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}

	public void run() {

		try {
			soketZaKomunikaciju = new Socket("localhost", portCS);
			BufferedReader ulazKonzola = new BufferedReader(
					new InputStreamReader(System.in));
			PrintStream izlazKaServeru = new PrintStream(
					soketZaKomunikaciju.getOutputStream());
			BufferedReader ulazOdServera = new BufferedReader(
					new InputStreamReader(soketZaKomunikaciju.getInputStream()));

			// prijavljivanje na server da klijent moze da bude server
			Socket soket1 = null;
			// inicijalizujemo serverski soket koji osluskuje zahteve za
			// konverzijom
			nitZaKonverziju = new KlijentNit(new Socket());

			// kako da umesto stringa ubacim ip adresu ?
			String porukaOdServera;

			// broj koji je korisnik uneo da zeli da konvertuje
			String brojZaKonvertovanje = "";

			// tip konverzije koju je korisnik odabrao
			String odabranaKonverzija = "";

			while ((porukaOdServera = ulazOdServera.readLine()) != null) {
				String[] delovi = porukaOdServera.split("|");

				String akcija = delovi[0];

				if (akcija.equals("WELCOME")) {
					boolean ispravno = false;
					String porukaServeru = "SUPPORTED_CONVERSIONS|";
					while (!ispravno) {
						System.out
								.println("Unesite konverzije koje podrzavate razdvojene zapetama u obliku: HexToDec,OctToFour,BinToDec,FiveToSeven ili 0 ako ne vrsite konverziju");

						String podrzaneKonverzije = ulazKonzola.readLine();

						String[] unos = podrzaneKonverzije.split(",");

						// diskutabilna provera
						if (unos.equals("HexToDec") || unos.equals("OctToFour")
								|| unos.equals("BinToDec")
								|| unos.equals("FiveToSeven")
								|| unos.equals("0")) {

							porukaServeru += "|" + unos + "|"
									+ portZaKonverziju;
							ispravno = true;
						} else {
							System.out
									.println("Greska pri unosu, molim vas unesite opet:");
							
						}
					}

					// na kraju saljemo i port za konverziju

					izlazKaServeru.println(porukaServeru);
				} else if (akcija.equals("ASK")) {
					System.out
							.println("Unesite broj koje zelite da konvertujete");

					brojZaKonvertovanje = ulazKonzola.readLine();
					String regex = "[0-9a-f]";
					boolean dobarBroj = brojZaKonvertovanje.matches(regex);

					if (dobarBroj) {
						System.out
								.println("Unesite naziv konverzije (podrzane su: HexToDec,DecToHex,OctToFour,FourToOct,BinToDec,DecToBin,FiveToSeven, SevenToFive)");

						odabranaKonverzija = ulazKonzola.readLine();
						String[] conv = odabranaKonverzija.split(",");
						if (!(conv.equals("HexToDec")
								|| conv.equals("OctToFour")
								|| conv.equals("BinToDec")
								|| conv.equals("FiveToSeven")
								|| conv.equals("DecToHex")
								|| conv.equals("FourToOct")
								|| conv.equals("DecToBin") || conv
									.equals("SevenToFive"))) {
							System.out
									.println("Greska, molimo vas da unesete ponovo!");
						}

					} else {
						System.out
								.println("Greska, molimo vas da unesete ponovo!");
					}

					izlazKaServeru.println("CONVERT|" + brojZaKonvertovanje
							+ "|" + odabranaKonverzija);
				} else if (akcija.equals("CONVERTERS")) {
					String konverteri = delovi[1];

					String[] ipAdresa = konverteri.split("##");
					String[] ipIPort = ipAdresa[0].split(":");

					System.out.println("Pozivamo konverter na adresi "
							+ ipIPort
							+ ". Molimo sacekajte rezultat konverzije...");

					String konvertovaniBroj = kontaktirajKonverter(ipIPort[0],
							Integer.parseInt(ipIPort[1]), brojZaKonvertovanje,
							odabranaKonverzija);

					if (konvertovaniBroj == null) {
						System.out
								.println("Dogodila se greska, molimo pokusajte ponovo");
					} else {
						System.out.println("Kada se broj "
								+ brojZaKonvertovanje + " konvertuje preko "
								+ odabranaKonverzija
								+ " konverzije dobije se vrednost "
								+ konvertovaniBroj);
						System.out
								.println("Unesite da li je konverzacija bila uspesna: (DA, ako jeste, NE u suprotnom");
						String uspesnost = ulazKonzola.readLine();
						if (uspesnost.equals("NE")) {
							System.out.println("zao mi je :(");
						}

						// saljemo informaciju serveru o uspesnoj konverziji
						izlazKaServeru.println("REPORT|" + brojZaKonvertovanje
								+ "|" + odabranaKonverzija + "|"
								+ konvertovaniBroj + "|" + ipIPort + "|"
								+ uspesnost);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String kontaktirajKonverter(String ip, int port,
			String brojZaKonvertovanje, String odabranaKonverzija) {
		try {
			Socket soketZaKomunikaciju = new Socket(ip, port);
			PrintStream izlazKaKonvertoru = new PrintStream(
					soketZaKomunikaciju.getOutputStream());
			BufferedReader ulazOdServera = new BufferedReader(
					new InputStreamReader(soketZaKomunikaciju.getInputStream()));

			izlazKaKonvertoru.println("CONVERT|" + brojZaKonvertovanje + "|"
					+ odabranaKonverzija);
			String odgovorKonvertora = ulazOdServera.readLine();

			if (odgovorKonvertora != null) {
				String[] deloviPoruke = odgovorKonvertora.split("|");

				if (deloviPoruke[0].equals("CONVERTED_VALUE")) {
					return deloviPoruke[3];

				}
			}
//			soketZaKomunikaciju.close();
			return null;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
