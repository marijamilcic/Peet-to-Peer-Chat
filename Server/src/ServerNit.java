import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.GregorianCalendar;
import java.util.LinkedList;


public class ServerNit extends Thread {

	private CentralServer centralServer;
	private Socket soketZaKomunikaciju;

	private String ipAdresa;
	private int portZaKonverziju;
	private int[] podrzavanaKonverzija = new int[4];
	int suma = 0;

	public ServerNit(CentralServer centralServer, Socket klijentSoket) {
		this.soketZaKomunikaciju = klijentSoket;
		this.centralServer = centralServer;
		this.ipAdresa = klijentSoket.getInetAddress().toString();
		this.portZaKonverziju = klijentSoket.getPort();
	}

	public void run() {

		

		try {

			BufferedReader ulazOdKlijenta = new BufferedReader(new InputStreamReader(
					soketZaKomunikaciju.getInputStream()));
			PrintStream izlazKaKlijentu = new PrintStream(
					soketZaKomunikaciju.getOutputStream());

			izlazKaKlijentu.println("WELCOME-");

			while (true) {
				
				String tekst;
				tekst = ulazOdKlijenta.readLine();
				System.out.println("Stigao zahtev!");
				System.out.println(tekst);
				String[] poruka = tekst.split("-");
				for (String string : poruka) {
					System.out.println(string + " ");
				}
				
				@SuppressWarnings("unused")
				String broj;
				String konverzija;
				
				if (poruka[0].startsWith("CONVERSIONS")) {
					System.out.println("Poruka: OK");
					String konverzije = poruka[1];
					
					if (konverzije.contains("HexToDec")) {
						podrzavanaKonverzija[0] = 1;
						System.out.println("HexToDec: OK");
					}
					
					if (konverzije.contains("OctToFour")) {
						podrzavanaKonverzija[1] = 1;
						System.out.println("OctToFour: OK");
					}
					
					if (konverzije.contains("BinToDec")) {
						podrzavanaKonverzija[2] = 1;
						System.out.println("BinToDec: OK");
					}
					
					if (konverzije.contains("FiveToSeven")) {
						podrzavanaKonverzija[3] = 1;
						System.out.println("FiveToSeven: OK");
					}
					
					
					izlazKaKlijentu.println("ASK-");
					System.out.println("Poruka: " + poruka[0]);
				} else if (poruka[0].startsWith("CONVERT")) {
					// OVO SE NIGDE NE KORISTI
					broj = poruka[1];
					konverzija = poruka[2];
					
					LinkedList<String> listaKlijenataKojiPodrzavaju = new LinkedList<String>();
					System.out.println("Trazim ip adresu.....");
					boolean pronasao = false;
					
					for (int i = 0; i < centralServer.getKlijenti().size(); i++) {
						System.out.println("Proveravam listu");
						System.out.println("Klijent " + i + " " + centralServer.getKlijenti().get(i).getIpAdresa());
						//if (centralServer.getKlijenti().get(i) != null) {
							System.out.println("Trazim:" + konverzija);
							int[] konverzijeDatogKonvertera = centralServer.getKlijenti().get(i).getPodrzavanaKonverzija();
							
							if (konverzija.equals("HexToDec") && konverzijeDatogKonvertera[0] == 1 || 
									konverzija.equals("DecToHex") && konverzijeDatogKonvertera[0] == 1 ||
									konverzija.equals("OctToFour") && konverzijeDatogKonvertera[1] == 1 ||
									konverzija.equals("FourToOct") && konverzijeDatogKonvertera[1] == 1 ||
									konverzija.equals("DecToBin") && konverzijeDatogKonvertera[2] == 1 ||
									konverzija.equals("BinToDec") && konverzijeDatogKonvertera[2] == 1 || 
									konverzija.equals("SevenToFive") && konverzijeDatogKonvertera[3] == 1 ||
									konverzija.equals("FiveToSeven") && konverzijeDatogKonvertera[3] == 1) {
								
								// smestamo u formatu ipadresa:port
								listaKlijenataKojiPodrzavaju.add(centralServer.getKlijenti().get(i).getIpAdresa() + ":" + centralServer.getKlijenti().get(i).getPortZaKonverziju());
								pronasao = true;
							}
							
						//}
					}
					
					if (!pronasao) {
						izlazKaKlijentu.println("Niko ne podrzava ovu konverziju");
					}
					
					String porukaZaKlijenta = "CONVERTERS-";
					
					for (int j = 0; j < listaKlijenataKojiPodrzavaju.size(); j++) {
						porukaZaKlijenta += listaKlijenataKojiPodrzavaju.get(j) + "##";
						System.out.println("Poslao sam klijente!");
					}
					izlazKaKlijentu.println(porukaZaKlijenta);
				} else if (poruka[0].equals("REPORT")) {
					String brojZaKonvertovanje = poruka[1];
					String odabranaKonverzija = poruka[2];
					String rezultatKonverzije = poruka[3];
					String IP = poruka[4];
					String uspesnost = poruka[5];
					if (uspesnost.equals("DA")) {
						suma +=1;
						System.out.println("Dosadasnji broj uspesnih konverzija je:" + suma);
					}
					
					GregorianCalendar vreme = new GregorianCalendar();
					int sat = vreme.get(GregorianCalendar.HOUR);
					int min = vreme.get(GregorianCalendar.MINUTE);
					int dan =  vreme.get(GregorianCalendar.DAY_OF_MONTH);
					int mesec = vreme.get(GregorianCalendar.MONTH) +1;
					int god = vreme.get(GregorianCalendar.YEAR);
					
					
					
					BufferedWriter fajlBaza = new BufferedWriter(new FileWriter("baza.txt", true));
					fajlBaza.write("izvestaj: Klijent cija je IPadresa:" + ipAdresa + "je broj:" + brojZaKonvertovanje + "konvertovao zahtevajuci:" 
					+ odabranaKonverzija + "a zeljeni rezultat je:" +rezultatKonverzije + ".Vreme i datum: "+sat+":"+min+ "**" + dan+"."+mesec+"."+god+"." );
					fajlBaza.newLine();
					fajlBaza.close();
					
					// u tekstualni fajl upisati podatke o konverziji
					
					
					
				} /*else {
					continue;
				}*/
				//String kraj = ulazOdKlijenta.readLine();
				if (tekst.equalsIgnoreCase("1")) {
					soketZaKomunikaciju.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getIpAdresa() {
		return ipAdresa;
	}
	
	public int[] getPodrzavanaKonverzija() {
		return podrzavanaKonverzija;
	}

	public int getPortZaKonverziju() {
		return portZaKonverziju;
	}
	
	
}
