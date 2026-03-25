package com.chinaex123.cobblestone_generator.block.entity;

import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorBlock;
import com.chinaex123.cobblestone_generator.block.CobblestoneGeneratorTier;
import com.chinaex123.cobblestone_generator.config.CobblestoneGeneratorConfig;
import com.chinaex123.cobblestone_generator.network.NetworkHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseGeneratorBlockEntity extends BlockEntity {
    protected final CobblestoneGeneratorTier tier;
    protected int generateTimer = 0;
    protected int lastProcessedSlot = 0;
    
    private static final int TOTAL_SLOTS = 9;

    public final ItemStack[] items = new ItemStack[TOTAL_SLOTS];

    {
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            items[i] = ItemStack.EMPTY;
        }
    }

    private final ResourceHandler<@NotNull ItemResource> itemHandler = new ResourceHandler<>() {
        @Override
        public int size() {
            return TOTAL_SLOTS;
        }

        @Override
        public ItemResource getResource(int index) {
            if (index >= 0 && index < TOTAL_SLOTS) {
                ItemStack stack = items[index];
                if (!stack.isEmpty()) {
                    return ItemResource.of(stack);
                }
            }
            return ItemResource.EMPTY;
        }

        @Override
        public long getAmountAsLong(int index) {
            if (index >= 0 && index < TOTAL_SLOTS) {
                ItemStack stack = items[index];
                return stack.isEmpty() ? 0L : stack.getCount();
            }
            return 0L;
        }

        @Override
        public long getCapacityAsLong(int index, ItemResource resource) {
            return 64L;
        }

        @Override
        public int insert(int index, ItemResource resource, int amount, @NotNull TransactionContext transaction) {
            return 0;
        }

        @Override
        public int extract(int index, ItemResource resource, int amount, @NotNull TransactionContext transaction) {
            if (index >= 0 && index < TOTAL_SLOTS) {
                ItemStack current = items[index];

                if (current.isEmpty()) {
                    return 0;
                }

                if (!ItemStack.isSameItemSameComponents(current, resource.toStack(1))) {
                    return 0;
                }

                int toExtract = Math.min(amount, current.getCount());

                if (toExtract <= 0) {
                    return 0;
                }

                ItemStack remaining = current.copyWithCount(current.getCount() - toExtract);
                items[index] = remaining.isEmpty() ? ItemStack.EMPTY : remaining;

                setChanged();

                return toExtract;
            }

            return 0;
        }

        @Override
        public boolean isValid(int index, ItemResource resource) {
            return true;
        }
    };

    private final EnergyHandler energyHandler = new EnergyHandler() {
        @Override
        public int insert(int maxReceive, @NotNull TransactionContext transaction) {
            return 0;
        }

        @Override
        public int extract(int maxExtract, @NotNull TransactionContext transaction) {
            return 0;
        }

        @Override
        public long getAmountAsLong() {
            return 0;
        }

        @Override
        public long getCapacityAsLong() {
            return 0;
        }
    };

    public BaseGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = ((CobblestoneGeneratorBlock) state.getBlock()).getTier();
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        CompoundTag customData = input.read("custom_data", CompoundTag.CODEC).orElse(new CompoundTag());
        this.generateTimer = customData.getInt("GenerateTimer").orElse(0);
        this.lastProcessedSlot = customData.getInt("LastProcessedSlot").orElse(0);

        CompoundTag itemsTag = customData.getCompound("items").orElse(new CompoundTag());
        for (int i = 0; i < items.length; i++) {
            String key = "item" + i;
            int finalI = i;

            if (itemsTag.contains(key)) {
                var itemTagOpt = itemsTag.getCompound(key);
                itemTagOpt.flatMap(itemTag -> ItemStack.CODEC.parse(NbtOps.INSTANCE, itemTag)
                        .result()).ifPresent(stack -> items[finalI] = stack);
            } else {
                items[finalI] = ItemStack.EMPTY;
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        CompoundTag customData = new CompoundTag();
        customData.putInt("GenerateTimer", this.generateTimer);
        customData.putInt("LastProcessedSlot", this.lastProcessedSlot);

        CompoundTag itemsTag = new CompoundTag();
        for (int i = 0; i < items.length; i++) {
            int finalI = i;
            ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, items[i]).result().ifPresent(itemTag -> itemsTag.put("item" + finalI, itemTag));
        }
        customData.put("items", itemsTag);

        output.store("custom_data", CompoundTag.CODEC, customData);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        NetworkHelper.syncBlockEntity(level, worldPosition, getBlockState());
    }

    protected void handleBasicGeneration() {
        generateTimer++;

        double speedMultiplier = CobblestoneGeneratorConfig.getSpeedMultiplier();
        int effectiveGenerationTicks = (int) Math.max(1, tier.getGenerationTicks() / speedMultiplier);

        if (generateTimer >= effectiveGenerationTicks) {
            generateTimer = 0;

            int outputCount = Math.min(tier.getOutputCount(), 64);
            ItemStack cobblestone = new ItemStack(Items.COBBLESTONE, outputCount);

            boolean hasSpace = false;
            for (int i = 0; i < 9; i++) {
                ItemStack stack = items[i];
                if (stack.isEmpty() ||
                        (stack.getItem() == Items.COBBLESTONE &&
                                stack.getCount() + outputCount <= stack.getMaxStackSize())) {
                    hasSpace = true;
                    break;
                }
            }

            if (hasSpace) {
                for (int attempt = 0; attempt < 9; attempt++) {
                    int slotIndex = (lastProcessedSlot + attempt) % 9;
                    ItemStack stack = items[slotIndex];

                    if (stack.isEmpty()) {
                        items[slotIndex] = cobblestone.copy();
                        lastProcessedSlot = (slotIndex + 1) % 9;
                        setChanged();
                        break;
                    }
                    else if (stack.getItem() == Items.COBBLESTONE &&
                            stack.getCount() + outputCount <= stack.getMaxStackSize()) {
                        stack.grow(outputCount);
                        lastProcessedSlot = (slotIndex + 1) % 9;
                        setChanged();
                        break;
                    }
                }
            }
        }
    }

    protected void handleItemOutput() {
        Direction outputDirection = CobblestoneGeneratorConfig.getOutputDirection();
        BlockPos targetPos = worldPosition.relative(outputDirection);

        BlockEntity targetBlockEntity = null;
        if (level != null) {
            targetBlockEntity = level.getBlockEntity(targetPos);
        }

        if (targetBlockEntity != null) {
            var handler = level.getCapability(Capabilities.Item.BLOCK, targetPos, outputDirection.getOpposite());
            if (handler != null) {
                for (int slotAttempt = 0; slotAttempt < 9; slotAttempt++) {
                    int sourceSlot = (lastProcessedSlot + slotAttempt) % 9;
                    ItemStack stack = items[sourceSlot];

                    if (!stack.isEmpty() && stack.getItem() == Items.COBBLESTONE) {
                        int transferCount = Math.min(stack.getCount(), 64);
                        ItemStack toTransfer = stack.copy();
                        toTransfer.setCount(transferCount);

                        ItemStack remaining = toTransfer;
                        for (int targetSlot = 0; targetSlot < handler.size() && !remaining.isEmpty(); targetSlot++) {
                            ItemResource targetResource = handler.getResource(targetSlot);
                            if (targetResource.isEmpty() ||
                                    ItemStack.isSameItemSameComponents(remaining, targetResource.toStack(1))) {
                                try (Transaction transaction = Transaction.openRoot()) {
                                    long inserted = handler.insert(targetSlot, ItemResource.of(remaining), remaining.getCount(), transaction);
                                    if (inserted > 0) {
                                        remaining.shrink((int) inserted);
                                        transaction.commit();
                                    }
                                }
                            }
                        }

                        int actuallyTransferred = transferCount - remaining.getCount();
                        if (actuallyTransferred > 0) {
                            stack.shrink(actuallyTransferred);
                            setChanged();
                            lastProcessedSlot = (sourceSlot + 1) % 9;
                            break;
                        }
                    }
                }
            }
        }
    }

    public ResourceHandler<@NotNull ItemResource> getItemHandler() {
        return itemHandler;
    }

    @Nullable
    public EnergyHandler getEnergyHandler(@Nullable Direction side) {
        return energyHandler;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                ModBlockEntities.COBBLE_GENERATOR.get(),
                (be, side) -> {
                    if (side != Direction.UP) {
                        return be.getItemHandler();
                    }
                    return null;
                }
        );

        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                ModBlockEntities.SPECIAL_GENERATOR.get(),
                (be, side) -> {
                    if (side != Direction.UP) {
                        return be.getItemHandler();
                    }
                    return null;
                }
        );
    }
}
