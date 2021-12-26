package dev.necauqua.mods.cl;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

public final class CellLeverBlockItem extends BlockItem {

    private static final UUID SPECIAL = new UUID(0x5a8c3be32aa44e91L, 0x8d7f4e7b0dc8223eL); // amadornes
    private static final Component SPECIAL_NAME = new TextComponent("Flippin' Lever");

    public CellLeverBlockItem() {
        super(CellLever.BLOCK, new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE));
        setRegistryName(CellLever.NAME);
    }

    @Override
    protected boolean allowdedIn(CreativeModeTab itemGroup) {
        return !CellLever.REPLACE_VANILLA_LEVER.get() && super.allowdedIn(itemGroup);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!CellLever.REPLACE_VANILLA_LEVER.get() || !(entity instanceof Player player)) {
            return;
        }
        // the slot that we receive in this method is compartment-relative aka useless,
        // so we have to do a full loop here - at least it'll only happen once if ever will, lul
        var inventory = player.getInventory();
        for (var i = 0; i < inventory.getContainerSize(); i++) {
            var s = inventory.getItem(i);
            if (s.getItem() == this) {
                inventory.setItem(i, new ItemStack(Items.LEVER, s.getCount(), s.getTag()));
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Component getName(ItemStack item) {
        var mc = Minecraft.getInstance();
        return mc.player == null || !mc.player.getGameProfile().getId().equals(SPECIAL) ?
            super.getName(item) :
            SPECIAL_NAME;
    }
}
