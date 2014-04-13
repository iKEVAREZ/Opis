package mcp.mobius.opis.tools;

import mcp.mobius.opis.modOpis;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDebug extends BlockContainer {

    public BlockDebug(int par1, Material par2Material) {
        super(par1, par2Material);
        this.setUnlocalizedName("Debug Companion Cube");
    }	
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileDebug();
	}

}
