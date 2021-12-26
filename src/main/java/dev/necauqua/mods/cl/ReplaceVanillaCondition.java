package dev.necauqua.mods.cl;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import static dev.necauqua.mods.cl.CellLever.MODID;

public final class ReplaceVanillaCondition implements LootItemCondition {

    private static final LootItemConditionType TYPE = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(MODID, "replace_vanilla"), new LootItemConditionType(Serializer.INSTANCE));
    private static final ReplaceVanillaCondition INSTANCE = new ReplaceVanillaCondition();

    public static void init() {
        // just trigger the static init lul
    }

    private ReplaceVanillaCondition() {}

    @Override
    public LootItemConditionType getType() {
        return TYPE;
    }

    @Override
    public boolean test(LootContext context) {
        return CellLever.REPLACE_VANILLA_LEVER.get();
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ReplaceVanillaCondition> {

        public static final Serializer INSTANCE = new ReplaceVanillaCondition.Serializer();

        @Override
        public void serialize(JsonObject object, ReplaceVanillaCondition condition, JsonSerializationContext context) {}

        @Override
        public ReplaceVanillaCondition deserialize(JsonObject object, JsonDeserializationContext context) {
            return ReplaceVanillaCondition.INSTANCE;
        }
    }
}
