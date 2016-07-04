package mrriegel.dingding;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

import org.lwjgl.input.Keyboard;

public class GuiDing extends GuiScreen {
	private GuiTextField commandTextField;
	private GuiTextField previousOutputTextField;
	private GuiButton doneBtn;
	private GuiButton cancelBtn;
	private GuiButton outputBtn;
	private GuiButton modeBtn;
	private GuiButton conditionalBtn;
	private GuiButton autoExecBtn;
	private boolean trackOutput;
	private boolean conditional;
	private boolean automatic;

	@Override
	public void updateScreen() {
		this.commandTextField.updateCursorCounter();
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.done", new Object[0])));
		this.buttonList.add(this.cancelBtn = new GuiButton(1, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel", new Object[0])));
		this.buttonList.add(this.outputBtn = new GuiButton(4, this.width / 2 + 150 - 20, 135, 20, 20, "O"));
		this.buttonList.add(this.modeBtn = new GuiButton(5, this.width / 2 - 50 - 100 - 4, 165, 100, 20, I18n.format("advMode.mode.sequence", new Object[0])));
		this.buttonList.add(this.conditionalBtn = new GuiButton(6, this.width / 2 - 50, 165, 100, 20, I18n.format("advMode.mode.unconditional", new Object[0])));
		this.buttonList.add(this.autoExecBtn = new GuiButton(7, this.width / 2 + 50 + 4, 165, 100, 20, I18n.format("advMode.mode.redstoneTriggered", new Object[0])));
		this.commandTextField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, 50, 300, 20);
		this.commandTextField.setMaxStringLength(32500);
		this.commandTextField.setFocused(true);
		this.previousOutputTextField = new GuiTextField(3, this.fontRendererObj, this.width / 2 - 150, 135, 276, 20);
		this.previousOutputTextField.setMaxStringLength(32500);
		this.previousOutputTextField.setEnabled(false);
		this.previousOutputTextField.setText("-");
		this.doneBtn.enabled = false;
		this.outputBtn.enabled = false;
		this.modeBtn.enabled = false;
		this.conditionalBtn.enabled = false;
		this.autoExecBtn.enabled = false;
	}

	public void updateGui() {
		this.doneBtn.enabled = true;
		this.outputBtn.enabled = true;
		this.modeBtn.enabled = true;
		this.conditionalBtn.enabled = true;
		this.autoExecBtn.enabled = true;
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.commandTextField.mouseClicked(mouseX, mouseY, mouseButton);
		this.previousOutputTextField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, I18n.format("advMode.setCommand", new Object[0]), this.width / 2, 20, 16777215);
		this.drawString(this.fontRendererObj, I18n.format("advMode.command", new Object[0]), this.width / 2 - 150, 37, 10526880);
		this.commandTextField.drawTextBox();
		int i = 75;
		int j = 0;
		this.drawString(this.fontRendererObj, I18n.format("advMode.nearestPlayer", new Object[0]), this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
		this.drawString(this.fontRendererObj, I18n.format("advMode.randomPlayer", new Object[0]), this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
		this.drawString(this.fontRendererObj, I18n.format("advMode.allPlayers", new Object[0]), this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
		this.drawString(this.fontRendererObj, I18n.format("advMode.allEntities", new Object[0]), this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
		this.drawString(this.fontRendererObj, "", this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);

		if (!this.previousOutputTextField.getText().isEmpty()) {
			i = i + j * this.fontRendererObj.FONT_HEIGHT + 1;
			this.drawString(this.fontRendererObj, I18n.format("advMode.previousOutput", new Object[0]), this.width / 2 - 150, i, 10526880);
			this.previousOutputTextField.drawTextBox();
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
