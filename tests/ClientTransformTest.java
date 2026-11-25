package levelztest;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import java.nio.file.*;
public class ClientTransformTest implements PreLaunchEntrypoint {
 public void onPreLaunch(){try{
  for(String s:new String[]{"net.levelz.screen.SkillInfoScreen","net.levelz.screen.SkillScreen","net.libz.util.DrawTabHelper","net.levelz.init.RenderInit","net.minecraft.class_1429","net.minecraft.class_1657","net.minecraft.class_2248","net.levelz.screen.SkillScreen$WidgetButtonPage","net.levelz.screen.SkillListScreen","com.goosker.levelzoverhaul.SatisfyingButtonsBridge"}){
   Class.forName(s,false,Thread.currentThread().getContextClassLoader());
   System.out.println("LEVELZ38 CLIENT TRANSFORM PASS: "+s);
  }
  Files.writeString(Path.of("CLIENT_PASS"),"All patched target classes transformed successfully");
  System.exit(0);
 }catch(Throwable t){t.printStackTrace();System.exit(1);}}
}
