package core;

public class MCParkovaniePoslednaTretina extends MCParkovanie {
	final int pocetPreskocenychMiest;
	public MCParkovaniePoslednaTretina(int pocetParkovacichMiest) {
		super(pocetParkovacichMiest);
		pocetPreskocenychMiest = (pocetParkovacichMiest*2)/3 + (((pocetParkovacichMiest*2)%3==0) ? 0 : 1);
	}

	@Override
	void vykonajPokus() {
		// FIXME naozaj -1?
		for(int i = pocetPreskocenychMiest; i<parkovisko.length;i++) {
			if(!parkovisko[i]) {
				// tu -1 nie je lebo hodnotime polohu miesta normalne teda od 1 .. n
				celkoveOhodnotenie+= parkovisko.length-i;
				this.uspesnosti[parkovisko.length-i]++;
				return;
			}
		}
		celkoveOhodnotenie+=pocetParkovacichMiest*2;
		this.uspesnosti[pocetParkovacichMiest*2-1]++;
	}

}
