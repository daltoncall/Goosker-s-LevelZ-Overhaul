package levelztest;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.*;
import net.levelz.access.PlayerStatsManagerAccess;
import net.levelz.stats.Skill;
import com.goosker.levelzoverhaul.BreedingBonus;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import java.nio.file.*;

public class RuntimeTest implements ModInitializer {
 static int checks;
 static void check(boolean ok,String message){if(!ok)throw new AssertionError(message);checks++;System.out.println("LEVELZ38 PASS: "+message);}
 public void onInitialize(){ServerLifecycleEvents.SERVER_STARTED.register(server->{try{
  var w=server.method_30002();
  var p=new class_3222(server,w,new GameProfile(UUID.randomUUID(),"BreedTest"));
  p.field_13987 = new class_3244(server, new class_2535(class_2598.field_11942), p);
  w.method_18456().add(p);
  check(net.levelz.init.ConfigInit.CONFIG.farmingTwinChance==0,"native max-level twins disabled");
  check(net.levelz.init.ConfigInit.CONFIG.maxLevel==50,"max skill level remains 50");
  check(net.levelz.init.ConfigInit.CONFIG.tradeBonus==1.9D,"villager discount reaches 95 percent");
  check(net.levelz.init.ConfigInit.CONFIG.miningOreChance==0,"native ore duplication disabled");
  check(net.levelz.init.ConfigInit.CONFIG.breedingXPMultiplier==0,"native constant breeding XP disabled");
  var stats=((PlayerStatsManagerAccess)p).getPlayerStatsManager();
  stats.setSkillLevel(Skill.AGILITY,50);
  p.method_7344().method_35218(0);p.method_7322(1);
  check(p.method_7344().method_35219()==0,"agility 50 removes activity exhaustion");
  stats.setSkillLevel(Skill.AGILITY,25);p.method_7322(1);
  check(Math.abs(p.method_7344().method_35219()-0.5F)<0.0001F,"agility 25 halves activity exhaustion");
  stats.setSkillLevel(Skill.AGILITY,50);p.method_7344().method_35218(0);
  var damage=class_1657.class.getDeclaredMethod("method_6074",class_1282.class,float.class);damage.setAccessible(true);
  var hitSource=p.method_48923().method_48802(p);
  check(hitSource.method_5528()>0,"test damage source uses exhaustion");
  damage.invoke(p,hitSource,1.0F);
  check(p.method_7344().method_35219()>0,"damage still adds exhaustion at agility 50");
  stats.setSkillLevel(Skill.DEFENSE,50);p.method_14226();
  check(Math.abs(p.method_26825(class_5134.field_23725)-10.0D)<0.0001D,"vitality 50 grants 10 toughness");
  var pick=new class_1799(class_1802.field_8377);pick.method_7978(class_1893.field_9130,3);
  var diamond=class_2246.field_10442.method_9564();
  var pos=new class_2338(10,70,10);
  stats.setSkillLevel(Skill.MINING,50);
  for(int n=0;n<100;n++) {
   var drops=class_2248.method_9609(diamond,w,pos,null,p,pick);
   check(drops.stream().mapToInt(class_1799::method_7947).sum()==3,"ore max gives three diamonds with Fortune III");
  }
  check(class_1890.method_8225(class_1893.field_9130,pick)==3,"held Fortune enchantment remains intact");
  stats.setSkillLevel(Skill.MINING,25);
  check(class_2248.method_9609(diamond,w,pos,null,p,pick).stream().mapToInt(class_1799::method_7947).sum()==2,"mining 25 guarantees double drops");
  var silk=new class_1799(class_1802.field_8377);silk.method_7978(class_1893.field_9099,1);
  stats.setSkillLevel(Skill.MINING,50);
  var silkDrops=class_2248.method_9609(diamond,w,pos,null,p,silk);
  check(silkDrops.size()==1&&silkDrops.get(0).method_7909()==class_1802.field_8787&&silkDrops.get(0).method_7947()==1,"Silk Touch remains one ore block");
  stats.setSkillLevel(Skill.FARMING,50);
  var wheat=class_2246.field_10293.method_9564().method_11657(class_2302.field_10835,7);
  int produceBefore=produce(w);class_2246.field_10293.method_9576(w,pos,wheat,p);
  check(produce(w)-produceBefore==6,"husbandry 50 adds six wheat to the normal one (700 percent)");
  check(com.goosker.levelzoverhaul.BalanceValues.xpBonus(0)==0&&com.goosker.levelzoverhaul.BalanceValues.xpBonus(25)==0.5F&&com.goosker.levelzoverhaul.BalanceValues.xpBonus(50)==1,"smelting and breeding XP scale from 100 to 200 percent");
  int[] breedingXp = new int[3];
  int[] xpLevels={0,25,50};
  for(int i=0;i<3;i++) {
   stats.setSkillLevel(Skill.FARMING,xpLevels[i]);
   var a=new class_1430(class_1299.field_6085,w);var b=new class_1430(class_1299.field_6085,w);
   a.method_5808(2+i*3,70,2,0,0);a.method_6480(p);b.method_6480(p);
   a.method_6051().method_43052(12345L);
   int before=xp(w);a.method_49794(w,b,new class_1430(class_1299.field_6085,w));breedingXp[i]=xp(w)-before;
  }
  System.out.println("XP_BY_LEVEL "+java.util.Arrays.toString(breedingXp));
  check(breedingXp[0]>0&&breedingXp[2]==2*breedingXp[0],"actual breeding skill XP doubles from level 0 to 50");
  check(Math.abs(breedingXp[1]-breedingXp[0]*1.5F)<=1,"actual breeding XP scales at level 25");
  var recipe=new class_3861(new class_2960("levelztest:smelt"),"",class_7709.field_40244,class_1856.field_9017,new class_1799(class_1802.field_8861),1.0F,200);
  var recipes=new java.util.ArrayList<net.minecraft.class_1860<?>>(w.method_8433().method_8126());recipes.add(recipe);w.method_8433().method_20702(recipes);
  for(int level:new int[]{0,25,50}) {
   stats.setSkillLevel(Skill.SMITHING,level);
   var furnace=new class_3866(pos,class_2246.field_10181.method_9564());furnace.method_31662(w);
   for(int n=0;n<10;n++)furnace.method_7662(recipe);
   int before=p.field_7495;furnace.method_17763(p);
   check(p.field_7495-before==level/5,"actual smithing extra XP for 10 cooked items at level "+level);
  }
  for(int level:new int[]{0,1,5,9,10,11,19,20,25,30,40,49,50}){
   ((PlayerStatsManagerAccess)p).getPlayerStatsManager().setSkillLevel(Skill.FARMING,level);
   for(int n=0;n<8;n++){
    var a=new class_1430(class_1299.field_6085,w);var b=new class_1430(class_1299.field_6085,w);
    a.method_5808(0,70,0,0,0);b.method_5808(1,70,0,0,0);a.method_6480(p);b.method_6480(p);
    int before=count(w);a.method_24650(w,b);int babies=count(w)-before;
    int min=1+level/10,max=min+(level%10==0?0:1);
    check(babies>=min&&babies<=max,"level "+level+" produced "+babies+" babies ("+min+".."+max+")");
   }
  }
  var a=new class_1430(class_1299.field_6085,w);var b=new class_1430(class_1299.field_6085,w);
  int before=count(w);a.method_24650(w,b);check(count(w)-before==1,"unattributed breeding has no extras");
  for(int level=0;level<=50;level++)for(int roll=0;roll<100;roll++){
   int count=BreedingBonus.extraCount(level,roll/100f);
   check(count==level/10+(roll<(level%10)*10?1:0),"scaling "+level+" roll "+roll);
  }
  check(BreedingBonus.display(25).equals("50% x3 Spawn"),"level 25 display");
  check(BreedingBonus.display(50).equals("100% x5 Spawn"),"level 50 display");
  Files.writeString(Path.of("TEST_PASS"),checks+" runtime checks passed");
 }catch(Throwable t){t.printStackTrace();try{Files.writeString(Path.of("TEST_FAIL"),t.toString());}catch(Exception ignored){}}finally{server.method_3747(false);}});}
 static int xp(class_3218 w)throws Exception {
  int n=0;var map=net.levelz.entity.LevelExperienceOrbEntity.class.getDeclaredMethod("getClumpedMap");map.setAccessible(true);
  for(class_1297 e:w.method_27909())if(e instanceof net.levelz.entity.LevelExperienceOrbEntity orb){var values=(java.util.Map<Integer,Integer>)map.invoke(orb);for(var e2:values.entrySet())n+=e2.getKey()*e2.getValue();}
  return n;
 }
 static int produce(class_3218 w){int n=0;for(class_1297 e:w.method_27909())if(e instanceof class_1542 item&&item.method_6983().method_7909()==class_1802.field_8861)n+=item.method_6983().method_7947();return n;}
 static int count(class_3218 w){int n=0;for(class_1297 e:w.method_27909())if(e instanceof class_1430&&((class_1430)e).method_6109())n++;return n;}
}
