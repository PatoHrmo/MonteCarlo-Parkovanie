package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RozmiestnovacParkoviska {
	List<MCParkovanie> monteCarla;
	boolean[] parkovisko;
	Random[] generatoryInt;
	Random generatorPoctuObsadenychMiest;
	List<Integer> indexyVolnychMiest;
	int[] poctyObsadenychMiest;

	public RozmiestnovacParkoviska(int pocetMiest) {
		parkovisko = new boolean[pocetMiest];
		Random genSeedov = new Random();
		generatorPoctuObsadenychMiest = new Random(genSeedov.nextLong());
		generatoryInt = new Random[pocetMiest];
		indexyVolnychMiest = new ArrayList<>();
		monteCarla = new ArrayList<>();
		for (int i = 0; i < generatoryInt.length; i++) {
			generatoryInt[i] = new Random(genSeedov.nextLong());
		}
		poctyObsadenychMiest = new int[pocetMiest];
	}

	private void rozmiestniParkovisko() {
		indexyVolnychMiest.clear();
		for (int i = 0; i < parkovisko.length; i++) {
			parkovisko[i] = false;
			indexyVolnychMiest.add(i);
		}
		int pocetObsadenychMiest = getPocetObsadenychMiest();

		for (int i = 0; i < pocetObsadenychMiest; i++) {
			int pocetVolnychMiest = indexyVolnychMiest.size();
			int indexObsadenehoMiesta = indexyVolnychMiest
					.remove(generatoryInt[pocetVolnychMiest - 1].nextInt(pocetVolnychMiest));
			parkovisko[indexObsadenehoMiesta] = true;
			poctyObsadenychMiest[indexObsadenehoMiesta]++;
		}
		nastavParkoviskoMonteCarlam();
	}

	public void vykonajReplikácie(int pocetPokusov) {
		for (int i = 0; i < pocetPokusov; i++) {
			rozmiestniParkovisko();
			for(MCParkovanie mc : monteCarla) {
				mc.vykonajReplikaciu();
			}
		}
	}

	private int getPocetObsadenychMiest() {
		int generovane = this.generatorPoctuObsadenychMiest.nextInt(parkovisko.length) + 1;
		return generovane;
	}

	public void pridajMonteCarlo(MCParkovanie mc) {
		monteCarla.add(mc);
	}

	private void nastavParkoviskoMonteCarlam() {
		for (MCParkovanie mc : monteCarla) {
			mc.nastavParkovisko(parkovisko.clone());
		}
	}

	public void vypisPoctyObMiest() {
		for (int i = 0; i < poctyObsadenychMiest.length; i++) {
			System.out.println((i + 1) + "aut bolo na parkovisku" + poctyObsadenychMiest[i]);
		}
	}

	public int getPocetHotovychReplikacii() {
		return monteCarla.get(0).getPocetIteracii();
	}
}
