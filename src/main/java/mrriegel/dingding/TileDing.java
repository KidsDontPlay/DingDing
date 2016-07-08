package mrriegel.dingding;

import java.util.Set;

import mrriegel.dingding.ClientProxy.Area;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class TileDing extends TileEntity {

	Set<String> players = Sets.newHashSet();
	int sound = 0;
	String show = "";
	Area area = Area.TL;
	double color;
	boolean on;

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound syncData = new NBTTagCompound();
		this.writeToNBT(syncData);
		return new SPacketUpdateTileEntity(this.pos, 1, syncData);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		players = new Gson().fromJson(compound.getString("players"), new TypeToken<Set<String>>() {
		}.getType());
		sound = compound.getInteger("sound");
		show = compound.getString("show");
		if (compound.hasKey("area"))
			area = Area.valueOf(compound.getString("area"));
		color = compound.getDouble("color");
		on = compound.getBoolean("on");
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("players", new Gson().toJson(players));
		compound.setInteger("sound", sound);
		compound.setString("show", show);
		compound.setString("area", area.toString());
		compound.setDouble("color", color);
		compound.setBoolean("on", on);
		return compound;
	}

	public void notifyPlayers() {
		if (!getWorld().isRemote)
			for (String s : players) {
				EntityPlayer p = getPlayer(getWorld(), s);
				if (p != null)
					DingDing.DISPATCHER.sendTo(new NotifyMessage(sound, show, area, color), (EntityPlayerMP) p);
			}
	}

	private EntityPlayer getPlayer(World w, String name) {
		for (WorldServer world : w.getMinecraftServer().worldServers)
			for (EntityPlayer p : world.playerEntities)
				if (p.getDisplayNameString().equals(name))
					return p;
		return null;
	}

}
