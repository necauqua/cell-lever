package dev.necauqua.mods.cl;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

public final class CellLeverBlockItem extends BlockItem {

    private static final UUID SPECIAL = new UUID(0x5a8c3be32aa44e91L, 0x8d7f4e7b0dc8223eL); // amadornes
    private static final TextComponent SPECIAL_NAME = new StringTextComponent("Flippin' Lever");

    public CellLeverBlockItem() {
        super(CellLever.BLOCK, new Item.Properties().tab(ItemGroup.TAB_REDSTONE));
        setRegistryName(CellLever.NAME);
    }

    @Override
    protected boolean allowdedIn(ItemGroup itemGroup) {
        return !CellLever.REPLACE_VANILLA_LEVER.get() && super.allowdedIn(itemGroup);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!CellLever.REPLACE_VANILLA_LEVER.get() || !(entity instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) entity;
        // the slot that we receive in this method is compartment-relative aka useless,
        // so we have to do a full loop here - at least it'll only happen once if ever will, lul
        for (int i = 0; i < player.inventory.getContainerSize(); i++) {
            ItemStack s = player.inventory.getItem(i);
            if (s.getItem() == this) {
                player.inventory.setItem(i, new ItemStack(Items.LEVER, s.getCount(), s.getTag()));
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ITextComponent getName(ItemStack item) {
        Minecraft mc = Minecraft.getInstance();
        return mc.player == null || !mc.player.getGameProfile().getId().equals(SPECIAL) ?
            super.getName(item) :
            SPECIAL_NAME;
    }
}
