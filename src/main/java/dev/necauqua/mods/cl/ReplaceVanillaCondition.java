package dev.necauqua.mods.cl;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public final class ReplaceVanillaCondition implements LootItemCondition {

    private static final ReplaceVanillaCondition INSTANCE = new ReplaceVanillaCondition();

    private ReplaceVanillaCondition() {}

    @Override
    public LootItemConditionType getType() {
        return CellLever.REPLACE_VANILLA_TYPE.get();
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
