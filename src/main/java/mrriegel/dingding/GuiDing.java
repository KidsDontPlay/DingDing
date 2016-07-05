package mrriegel.dingding;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiSlider;

import org.lwjgl.input.Keyboard;

public class GuiDing extends GuiScreen {
	private GuiTextField showTextField;
	private final TileDing tile;
	private GuiButton playBtn;
	private GuiButton soundBtn;
	private GuiButton areaBtn;

	private GuiSlider slide;

	public GuiDing(TileDing tile) {
		this.tile = tile;
	}

	@Override
	public void updateScreen() {
		showTextField.updateCursorCounter();
		soundBtn.displayString = "Sound " + tile.sound;
		areaBtn.displayString = tile.area.getName();
		slide.displayString = "Color";
		tile.color = slide.sliderValue;
		showTextField.setTextColor(DingDing.getColor(tile.color).getRGB());

	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(this.playBtn = new GuiButton(4, this.width / 2 - 33, 80, 30, 20, "Play"));
		this.buttonList.add(this.soundBtn = new GuiButton(5, this.width / 2 - 50 - 100 - 4, 80, 100, 20, ""));
		this.buttonList.add(this.areaBtn = new GuiButton(6, this.width / 2 - 50 - 100 - 4, 120, 70, 20, ""));
		this.buttonList.add(this.slide = new GuiSlider(0, this.width / 2 - 33, 120, 100, 20, "Color", "", 0, 360, 0, false, false));
		this.slide.sliderValue = tile.color;
		this.showTextField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, 50, 300, 20);
		this.showTextField.setMaxStringLength(2500);
		this.showTextField.setFocused(true);
		this.showTextField.setText(tile.show);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		DingDing.DISPATCHER.sendToServer(new GuiMessage(tile.sound, tile.show, tile.area, tile.color, tile.getPos()));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			switch (button.id) {
			case 0:
				tile.color = ((GuiSlider) button).sliderValue;
				break;
			case 4:
				DingDing.proxy.playSound(tile.sound);
				break;
			case 5:
				tile.sound++;
				tile.sound %= ClientProxy.sounds.size();
				break;
			case 6:
				tile.area = tile.area.next();
				break;
			default:
				break;
			}
			DingDing.DISPATCHER.sendToServer(new GuiMessage(tile.sound, tile.show, tile.area, tile.color, tile.getPos()));
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (showTextField.textboxKeyTyped(typedChar, keyCode)) {
			tile.show = showTextField.getText();
		} else
			super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.showTextField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.showTextField.drawTextBox();
		// this.drawString(fontRendererObj, "muselmann", this.width/2, 150,
		// tile.getColor().getRGB());

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}