package dev.necauqua.mods.cl;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import static dev.necauqua.mods.cl.CellLever.MODID;

public final class ReplaceVanillaCondition implements ILootCondition {

    private static final LootConditionType TYPE = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(MODID, "replace_vanilla"), new LootConditionType(Serializer.INSTANCE));
    private static final ReplaceVanillaCondition INSTANCE = new ReplaceVanillaCondition();

    public static void init() {
        // just trigger the static init lul
    }

    private ReplaceVanillaCondition() {}

    @Override
    public LootConditionType getType() {
        return TYPE;
    }

    @Override
    public boolean test(LootContext context) {
        return CellLever.REPLACE_VANILLA_LEVER.get();
    }

    public static class Serializer implements ILootSerializer<ReplaceVanillaCondition> {

        public static final Serializer INSTANCE = new ReplaceVanillaCondition.Serializer();

        @Override
        public void serialize(JsonObject object, ReplaceVanillaCondition condition, JsonSerializationContext context) {}

        @Override
        public ReplaceVanillaCondition deserialize(JsonObject object, JsonDeserializationContext context) {
            return ReplaceVanillaCondition.INSTANCE;
        }
    }
}
