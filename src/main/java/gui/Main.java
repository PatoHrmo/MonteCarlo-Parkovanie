package gui;

import core.MCParkovanie;
import core.MCParkovaniePoslednaTretina;
import core.MCParkovaniePrvyVolny;
import core.RozmiestnovacParkoviska;

public class Main {

	public static void main(String[] args) {
		RozmiestnovacParkoviska rz; 
		MCParkovanie firstParking;
		MCParkovanie thirdParking;
		for(int i = 30; i<31;i++) {
			rz = new RozmiestnovacParkoviska(i);
			firstParking = new MCParkovaniePrvyVolny(i);
			thirdParking = new MCParkovaniePoslednaTretina(i);
			rz.pridajMonteCarlo(thirdParking);
			rz.pridajMonteCarlo(firstParking);
			rz.vykonajReplikácie(50000000);
			System.out.println("Parkovisko velkosti "+i+" :");
			System.out.println();
			System.out.println("	Strategia prvy volny "+firstParking.getCelkovuUspesnostParkovania());
			System.out.println("	Strategia dve tretiny preskocit "+thirdParking.getCelkovuUspesnostParkovania());
			System.out.println(System.lineSeparator()+System.lineSeparator());
		}
	}

}
