package dev.necauqua.mods.cl;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

import static dev.necauqua.mods.cl.CellLever.ns;

public final class ConditionalShapelessRecipe extends ShapelessRecipe {

    public ConditionalShapelessRecipe(ResourceLocation id, String group, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group, result, ingredients);
    }

    @Override
    public boolean matches(CraftingContainer grid, Level level) {
        return !CellLever.REPLACE_VANILLA_LEVER.get() && super.matches(grid, level);
    }

    public static final class Serializer extends ShapelessRecipe.Serializer {

        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
            setRegistryName(ns("shapeless_optional"));
        }

        @Override
        public ConditionalShapelessRecipe fromJson(ResourceLocation id, JsonObject json) {
            var recipe = super.fromJson(id, json);
            return new ConditionalShapelessRecipe(id, recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients());
        }

        @SuppressWarnings("ConstantConditions") // recipe is never null
        @Override
        public ConditionalShapelessRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf payload) {
            var recipe = super.fromNetwork(id, payload);
            return new ConditionalShapelessRecipe(id, recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients());
        }

        @Override
        public void toNetwork(FriendlyByteBuf payload, ShapelessRecipe id) {
            super.toNetwork(payload, id);
        }
    }
}
