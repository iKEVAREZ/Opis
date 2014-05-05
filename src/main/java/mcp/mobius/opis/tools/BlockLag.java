package mcp.mobius.opis.tools;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockLag extends BlockContainer {

    public BlockLag(int par1, Material par2Material) {
        super(par1, par2Material);
        this.setUnlocalizedName("Lag Generator");
    }	
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileLag();
	}

}
