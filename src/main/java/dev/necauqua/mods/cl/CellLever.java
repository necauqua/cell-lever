package dev.necauqua.mods.cl;

import dev.necauqua.mods.cl.ReplaceVanillaCondition.Serializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static dev.necauqua.mods.cl.CellLever.MODID;

@Mod(MODID)
@EventBusSubscriber(bus = Bus.MOD)
public final class CellLever {

    public static final Logger LOGGER = LogManager.getLogger(CellLever.class);

    public static final String MODID = "cell_lever";

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);
    private static final DeferredRegister<LootItemConditionType> LOOT_ITEMS = DeferredRegister.create(Registry.LOOT_ITEM_REGISTRY, MODID);

    public static final RegistryObject<CellLeverBlock> BLOCK = BLOCKS.register("it", CellLeverBlock::new);
    public static final RegistryObject<LootItemConditionType> REPLACE_VANILLA_TYPE = LOOT_ITEMS.register("replace_vanilla", () -> new LootItemConditionType(Serializer.INSTANCE));

    private static final ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();

    public static final BooleanValue REPLACE_VANILLA_LEVER = configBuilder
        .comment("Replace the block that is placed by the lever item with a cell lever block, and change cell lever block to drop vanilla levers back")
        .define("replace_vanilla_lever", false);

    public CellLever() {
        ModLoadingContext.get().registerConfig(Type.SERVER, configBuilder.build());
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register("it", CellLeverBlockItem::new);
        RECIPE_SERIALIZERS.register("shapeless_optional", ConditionalShapelessRecipe.Serializer::new);

        BLOCKS.register(bus);
        ITEMS.register(bus);
        RECIPE_SERIALIZERS.register(bus);
        LOOT_ITEMS.register(bus);
        ReplaceVanillaCondition.init();
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void on(ModConfigEvent e) {
        var leverItem = (BlockItem) Items.LEVER;
        if (REPLACE_VANILLA_LEVER.get()) {
            LOGGER.info("Setting vanilla lever item to place cell levers");
            leverItem.block = BLOCK.get();
        } else if (leverItem.block == BLOCK.get()) {
            LOGGER.info("Resetting vanilla lever back to placing vanilla levers");
            leverItem.block = Blocks.LEVER;
        }
    }

    public static ResourceLocation ns(String name) {
        return new ResourceLocation(MODID, name);
    }
}
