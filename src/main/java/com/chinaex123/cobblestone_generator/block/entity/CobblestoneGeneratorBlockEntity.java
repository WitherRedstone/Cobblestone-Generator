package com.chinaex123.cobblestone_generator.block.entity;

import com.chinaex123.cobblestone_generator.network.NetworkHelper;
import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorBlock;
import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorTier;

public class CobblestoneGeneratorBlockEntity extends BlockEntity {
    private final CobblestoneGeneratorTier tier;
    private int generateTimer = 0;
    private int lastProcessedSlot = 0;

    private final ItemStackHandler itemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }
    };

    public CobblestoneGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COBBLE_GENERATOR.get(), pos, state);
        this.tier = ((CobblestoneGeneratorBlock) state.getBlock()).getTier();
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.generateTimer = nbt.getInt("GenerateTimer");
        this.lastProcessedSlot = nbt.getInt("LastProcessedSlot");
        itemHandler.deserializeNBT(registries, nbt.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.putInt("GenerateTimer", this.generateTimer);
        nbt.putInt("LastProcessedSlot", this.lastProcessedSlot);
        nbt.put("Inventory", itemHandler.serializeNBT(registries));
    }

    @Override
    public void setChanged() {
        super.setChanged();
        // 使用工具类进行同步
        NetworkHelper.syncBlockEntity(level, worldPosition, getBlockState());
    }

    private void triggerSync() {
        if (NetworkHelper.shouldSync(level)) {
            NetworkHelper.syncBlockEntity(level, worldPosition, getBlockState());
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CobblestoneGeneratorBlockEntity blockEntity) {
        if (level.isClientSide) return;

        blockEntity.generateTimer++;

        if (blockEntity.generateTimer >= blockEntity.tier.getGenerationTicks()) {
            blockEntity.generateTimer = 0;

            int outputAmount = Math.min(blockEntity.tier.getOutputAmount(), 64);
            ItemStack cobblestone = new ItemStack(Items.COBBLESTONE, outputAmount);

            // 生成逻辑...
            for (int attempt = 0; attempt < 9; attempt++) {
                int slotIndex = (blockEntity.lastProcessedSlot + attempt) % 9;
                ItemStack stack = blockEntity.itemHandler.getStackInSlot(slotIndex);

                if (stack.isEmpty()) {
                    blockEntity.itemHandler.setStackInSlot(slotIndex, cobblestone.copy());
                    blockEntity.lastProcessedSlot = (slotIndex + 1) % 9;
                    blockEntity.setChanged();
                    break;
                } else if (stack.getItem() == Items.COBBLESTONE &&
                        stack.getCount() + outputAmount <= stack.getMaxStackSize()) {
                    stack.grow(outputAmount);
                    blockEntity.lastProcessedSlot = (slotIndex + 1) % 9;
                    blockEntity.setChanged();
                    break;
                }
            }
        }

        // 使用配置的输出方向
        Direction outputDirection = CobblestoneGeneratorConfig.getOutputDirection();
        BlockPos targetPos = pos.relative(outputDirection);

        var targetBlockEntity = level.getBlockEntity(targetPos);
        if (targetBlockEntity != null) {
            var handler = level.getCapability(Capabilities.ItemHandler.BLOCK, targetPos, outputDirection.getOpposite());
            if (handler != null) {
                // 输出逻辑
                for (int slotAttempt = 0; slotAttempt < 9; slotAttempt++) {
                    int sourceSlot = (blockEntity.lastProcessedSlot + slotAttempt) % 9;
                    ItemStack stack = blockEntity.itemHandler.getStackInSlot(sourceSlot);

                    if (!stack.isEmpty() && stack.getItem() == Items.COBBLESTONE) {
                        int transferAmount = Math.min(stack.getCount(), 64);
                        ItemStack toTransfer = stack.copy();
                        toTransfer.setCount(transferAmount);

                        ItemStack remaining = toTransfer;
                        for (int targetSlot = 0; targetSlot < handler.getSlots() && !remaining.isEmpty(); targetSlot++) {
                            if (handler.isItemValid(targetSlot, remaining)) {
                                remaining = handler.insertItem(targetSlot, remaining, false);
                            }
                        }

                        int actuallyTransferred = transferAmount - remaining.getCount();
                        if (actuallyTransferred > 0) {
                            stack.shrink(actuallyTransferred);
                            blockEntity.setChanged();
                            blockEntity.lastProcessedSlot = (sourceSlot + 1) % 9;
                            break;
                        }
                    }
                }
            }
        }
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.COBBLE_GENERATOR.get(),
                (be, side) -> {
                    if (side != Direction.UP) {
                        return be.getItemHandler();
                    }
                    return null;
                }
        );
    }
}
