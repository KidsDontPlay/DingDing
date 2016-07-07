package mrriegel.dingding;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockDing extends BlockContainer {

	public BlockDing() {
		super(Material.ROCK);
		this.setHardness(2.0F);
		this.setCreativeTab(CreativeTabs.REDSTONE);
		this.setRegistryName("ding");
		this.setUnlocalizedName(getRegistryName().toString());
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileDing();
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		super.neighborChanged(state, worldIn, pos, blockIn);
		if (worldIn.isRemote)
			return;
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof TileDing) {
			TileDing tile = (TileDing) tileentity;
			boolean power = worldIn.isBlockPowered(pos);
			if (!tile.on && power) {
				tile.on = true;
				tile.notifyPlayers();
			} else if (!power)
				tile.on = false;
			tile.markDirty();
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (worldIn.getTileEntity(pos) instanceof TileDing && placer instanceof EntityPlayer) {
			((TileDing) worldIn.getTileEntity(pos)).players.add(((EntityPlayer) placer).getDisplayNameString());
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof TileDing) {
			playerIn.openGui(DingDing.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
			if (!worldIn.isRemote && ((TileDing) tileentity).players.add(playerIn.getDisplayNameString())) {
				playerIn.addChatMessage(new TextComponentString("Added "+playerIn.getDisplayNameString()));
			}
			return true;
		} else {
			return true;
		}
	}

}
