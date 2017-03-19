package core;

public abstract class MonteCarlo {
	private int pocetIteracii;
	public MonteCarlo() {
		pocetIteracii=0;
	}
	/**
	 * vykon� pokus dann�ho monte carla NEIKREMENTOVAT pocet iteracii
	 */
    abstract void vykonajPokus();
    public void vykonajReplikaciu(){
    	vykonajPokus();
    	pocetIteracii++;
    }
	public int getPocetIteracii() {
		return pocetIteracii;
	}
}
