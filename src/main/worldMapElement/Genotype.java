package worldMapElement;

import java.util.Random;

import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static java.lang.Math.abs;

public class Genotype {
    private byte[] genotype;

    public Genotype(byte[] newGenotype) {
        genotype = newGenotype;
    }

    public Genotype reproduceGenotype(Animal a2) {
        Random generator = new Random();
        int cut1 = abs(generator.nextInt(30));
        int cut2;
        do {
            cut2 = abs(generator.nextInt(30));
        } while (cut2 == cut1);

        byte[] g1 = genotype;
        byte[] g2 = a2.getGenotype();
        byte[] gens = new byte[8];
        for (int i = 0; i < 32; i++) {
            if (i > min(cut1, cut2) && i <= max(cut1, cut2)) {
                gens[g2[i]]++;
            } else {
                gens[g1[i]]++;
            }
        }

        int count = 0;
        for (int i = 0; i < 8; i++) {
            if (gens[i] == 0) {
                gens[i]++;
                count++;
            }
        }

        int jump;
        for (int i = 0; i < count; i++) {
            jump = abs(generator.nextInt()) % 8;
            while (gens[jump % 8] <= 1) {
                jump++;
            }
            gens[jump % 8]--;
        }

        byte[] gen = new byte[32];
        int j = 0;
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < gens[i]; k++) {
                gen[j + k] = (byte) i;
            }
            j = j + gens[i];
        }
        return new Genotype(gen);
    }

    public byte getDirection(int i) {
        return genotype[i];
    }

    public byte[] getGenotype() {
        return genotype;
    }
}
