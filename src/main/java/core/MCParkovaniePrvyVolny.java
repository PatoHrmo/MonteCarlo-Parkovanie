package core;

public class MCParkovaniePrvyVolny extends MCParkovanie {
	
	public MCParkovaniePrvyVolny(int pocetParkovacichMiest) {
		super(pocetParkovacichMiest);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void vykonajPokus() {
		for(int i = 0; i<parkovisko.length;i++) {
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
