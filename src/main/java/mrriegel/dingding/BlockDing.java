package mrriegel.dingding;

import java.util.List;

import mrriegel.dingding.ClientProxy.Area;
import mrriegel.dingding.ClientProxy.TextElement;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDing extends BlockContainer {

	public BlockDing() {
		super(Material.ROCK);
		this.setHardness(2.0F);
		this.setCreativeTab(CreativeTabs.REDSTONE);
		this.setRegistryName("ding");
		this.setUnlocalizedName(getRegistryName().toString());
	}

	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileDing();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (worldIn.getTileEntity(pos) instanceof TileDing && placer instanceof EntityPlayer) {
			((TileDing) worldIn.getTileEntity(pos)).players.add(((EntityPlayer) placer).getDisplayNameString());
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.getTileEntity(pos) instanceof TileDing && worldIn.isRemote) {
			DingDing.proxy.playSound(3);
			ClientProxy.add(new TextElement(new Object() + "", 100, Area.values()[worldIn.rand.nextInt(Area.values().length)]));
		}
		return true;
	}

}
