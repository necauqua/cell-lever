package dev.necauqua.mods.cl;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static dev.necauqua.mods.cl.CellLever.MODID;

@Mod(MODID)
@EventBusSubscriber(bus = Bus.MOD)
public final class CellLever {

    public static final Logger LOGGER = LogManager.getLogger(CellLever.class);

    public static final String MODID = "cell_lever";
    public static final ResourceLocation NAME = new ResourceLocation(MODID, "it");
    public static final CellLeverBlock BLOCK = new CellLeverBlock();
    public static final CellLeverBlockItem ITEM = new CellLeverBlockItem();

    private static final ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();

    public static final BooleanValue REPLACE_VANILLA_LEVER = CONFIG_BUILDER
        .comment("Replace the block that is placed by the lever item with a cell lever block, and change cell lever block to drop vanilla levers back")
        .define("replace_vanilla_lever", false);

    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> e) {
        e.getRegistry().register(BLOCK);
    }

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(ITEM);
    }

    @SubscribeEvent
    public static void registerRecipe(RegistryEvent.Register<RecipeSerializer<?>> e) {
        e.getRegistry().register(ConditionalShapelessRecipe.Serializer.INSTANCE);
    }

    public CellLever() {
        ModLoadingContext.get().registerConfig(Type.SERVER, CONFIG_BUILDER.build());
        ReplaceVanillaCondition.init();
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void on(ModConfigEvent e) {
        var leverItem = (BlockItem) Items.LEVER;
        if (REPLACE_VANILLA_LEVER.get()) {
            LOGGER.info("Setting vanilla lever item to place cell levers");
            leverItem.block = BLOCK;
        } else if (leverItem.block == BLOCK) {
            LOGGER.info("Resetting vanilla lever back to placing vanilla levers");
            leverItem.block = Blocks.LEVER;
        }
    }

    public static ResourceLocation ns(String name) {
        return new ResourceLocation(MODID, name);
    }
}
