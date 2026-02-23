package com.chinaex123.cobblestone_generator.block;

import com.chinaex123.cobblestone_generator.block.entity.CobblestoneGeneratorBlockEntity;
import com.chinaex123.cobblestone_generator.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

public class CobblestoneGeneratorBlock extends BaseEntityBlock {
    private final CobblestoneGeneratorTier tier;

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(properties -> new CobblestoneGeneratorBlock(properties, CobblestoneGeneratorTier.STONE));
    }

    public CobblestoneGeneratorBlock(Properties properties, CobblestoneGeneratorTier tier) {
        super(properties);
        this.tier = tier;
    }

    public CobblestoneGeneratorTier getTier() {
        return tier;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CobblestoneGeneratorBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof CobblestoneGeneratorBlockEntity generatorBE)) {
            return InteractionResult.SUCCESS;
        }

        // 提取物品逻辑...
        for (int i = 0; i < 9; i++) {
            ItemStack stack = generatorBE.getItemHandler().getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == Items.COBBLESTONE) {
                int extractAmount = Math.min(stack.getCount(), 64);
                ItemStack toGive = stack.copy();
                toGive.setCount(extractAmount);

                if (!player.getInventory().add(toGive)) {
                    player.drop(toGive, false);
                }

                stack.shrink(extractAmount);
                generatorBE.setChanged(); // 使用封装的同步方法

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return null;
        }

        return createTickerHelper(blockEntityType, ModBlockEntities.COBBLE_GENERATOR.get(),
                CobblestoneGeneratorBlockEntity::tick);
    }
}
