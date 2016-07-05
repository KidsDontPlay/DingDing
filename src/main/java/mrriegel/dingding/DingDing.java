package mrriegel.dingding;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = DingDing.MODID, name = DingDing.MODNAME, version = DingDing.VERSION)
public class DingDing {
	public static final String MODID = "dingding";
	public static final String VERSION = "1.0.0";
	public static final String MODNAME = "DingDing";

	@Instance(DingDing.MODID)
	public static DingDing instance;

	public static final SimpleNetworkWrapper DISPATCHER = new SimpleNetworkWrapper(MODID);

	public static final Block ding = new BlockDing();

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

	public static EntityPlayer getPlayer(World world, String name) {
		for (EntityPlayer p : world.playerEntities)
			if (p.getDisplayNameString().equals(name))
				return p;
		return null;
	}

	public static Color getColor(double color) {
		return Color.getHSBColor((float) color, 1f, 1f);
	}

	public static void notifyPlayers(TileDing tile) {
		if (!tile.getWorld().isRemote)
			for (String s : tile.players) {
				EntityPlayer p = getPlayer(tile.getWorld(), s);
				if (p != null)
					DingDing.DISPATCHER.sendTo(new NotifyMessage(tile.sound, tile.show, tile.area, tile.color), (EntityPlayerMP) p);
			}
	}

}