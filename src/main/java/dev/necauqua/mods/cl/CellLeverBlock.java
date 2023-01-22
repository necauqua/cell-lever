package dev.necauqua.mods.cl;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.HitResult;

import java.util.Collection;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.ATTACH_FACE;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

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
    }

    @Override
    public String getDescriptionId() {
        return CellLever.REPLACE_VANILLA_LEVER.get() ?
            "block.minecraft.lever" :
            "block.cell_lever:it.name";
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return new ItemStack(CellLever.REPLACE_VANILLA_LEVER.get() ? Blocks.LEVER : this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean flag) {
        if (level.isClientSide) {
            return;
        }
        boolean hasSignal;
        if (state.getValue(ATTACH_FACE) == AttachFace.WALL) {
            var dir = state.getValue(HORIZONTAL_FACING);
            var left = dir.getCounterClockWise();
            var right = dir.getClockWise();
            hasSignal = level.getSignal(pos.relative(dir), dir) > 0 || level.getSignal(pos.relative(left), left) > 0 || level.getSignal(pos.relative(right), right) > 0;
        } else {
            var side = state.getValue(HORIZONTAL_FACING).getClockWise();
            var otherSide = side.getOpposite();
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
    public int getSignal(BlockState state, BlockGetter blockReader, BlockPos pos, Direction dir) {
        if (state.getValue(ATTACH_FACE) == AttachFace.WALL || state.getValue(HORIZONTAL_FACING).getAxis() != dir.getAxis()) {
            return 0;
        }
        return super.getSignal(state, blockReader, pos, dir);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(PREV_INPUT));
    }
}

