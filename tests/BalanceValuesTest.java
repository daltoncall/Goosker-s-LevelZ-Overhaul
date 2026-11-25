import com.goosker.levelzoverhaul.BalanceValues;
public class BalanceValuesTest {
 public static void main(String[] args) {
  for(int level=0;level<=50;level++) {
   int sum=0;
   for(int roll=0;roll<100;roll++)sum+=BalanceValues.oreMultiplier(level,roll/100.0F);
   if(sum!=100+level*4)throw new AssertionError("ore mean at "+level+": "+sum);
  }
  if(BalanceValues.toughness(50)!=10.0||BalanceValues.exhaustionFactor(50)!=0
   ||BalanceValues.tradeDiscount(50)!=95.0||BalanceValues.xpBonus(50)!=1.0F
   ||BalanceValues.produceExtras(50)!=6.0F)throw new AssertionError("maximum mismatch");
  if(!BalanceValues.oreDisplay(50).equals("100% x3 Ore Drops"))throw new AssertionError();
  if(!BalanceValues.oreDisplay(25).equals("100% x2 Ore Drops"))throw new AssertionError();
  if(!BalanceValues.oreDisplay(10).equals("40% x2 Ore Drops"))throw new AssertionError();
  System.out.println("5100 ore outcomes and all balance maximums passed");
 }
}
