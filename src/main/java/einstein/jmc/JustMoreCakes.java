package einstein.jmc;

import com.mojang.datafixers.util.Pair;
import cpw.mods.modlauncher.api.INameMappingService.Domain;
import einstein.jmc.client.gui.screens.inventory.CakeOvenScreen;
import einstein.jmc.init.*;
import einstein.jmc.util.EventHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.coremod.api.ASMAPI;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

@Mod(JustMoreCakes.MODID)
public class JustMoreCakes {

    public static final String MODID = "jmc";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final JMCTab JMC_TAB = new JMCTab(CreativeModeTab.TABS.length, "jmc_tab");

    public JustMoreCakes() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.init(modEventBus);
        ModBlockEntityTypes.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        ModRecipes.RECIPE_TYPES.register(modEventBus);
        ModVillagers.POIS.register(modEventBus);
        ModVillagers.PROFESSIONS.register(modEventBus);
        ModPotions.MOB_EFFECTS.register(modEventBus);
        ModPotions.POTIONS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.addListener(this::missingMappings);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ModServerConfigs.SERVERSPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModClientConfigs.CLIENTSPEC);
    }

    void commonSetup(final FMLCommonSetupEvent event) {
        registerVillageBuilding("plains", "bakery_1");
        registerVillageBuilding("plains", "bakery_2");
        registerVillageBuilding("desert", "bakery_1");
        registerVillageBuilding("savanna", "bakery_1");
        registerVillageBuilding("snowy", "bakery_1");
        registerVillageBuilding("snowy", "bakery_2");
        registerVillageBuilding("taiga", "bakery_1");
        GiveGiftToHero.GIFTS.put(ModVillagers.CAKE_BAKER.get(), new ResourceLocation(MODID, "gameplay/hero_of_the_village/cake_baker_gift"));
        resizeCakeStack();
    }

    void resizeCakeStack() {
        Field maxItemStackSize = null;

        try {
            final Field field = Item.class.getDeclaredField(ObfuscationReflectionHelper.remapName(Domain.FIELD, "f_41478_"));
            field.setAccessible(true);
            maxItemStackSize = field;
        } catch (Exception e) {
            LOGGER.catching(Level.WARN, e);
        }

        if (maxItemStackSize != null) {
            try {
                maxItemStackSize.setInt(Items.CAKE, 64);
            } catch (Exception e) {
                LOGGER.catching(Level.WARN, e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    void registerVillageBuilding(String biome, String name) {
        PlainVillagePools.bootstrap();
        DesertVillagePools.bootstrap();
        SavannaVillagePools.bootstrap();
        SnowyVillagePools.bootstrap();
        TaigaVillagePools.bootstrap();

        final StructureTemplatePool templatePool = BuiltinRegistries.TEMPLATE_POOL.get(new ResourceLocation("minecraft:village/" + biome + "/houses"));

        if (templatePool == null) {
            LOGGER.warn("Failed to register " + biome + " village building");
            return;
        }

        final StructurePoolElement structure = StructurePoolElement.legacy(MODID + ":village/" + biome + "/houses/" + biome + "_" + name, ProcessorLists.MOSSIFY_10_PERCENT).apply(StructureTemplatePool.Projection.RIGID);

        try {
            final Field templatesField = StructureTemplatePool.class.getDeclaredField(ASMAPI.mapField("f_210560_"));
            templatesField.setAccessible(true);
            final ObjectArrayList<StructurePoolElement> templates = (ObjectArrayList<StructurePoolElement>) templatesField.get(templatePool);
            templates.add(structure);

            final Field rawTemplatesField = StructureTemplatePool.class.getDeclaredField(ASMAPI.mapField("f_210559_"));
            rawTemplatesField.setAccessible(true);
            final List<Pair<StructurePoolElement, Integer>> rawTemplates = (List<Pair<StructurePoolElement, Integer>>) rawTemplatesField.get(templatePool);
            rawTemplates.add(Pair.of(structure, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    void onParallelDispatch(final ParallelDispatchEvent event) {
        event.enqueueWork(ModPotions::registerPotionRecipes);
    }

    void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RED_MUSHROOM_CAKE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BROWN_MUSHROOM_CAKE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CHORUS_CAKE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CRIMSON_FUNGUS_CAKE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ENCASING_ICE.get(), RenderType.translucent());

        MenuScreens.register(ModMenuTypes.CAKE_OVEN.get(), CakeOvenScreen::new);
    }

    void missingMappings(MissingMappingsEvent event) {
        handleMissingMappings(event, ForgeRegistries.ITEMS, (name) -> missingBlock(name).asItem());
        handleMissingMappings(event, ForgeRegistries.BLOCKS, this::missingBlock);
    }

    @Nullable
    private Block missingBlock(String name) {
        switch (name) {
            case "cheese_cake":
                return ModBlocks.CHEESECAKE.get();
            case "triple_decker_cake":
                return ModBlocks.THREE_TIERED_CAKE.get();
        }
        return null;
    }

    private <T> void handleMissingMappings(MissingMappingsEvent event, IForgeRegistry registry, Function<String, T> function) {
        List<MissingMappingsEvent.Mapping<T>> mappings = event.getMappings(registry.getRegistryKey(), MODID);

        for (MissingMappingsEvent.Mapping mapping : mappings) {
            T value = function.apply(mapping.getKey().getPath());
            if (value != null) {
                mapping.remap(value);
            } else {
                LOGGER.info("Failed to remap (" + mapping.getKey().toString() + ") of registry (" + registry.getRegistryName() + ")");
                mapping.fail();
            }
        }
    }
}
