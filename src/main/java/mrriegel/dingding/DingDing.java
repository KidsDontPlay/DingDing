package mrriegel.dingding;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = DingDing.MODID, name = DingDing.MODNAME, version = DingDing.VERSION)
public class DingDing {
	public static final String MODID = "dingding";
	public static final String VERSION = "1.0.0";
	public static final String MODNAME = "DingDing";

	@Instance(DingDing.MODID)
	public static DingDing instance;
	
	public static final Block ding=new BlockDing();

	@SidedProxy(clientSide = "mrriegel.dingding.ClientProxy", serverSide = "mrriegel.dingding.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}