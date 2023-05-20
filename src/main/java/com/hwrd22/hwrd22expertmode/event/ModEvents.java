package com.hwrd22.hwrd22expertmode.event;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import com.hwrd22.hwrd22expertmode.adrenaline.PlayerAdrenaline;
import com.hwrd22.hwrd22expertmode.adrenaline.PlayerAdrenalineProvider;
import com.hwrd22.hwrd22expertmode.item.ModItems;
import com.hwrd22.hwrd22expertmode.networking.ModMessages;
import com.hwrd22.hwrd22expertmode.networking.packet.AdrenalineDataSyncS2CPacket;
import com.hwrd22.hwrd22expertmode.networking.packet.RageDataSyncS2CPacket;
import com.hwrd22.hwrd22expertmode.rage.PlayerRage;
import com.hwrd22.hwrd22expertmode.rage.PlayerRageProvider;
import com.hwrd22.hwrd22expertmode.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Mod.EventBusSubscriber(modid = ExpertMode.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerRageProvider.PLAYER_RAGE).isPresent()) {
                event.addCapability(new ResourceLocation(ExpertMode.MODID, "rage_properties"), new PlayerRageProvider());
            }
            if (!event.getObject().getCapability(PlayerAdrenalineProvider.PLAYER_ADRENALINE).isPresent()) {
                event.addCapability(new ResourceLocation(ExpertMode.MODID, "adrenaline_properties"), new PlayerAdrenalineProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerRageProvider.PLAYER_RAGE).ifPresent(oldStore -> { event.getOriginal().getCapability(PlayerRageProvider.PLAYER_RAGE).ifPresent(newStore -> {newStore.copyFrom(oldStore);});});
            event.getOriginal().getCapability(PlayerAdrenalineProvider.PLAYER_ADRENALINE).ifPresent(oldStore -> { event.getOriginal().getCapability(PlayerAdrenalineProvider.PLAYER_ADRENALINE).ifPresent(newStore -> {newStore.copyFrom(oldStore);});});
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerRage.class);
        event.register(PlayerAdrenaline.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END) {
            event.player.getCapability(PlayerRageProvider.PLAYER_RAGE).ifPresent(rage -> {
                boolean nearbyHostiles = false;
                List<Entity> entities = event.player.getLevel().getEntities(event.player, event.player.getBoundingBox().inflate(16.0D, 8.0D, 16.0D));
                for (Entity value : entities) {
                    if (value instanceof Zombie || value instanceof Skeleton || value instanceof Creeper || value instanceof ZombieVillager || value instanceof Drowned || value instanceof Husk || value instanceof Stray || value instanceof Blaze || value instanceof CaveSpider || value instanceof Pillager || value instanceof Vindicator || value instanceof Vex || value instanceof Ravager || value instanceof Witch || value instanceof Slime || value instanceof WitherSkeleton || value instanceof Ghast || value instanceof Phantom || value instanceof MagmaCube || value instanceof Guardian || value instanceof Silverfish || value instanceof Endermite || value instanceof Hoglin || value instanceof Zoglin || value instanceof Shulker || value instanceof Evoker || value instanceof ElderGuardian || value instanceof WitherBoss || value instanceof EnderDragon || value instanceof Warden || value instanceof PiglinBrute || value instanceof ZombifiedPiglin || value instanceof EnderMan || value instanceof Spider || value instanceof Piglin) {
                        nearbyHostiles = true;
                        break;
                    }
                }
                if(rage.getRage() > 0 && rage.getRage() != 10000 && !rage.getRageUse() && !nearbyHostiles) {
                    rage.subtractRage(10);
                    ModMessages.sendToPlayer(new RageDataSyncS2CPacket(rage.getRage()), ((ServerPlayer) event.player));
                }
                else if (rage.getRage() != 10000 && !rage.getRageUse()){
                    for (Entity entity : entities) {
                        if (entity instanceof Zombie || entity instanceof Skeleton || entity instanceof Creeper || entity instanceof ZombieVillager || entity instanceof Drowned || entity instanceof Husk || entity instanceof Stray || entity instanceof Blaze || entity instanceof CaveSpider || entity instanceof Pillager || entity instanceof Vindicator || entity instanceof Vex || entity instanceof Ravager || entity instanceof Witch || entity instanceof Slime || entity instanceof WitherSkeleton || entity instanceof Ghast || entity instanceof Phantom || entity instanceof MagmaCube || entity instanceof Guardian || entity instanceof Silverfish || entity instanceof Endermite || entity instanceof Hoglin || entity instanceof Zoglin || entity instanceof Shulker) {
                            if (entity.distanceTo(event.player) > 4)
                                rage.addRage(17 - (int) entity.distanceTo(event.player));
                            else
                                rage.addRage(13);
                        }
                    }
                    for (Entity entity : entities) {
                        if (entity instanceof Evoker || entity instanceof ElderGuardian || entity instanceof WitherBoss || entity instanceof EnderDragon || entity instanceof Warden || entity instanceof PiglinBrute) {
                            if (entity.distanceTo(event.player) > 4)
                                rage.addRage(3 * (17 - (int) entity.distanceTo(event.player)));
                            else
                                rage.addRage(39);
                        }
                    }
                    ModMessages.sendToPlayer(new RageDataSyncS2CPacket(rage.getRage()), ((ServerPlayer) event.player));
                    if (rage.getRage() == 10000)
                        event.player.getLevel().playSound(null, event.player.getOnPos(), ModSounds.RAGE_FILLED.get(), SoundSource.PLAYERS, 0.9f, 1.0f);
                }
                if (rage.getRage() > 0 && rage.getRageUse()) {

                    event.player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 20));
                    event.player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 1));
                    event.player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 1));
                    event.player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 1));
                    event.player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 1));
                    rage.subtractRage(55);
                    ModMessages.sendToPlayer(new RageDataSyncS2CPacket(rage.getRage()), ((ServerPlayer) event.player));
                }
                else if (rage.getRageUse()){
                    event.player.getLevel().playSound(null, event.player.getOnPos(), ModSounds.RAGE_EMPTIED.get(), SoundSource.PLAYERS, 0.5f, 1.0f);
                    rage.resetRageUse();
                    ModMessages.sendToPlayer(new RageDataSyncS2CPacket(rage.getRage()), ((ServerPlayer) event.player));
                }
            });
            event.player.getCapability(PlayerAdrenalineProvider.PLAYER_ADRENALINE).ifPresent(adrenaline -> {
                boolean nearbyBosses = false;
                List<Entity> entities = event.player.getLevel().getEntities(event.player, event.player.getBoundingBox().inflate(160.0D, 160.0D, 160.0D));
                for (Entity entity : entities) {
                    if (entity instanceof Evoker || entity instanceof ElderGuardian || entity instanceof WitherBoss || entity instanceof EnderDragon || entity instanceof Warden || entity instanceof PiglinBrute) {
                        nearbyBosses = true;
                        break;
                    }
                }
                // logic for if boss exists and is near player to be added
                if(adrenaline.getAdrenaline() != 600 && !adrenaline.getAdrenalineUse() && (nearbyBosses)) {
                    adrenaline.addAdrenaline(1);
                    ModMessages.sendToPlayer(new AdrenalineDataSyncS2CPacket(adrenaline.getAdrenaline()), ((ServerPlayer) event.player));
                    if (adrenaline.getAdrenaline() == 600)
                        event.player.getLevel().playSound(null, event.player.getOnPos(), ModSounds.ADRENALINE_FILLED.get(), SoundSource.PLAYERS, 0.9f, 1.0f);
                }
                else if(adrenaline.getAdrenaline() == 600 && !adrenaline.getAdrenalineUse()) {
                    event.player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 2));
                    ModMessages.sendToPlayer(new AdrenalineDataSyncS2CPacket(adrenaline.getAdrenaline()), ((ServerPlayer) event.player));
                }
                if(adrenaline.getAdrenaline() > 0 && adrenaline.getAdrenalineUse()) {
                    event.player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 20, 1));
                    event.player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 1));
                    event.player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 4));
                    adrenaline.subtractAdrenaline(6);
                    ModMessages.sendToPlayer(new AdrenalineDataSyncS2CPacket(adrenaline.getAdrenaline()), ((ServerPlayer) event.player));
                }
                else if (adrenaline.getAdrenalineUse()){
                    event.player.getLevel().playSound(null, event.player.getOnPos(), ModSounds.ADRENALINE_EMPTIED.get(), SoundSource.PLAYERS, 0.5f, 1.0f);
                    adrenaline.resetAdrenalineUse();
                    ModMessages.sendToPlayer(new AdrenalineDataSyncS2CPacket(adrenaline.getAdrenaline()), ((ServerPlayer) event.player));
                }
            });
            if (event.player.getOnPos().getY() < -1.0) {
                // add fog logic here
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerRageProvider.PLAYER_RAGE).ifPresent(rage -> {
                    ModMessages.sendToPlayer(new RageDataSyncS2CPacket(rage.getRage()), player);
                });
                player.getCapability(PlayerAdrenalineProvider.PLAYER_ADRENALINE).ifPresent(adrenaline -> {
                    ModMessages.sendToPlayer(new AdrenalineDataSyncS2CPacket(adrenaline.getAdrenaline()), player);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof EnderDragon dragon) {
            dragon.getAttribute(Attributes.MAX_HEALTH).setBaseValue(400.0f);
            dragon.setHealth(400.0f);
            dragon.getAttribute(Attributes.ARMOR).setBaseValue(2.5f);
        }
        if (event.getEntity() instanceof Evoker evoker) {
            evoker.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0f);
            evoker.setHealth(100.0f);
        }
        if (event.getEntity() instanceof ElderGuardian eguardian) {
            eguardian.getAttribute(Attributes.MAX_HEALTH).setBaseValue(250.0f);
            eguardian.setHealth(250.0f);
        }
        if (event.getEntity() instanceof PiglinBrute brute) {
            brute.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0f);
            brute.setHealth(100.0f);
            brute.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.6125f);
            brute.getAttribute(Attributes.ARMOR).setBaseValue(5.0f);
        }
        if (event.getEntity() instanceof Warden warden) {
            warden.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1024.f);
            warden.setHealth(1024.0f);
        }
        if (event.getEntity() instanceof Zombie zombie && !(event.getEntity() instanceof ZombieVillager)) {
            if (zombie.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.AIR)
                zombie.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.OAK_LEAVES, 1));
            if (zombie.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.AIR)
                zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_AXE, 1));
            // check if world is in endgame mode here
            zombie.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0);  // move to else condition when endgame mode is established
            zombie.setHealth(40.0f);  // also accounts for endgame stat
            zombie.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.345);
        }
        if (event.getEntity() instanceof ZombieVillager zillager) {
            zillager.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4025);
            zillager.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0);
            zillager.setHealth(40.0f);  // also accounts for endgame stat
        }
    }

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof ZombieVillager zillager) {  // moving equipment event here because the on join level event is broken with professions
            if (zillager.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.AIR) {
                if (zillager.getVillagerData().getProfession() == VillagerProfession.NITWIT)
                    zillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIRT, 1));
            }
            if (zillager.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.AIR) {
                if (zillager.getVillagerData().getProfession() == VillagerProfession.NITWIT)
                    zillager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STICK, 1));
                if (zillager.getVillagerData().getProfession() == VillagerProfession.LIBRARIAN) {
                    ItemStack sharpBook = new ItemStack(Items.BOOK, 1);  // create a new item
                    sharpBook.enchant(Enchantments.SHARPNESS, 5);  // enchant the created item
                    sharpBook.enchant(Enchantments.FIRE_ASPECT, 2);  // enchant the created item
                    sharpBook.enchant(Enchantments.KNOCKBACK, 1);  // enchant the created item
                    zillager.setItemSlot(EquipmentSlot.MAINHAND, sharpBook);  // set equipment for librarian z.villager
                }
                if (zillager.getVillagerData().getProfession() == VillagerProfession.FARMER)
                    zillager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.SCYTHE.get()));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(PlayerRageProvider.PLAYER_RAGE).ifPresent(rage -> {
                if (rage.getRage() != 10000 && !rage.getRageUse()) {
                    rage.addRage((int) (event.getAmount()) * 50);
                    ModMessages.sendToPlayer(new RageDataSyncS2CPacket(rage.getRage()), player);
                    if (rage.getRage() == 10000)
                        player.getLevel().playSound(null, player.getOnPos(), ModSounds.RAGE_FILLED.get(), SoundSource.PLAYERS, 0.9f, 1.0f);
                }
            });
            player.getCapability(PlayerAdrenalineProvider.PLAYER_ADRENALINE).ifPresent(adrenaline -> {
                if (!adrenaline.getAdrenalineUse()) {
                    if (adrenaline.getAdrenaline() == 600)
                        player.getLevel().playSound(null, player.getOnPos(), ModSounds.ADRENALINE_EMPTIED.get(), SoundSource.PLAYERS, 0.5f, 1.0f);
                    adrenaline.resetAdrenaline();
                    ModMessages.sendToPlayer(new AdrenalineDataSyncS2CPacket(adrenaline.getAdrenaline()), player);
                }
            });
            if (event.getSource().getEntity() instanceof Spider || event.getSource().getEntity() instanceof CaveSpider) {
                BlockPos playerPos = player.getOnPos();
                BlockPos cobwebPos = new BlockPos(playerPos.getX(), playerPos.getY() + 1, playerPos.getZ());
                player.getLevel().setBlockAndUpdate(cobwebPos, Blocks.COBWEB.defaultBlockState());
            }
            if (event.getSource().getEntity() instanceof EnderMan) {
                Random random = new Random();
                BlockPos playerPos = player.getOnPos();
                playerPos = new BlockPos(playerPos.getX(), playerPos.getY() + 1, playerPos.getZ());
                double x_change = (double) random.nextInt(-10, 11);
                double y_change = (double) random.nextInt(-10, 11);
                double z_change = (double) random.nextInt(-10, 11);
                double new_x = playerPos.getX() + x_change;
                double new_y = Math.max(playerPos.getY() + y_change, -63.0);
                double new_z = playerPos.getZ() + z_change;
                player.teleportTo(new_x, new_y, new_z);
                player.setPos(new_x, new_y, new_z);
                player.getLevel().playSound(null, player.getOnPos(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
            }
        }
    }
}
