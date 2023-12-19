package com.hwrd22.hwrd22expertmode;

import com.hwrd22.hwrd22expertmode.effect.ModEffects;
import com.hwrd22.hwrd22expertmode.entity.ModEntityType;
import com.hwrd22.hwrd22expertmode.item.ModItems;
import com.hwrd22.hwrd22expertmode.networking.ModMessages;
import com.hwrd22.hwrd22expertmode.potion.ModPotions;
import com.hwrd22.hwrd22expertmode.sound.ModSounds;
import com.hwrd22.hwrd22expertmode.util.ModItemProperties;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExpertMode.MODID)
public class ExpertMode
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "hwrd22expertmode";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    public ExpertMode()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Adding items from ModItems
        ModItems.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener((FMLCommonSetupEvent event) -> commonSetup());

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        ModSounds.register(modEventBus);

        ModEntityType.ENTITIES.register(modEventBus);

        ModEffects.register(modEventBus);

        ModPotions.register(modEventBus);
    }

    private void commonSetup()
    {
        ModItemProperties.addCustomItemProperties();
        ModMessages.register();
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event)
    {
        if (event.getTab() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.THIEF_GLOVE);
            event.accept(ModItems.KNIFE);
            event.accept(ModItems.VEX_SWORD);
            event.accept(ModItems.MOLTEN_AXE);
            event.accept(ModItems.DAMNED_AXE);
            event.accept(ModItems.SPEAR);
            event.accept(ModItems.FAKE_GILDED_TRIDENT);
            event.accept(ModItems.GILDED_TRIDENT);
            event.accept(ModItems.POSEIDON_TRIDENT);
            event.accept(ModItems.BLAZE_BOW);
            event.accept(ModItems.WITHER_BOW);
            event.accept(ModItems.DRAGON_BOW);
            event.accept(ModItems.JITTER_SHOTBOW);
            event.accept(ModItems.FIRE_STAFF);
            event.accept(ModItems.ICE_STAFF);
            event.accept(ModItems.LIGHTNING_STAFF);
            event.accept(ModItems.COPPER_HELMET);
            event.accept(ModItems.COPPER_CHESTPLATE);
            event.accept(ModItems.COPPER_LEGGINGS);
            event.accept(ModItems.COPPER_BOOTS);
            event.accept(ModItems.LAVA_DIVING_HELMET);
            event.accept(ModItems.LAVA_DIVING_CHESTPLATE);
            event.accept(ModItems.LAVA_DIVING_LEGGINGS);
            event.accept(ModItems.LAVA_DIVING_BOOTS);
            event.accept(ModItems.SLIME_BOOTS);
            event.accept(ModItems.PARADOX_BOOTS);
            event.accept(ModItems.BRUTE_CHESTPLATE);
            event.accept(ModItems.ENDER_DRAGON_HEAD);
            event.accept(ModItems.WITHER_SKULL);
        }
        if (event.getTab() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.SCYTHE);
            event.accept(ModItems.MOLTEN_AXE);
            event.accept(ModItems.DAMNED_AXE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
