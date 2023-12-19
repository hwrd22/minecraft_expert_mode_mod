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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
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
        NeoForge.EVENT_BUS.register(this);

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

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.KNIFE.get());
            event.accept(ModItems.VEX_SWORD.get());
            event.accept(ModItems.MOLTEN_AXE.get());
            event.accept(ModItems.DAMNED_AXE.get());
            event.accept(ModItems.SPEAR.get());
            event.accept(ModItems.FAKE_GILDED_TRIDENT.get());
            event.accept(ModItems.GILDED_TRIDENT.get());
            event.accept(ModItems.POSEIDON_TRIDENT.get());
            event.accept(ModItems.BLAZE_BOW.get());
            event.accept(ModItems.WITHER_BOW.get());
            event.accept(ModItems.DRAGON_BOW.get());
            event.accept(ModItems.JITTER_SHOTBOW.get());
            event.accept(ModItems.FIRE_STAFF.get());
            event.accept(ModItems.ICE_STAFF.get());
            event.accept(ModItems.LIGHTNING_STAFF.get());
            event.accept(ModItems.COPPER_HELMET.get());
            event.accept(ModItems.COPPER_CHESTPLATE.get());
            event.accept(ModItems.COPPER_LEGGINGS.get());
            event.accept(ModItems.COPPER_BOOTS.get());
            event.accept(ModItems.LAVA_DIVING_HELMET.get());
            event.accept(ModItems.LAVA_DIVING_CHESTPLATE.get());
            event.accept(ModItems.LAVA_DIVING_LEGGINGS.get());
            event.accept(ModItems.LAVA_DIVING_BOOTS.get());
            event.accept(ModItems.SLIME_BOOTS.get());
            event.accept(ModItems.PARADOX_BOOTS.get());
            event.accept(ModItems.BRUTE_CHESTPLATE.get());
            event.accept(ModItems.ENDER_DRAGON_HEAD.get());
            event.accept(ModItems.WITHER_SKULL.get());
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.SCYTHE.get());
            event.accept(ModItems.MOLTEN_AXE.get());
            event.accept(ModItems.DAMNED_AXE.get());
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
