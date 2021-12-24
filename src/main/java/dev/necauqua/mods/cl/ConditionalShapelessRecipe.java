package dev.necauqua.mods.cl;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import static dev.necauqua.mods.cl.CellLever.ns;

public final class ConditionalShapelessRecipe extends ShapelessRecipe {

    public ConditionalShapelessRecipe(ResourceLocation id, String group, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group, result, ingredients);
    }

    @Override
    public boolean matches(CraftingInventory grid, World world) {
        return !CellLever.REPLACE_VANILLA_LEVER.get() && super.matches(grid, world);
    }

    public static final class Serializer extends ShapelessRecipe.Serializer {

        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
            setRegistryName(ns("shapeless_optional"));
        }

        @Override
        public ConditionalShapelessRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapelessRecipe recipe = super.fromJson(id, json);
            return new ConditionalShapelessRecipe(id, recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients());
        }

        @SuppressWarnings("ConstantConditions") // recipe is never null
        @Override
        public ConditionalShapelessRecipe fromNetwork(ResourceLocation id, PacketBuffer payload) {
            ShapelessRecipe recipe = super.fromNetwork(id, payload);
            return new ConditionalShapelessRecipe(id, recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients());
        }

        @Override
        public void toNetwork(PacketBuffer payload, ShapelessRecipe id) {
            super.toNetwork(payload, id);
        }
    }
}
