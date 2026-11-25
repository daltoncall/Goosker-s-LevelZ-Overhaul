import com.goosker.levelzoverhaul.BreedingBonus;
public class BreedingBonusTest {
    public static void main(String[] args) {
        int checks = 0;
        for (int level = 0; level <= 50; level++) {
            int sum = 0;
            for (int roll = 0; roll < 100; roll++) {
                int count = BreedingBonus.extraCount(level, roll / 100.0F);
                if (count != level / 10 + (roll < (level % 10) * 10 ? 1 : 0))
                    throw new AssertionError("level " + level + ", roll " + roll);
                sum += count;
                checks++;
            }
            if (sum != level * 10) throw new AssertionError("expected offspring rate");
        }
        if (!BreedingBonus.display(25).equals("50% x3 Spawn")) throw new AssertionError();
        if (!BreedingBonus.display(50).equals("100% x5 Spawn")) throw new AssertionError();
        if (BreedingBonus.guaranteed(-1) != 0 || BreedingBonus.guaranteed(100) != 5) throw new AssertionError();
        System.out.println(checks + " offspring outcome checks passed, plus averages, display, and clamps");
    }
}
