package core;

public abstract class MCParkovanie extends MonteCarlo {
	int pocetParkovacichMiest;
	int celkoveOhodnotenie;
	boolean parkovisko[];
	int[] uspesnosti;
	public MCParkovanie(int pocetParkovacichMiest) {
		super();
		uspesnosti = new int[pocetParkovacichMiest*2];
		this.pocetParkovacichMiest = pocetParkovacichMiest;
		this.celkoveOhodnotenie = 0;
		parkovisko = new boolean[pocetParkovacichMiest];
	}
	abstract void vykonajPokus();
	public double getCelkovuUspesnostParkovania() {
		return (double)celkoveOhodnotenie/getPocetIteracii();
	}
	/**
	 * nastav� parkovisko pre nasledovn� pokus
	 */
	void nastavParkovisko(boolean[] parkovisko) {
		this.parkovisko = parkovisko;
	}
	/**
	 * vr�ti po�etnosti �spe�nosti potrebn� pre histogram
	 */
	public int[] getUspesnosti() {
		return uspesnosti;
	}
	public int getCelkoveOhodnotenie() {
		return celkoveOhodnotenie;
	}
	public int getVelkostParkoviska(){
		return pocetParkovacichMiest;
	}

}
