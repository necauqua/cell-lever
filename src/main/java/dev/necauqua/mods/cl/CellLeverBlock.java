package dev.necauqua.mods.cl;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Collection;

import static dev.necauqua.mods.cl.CellLever.ns;
import static net.minecraft.state.properties.BlockStateProperties.ATTACH_FACE;
import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public final class CellLeverBlock extends LeverBlock {

    // subclass to make false the default value
    private static final BooleanProperty PREV_INPUT = new BooleanProperty("prev_input") {
        private final ImmutableSet<Boolean> values = ImmutableSet.of(false, true);

        @Override
        public Collection<Boolean> getPossibleValues() {
            return values;
        }
    };

    public CellLeverBlock() {
        super(Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD));
        setRegistryName(ns("it"));
    }

    @Override
    public String getDescriptionId() {
        return CellLever.REPLACE_VANILLA_LEVER.get() ?
            "block.minecraft.lever" :
            "block.cell_lever:it.name";
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos pos2, boolean flag) {
        if (level.isClientSide) {
            return;
        }
        boolean hasSignal;
        if (state.getValue(ATTACH_FACE) == AttachFace.WALL) {
            Direction dir = state.getValue(HORIZONTAL_FACING);
            Direction left = dir.getCounterClockWise();
            Direction right = dir.getClockWise();
            hasSignal = level.getSignal(pos.relative(dir), dir) > 0 || level.getSignal(pos.relative(left), left) > 0 || level.getSignal(pos.relative(right), right) > 0;
        } else {
            Direction side = state.getValue(HORIZONTAL_FACING).getClockWise();
            Direction otherSide = side.getOpposite();
            hasSignal = level.getSignal(pos.relative(side), side) > 0 || level.getSignal(pos.relative(otherSide), otherSide) > 0;
        }

        if (state.getValue(PREV_INPUT) != hasSignal) {
            level.setBlock(pos, state = state.setValue(PREV_INPUT, hasSignal), 3);
            if (hasSignal) {
                pull(state, level, pos);
            }
        }
    }

    @Override
    public int getSignal(BlockState state, IBlockReader blockReader, BlockPos pos, Direction dir) {
        if (state.getValue(ATTACH_FACE) == AttachFace.WALL || state.getValue(HORIZONTAL_FACING).getAxis() != dir.getAxis()) {
            return 0;
        }
        return super.getSignal(state, blockReader, pos, dir);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PREV_INPUT);
    }
}

