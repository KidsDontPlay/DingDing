package mrriegel.dingding;

import io.netty.buffer.ByteBuf;
import mrriegel.dingding.ClientProxy.Area;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GuiMessage implements IMessage {

	int sound = 0;
	String show;
	Area area;
	double color;
	BlockPos pos;

	public GuiMessage() {
	}

	public GuiMessage(int sound, String show, Area area, double color, BlockPos pos) {
		super();
		this.sound = sound;
		this.show = show;
		this.area = area;
		this.color = color;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.sound = buf.readInt();
		this.color = buf.readDouble();
		this.show = ByteBufUtils.readUTF8String(buf);
		this.area = area.valueOf(ByteBufUtils.readUTF8String(buf));
		this.pos = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.sound);
		buf.writeDouble(this.color);
		ByteBufUtils.writeUTF8String(buf, this.show);
		ByteBufUtils.writeUTF8String(buf, this.area.toString());
		buf.writeLong(this.pos.toLong());
	}

	public static class Handler implements IMessageHandler<GuiMessage, IMessage> {

		@Override
		public IMessage onMessage(final GuiMessage message, final MessageContext ctx) {
			ctx.getServerHandler().playerEntity.getServerWorld().addScheduledTask(new Runnable() {

				@Override
				public void run() {
					TileDing tile = (TileDing) ctx.getServerHandler().playerEntity.getServerWorld().getTileEntity(message.pos);
					if (tile != null) {
						tile.area = message.area;
						tile.show = message.show;
						tile.sound = message.sound;
						tile.color = message.color;
						tile.markDirty();
					}
				}
			});
			return null;
		}

	}

}
