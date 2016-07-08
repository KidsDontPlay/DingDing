package mrriegel.dingding;

import mrriegel.dingding.ClientProxy.TextElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy implements IGuiHandler {
	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.refreshConfig(event.getSuggestedConfigurationFile());
		GameRegistry.register(DingDing.ding);
		GameRegistry.register(new ItemBlock(DingDing.ding).setRegistryName(DingDing.ding.getRegistryName()));
		GameRegistry.addShapedRecipe(new ItemStack(DingDing.ding), "rrr", "rer", "rrr", 'r', Items.REDSTONE, 'e', Items.ENDER_PEARL);
		GameRegistry.registerTileEntity(TileDing.class, "tileDing");
	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(DingDing.instance, this);
		DingDing.DISPATCHER.registerMessage(GuiMessage.Handler.class, GuiMessage.class, 0, Side.SERVER);
		DingDing.DISPATCHER.registerMessage(NotifyMessage.Handler.class, NotifyMessage.class, 1, Side.CLIENT);
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GuiDing((TileDing) world.getTileEntity(new BlockPos(x, y, z)));
	}

	public void playSound(int n) {
	}

	public void addMessage(TextElement e) {
	}
}
