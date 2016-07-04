package mrriegel.dingding;

import java.awt.event.TextEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ClientProxy extends CommonProxy {

	public static Minecraft mc;
	public static KeyBinding kb;
	public static List<SoundEvent> sounds;
	public static Set<TextElement> texts;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(DingDing.ding), 0, new ModelResourceLocation(DingDing.ding.getRegistryName(), "inventory"));
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(this);
		mc = Minecraft.getMinecraft();
		kb = new KeyBinding(DingDing.MODID + ".keybinding.openGUI", Keyboard.KEY_B, DingDing.MODID);
		ClientRegistry.registerKeyBinding(kb);
		sounds = Lists.newArrayList();
		texts = Sets.newHashSet();
		for (int i = 1; i < 8; i++) {
			ResourceLocation r = new ResourceLocation("dingding:ding0" + i);
			GameRegistry.register(new SoundEvent(r), r);
			sounds.add(SoundEvent.REGISTRY.getObject(r));
		}
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	@Override
	public void playSound(int n) {
		super.playSound(n);
		// mc.thePlayer.playSound(sounds.get(new
		// Random().nextInt(n%sounds.size())), .9f, 1);
		mc.thePlayer.playSound(sounds.get(new Random().nextInt(sounds.size())), .9f, 1);
	}

	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent event) {
		if (kb.isPressed()) {

			// mc.thePlayer.openGui(instance, 0, mc.theWorld, 0, 0, 0);
		}
	}

	@SubscribeEvent
	public void render(RenderGameOverlayEvent.Pre event) {
		if (event.getType() == ElementType.TEXT && mc.theWorld.getTotalWorldTime() / 10 % 2 == 0) {
			for (TextElement el : texts) {
				int diff = 15;
				int color = 0x5EA151;
				ScaledResolution res = event.getResolution();
				switch (el.area) {
				case BL:
					mc.fontRendererObj.drawString(el.text, diff, res.getScaledHeight() - diff - mc.fontRendererObj.FONT_HEIGHT, color, true);
					break;
				case BR:
					mc.fontRendererObj.drawString(el.text, res.getScaledWidth() - mc.fontRendererObj.getStringWidth(el.text) - diff, res.getScaledHeight() - diff - mc.fontRendererObj.FONT_HEIGHT, color, true);
					break;
				case TL:
					mc.fontRendererObj.drawString(el.text, diff, diff, color, true);
					break;
				case TR:
					mc.fontRendererObj.drawString(el.text, res.getScaledWidth() - mc.fontRendererObj.getStringWidth(el.text) - diff, diff, color, true);
					break;
				case CT:
					mc.fontRendererObj.drawString(el.text, (res.getScaledWidth() - mc.fontRendererObj.getStringWidth(el.text)) / 2, (res.getScaledHeight() - diff - mc.fontRendererObj.FONT_HEIGHT) / 2, color, true);
					break;

				}

			}

		}
	}

	public static void add(TextElement k){
		removeDouble(k.area);
		texts.add(k);
	}
	
	static void removeDouble(Area a) {
		Iterator<TextElement> it = texts.iterator();
		while (it.hasNext()) {
			TextElement el = it.next();
			if (el.area == a)
				it.remove();
		}
	}

	@SubscribeEvent
	public void fadeOut(LivingUpdateEvent e) {
		if (e.getEntity().worldObj.isRemote && e.getEntityLiving() instanceof EntityPlayer) {
			Iterator<TextElement> it = texts.iterator();
			while (it.hasNext()) {
				TextElement el = it.next();
				el.cool--;
				if (el.cool <= 0)
					it.remove();
			}
		}
	}

	static enum Area {
		TL("Top Left"), TR("Top Right"), BR("Bottom Right"), BL("Bottom Left"), CT("Center");
		String name;

		Area(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public Area next() {
			return values()[(this.ordinal() + 1) % values().length];
		}
	}

	static class TextElement {
		public String text;
		public int cool;
		public int id;
		public Area area;

		public TextElement(String text, int cool, Area area) {
			super();
			this.text = text;
			this.cool = cool;
			this.area = area;
			this.id = new Random().nextInt();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((area == null) ? 0 : area.hashCode());
			result = prime * result + id;
			result = prime * result + ((text == null) ? 0 : text.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TextElement other = (TextElement) obj;
			if (area != other.area)
				return false;
			if (id != other.id)
				return false;
			if (text == null) {
				if (other.text != null)
					return false;
			} else if (!text.equals(other.text))
				return false;
			return true;
		}

	}
}
