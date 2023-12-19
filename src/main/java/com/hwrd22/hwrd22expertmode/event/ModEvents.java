package com.hwrd22.hwrd22expertmode.event;

import com.google.common.collect.Lists;
import com.hwrd22.hwrd22expertmode.ExpertMode;
import com.hwrd22.hwrd22expertmode.adrenaline.PlayerAdrenaline;
import com.hwrd22.hwrd22expertmode.adrenaline.PlayerAdrenalineProvider;
import com.hwrd22.hwrd22expertmode.effect.ModEffects;
import com.hwrd22.hwrd22expertmode.entity.ArrowBolt;
import com.hwrd22.hwrd22expertmode.entity.ModEntityType;
import com.hwrd22.hwrd22expertmode.entity.WitherSkullBarrier;
import com.hwrd22.hwrd22expertmode.entity.projectile.DragonArrow;
import com.hwrd22.hwrd22expertmode.entity.projectile.LightningArrow;
import com.hwrd22.hwrd22expertmode.entity.projectile.ThrownGildedTrident;
import com.hwrd22.hwrd22expertmode.entity.projectile.WitherArrow;
import com.hwrd22.hwrd22expertmode.item.ModItems;
import com.hwrd22.hwrd22expertmode.networking.ModMessages;
import com.hwrd22.hwrd22expertmode.networking.packet.AdrenalineDataSyncS2CPacket;
import com.hwrd22.hwrd22expertmode.networking.packet.RageDataSyncS2CPacket;
import com.hwrd22.hwrd22expertmode.potion.ModPotions;
import com.hwrd22.hwrd22expertmode.rage.PlayerRage;
import com.hwrd22.hwrd22expertmode.rage.PlayerRageProvider;
import com.hwrd22.hwrd22expertmode.sound.ModSounds;
import com.hwrd22.hwrd22expertmode.util.DragonKilledSaveData;
import com.hwrd22.hwrd22expertmode.util.EndgameSaveData;
import com.hwrd22.hwrd22expertmode.util.WitherKilledSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.AttachCapabilitiesEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.*;
import java.util.stream.Collectors;

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
            event.getOriginal().getCapability(PlayerRageProvider.PLAYER_RAGE).ifPresent(oldStore -> event.getOriginal().getCapability(PlayerRageProvider.PLAYER_RAGE).ifPresent(newStore -> newStore.copyFrom(oldStore)));
            event.getOriginal().getCapability(PlayerAdrenalineProvider.PLAYER_ADRENALINE).ifPresent(oldStore -> event.getOriginal().getCapability(PlayerAdrenalineProvider.PLAYER_ADRENALINE).ifPresent(newStore -> newStore.copyFrom(oldStore)));
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
                List<Entity> entities = event.player.level().getEntities(event.player, event.player.getBoundingBox().inflate(16.0D, 8.0D, 16.0D));
                for (Entity value : entities) {
                    if (value instanceof Enemy) {
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
                        if (entity instanceof Monster && !(entity instanceof NeutralMob) || entity instanceof Slime || entity instanceof Ghast || entity instanceof Phantom || entity instanceof Hoglin || entity instanceof Shulker) {
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
                        event.player.level().playSound(null, event.player.getOnPos(), ModSounds.RAGE_FILLED.get(), SoundSource.PLAYERS, 0.9f, 1.0f);
                }
                if (rage.getRage() > 0 && rage.getRageUse()) {

                    event.player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 20));
                    event.player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 1));
                    event.player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 1));
                    event.player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 1));
                    rage.subtractRage(55);
                    ModMessages.sendToPlayer(new RageDataSyncS2CPacket(rage.getRage()), ((ServerPlayer) event.player));
                }
                else if (rage.getRageUse()){
                    event.player.level().playSound(null, event.player.getOnPos(), ModSounds.RAGE_EMPTIED.get(), SoundSource.PLAYERS, 0.5f, 1.0f);
                    rage.resetRageUse();
                    ModMessages.sendToPlayer(new RageDataSyncS2CPacket(rage.getRage()), ((ServerPlayer) event.player));
                }
            });
            event.player.getCapability(PlayerAdrenalineProvider.PLAYER_ADRENALINE).ifPresent(adrenaline -> {
                boolean nearbyBosses = false;
                List<Entity> bossScan = event.player.level().getEntities(event.player, event.player.getBoundingBox().inflate(160.0D, 160.0D, 160.0D));  // Long range for bosses with health bars (Dragon/Wither)
                List<Entity> minibossScan = event.player.level().getEntities(event.player, event.player.getBoundingBox().inflate(29.0D, 29.0D, 29.0D));  // Shorter range for "bosses" without health bars
                for (Entity entity : bossScan) {
                    if (entity instanceof WitherBoss || entity instanceof EnderDragon) {
                        nearbyBosses = true;
                        break;
                    }
                }
                for (Entity entity : minibossScan) {
                    if (entity instanceof Evoker || entity instanceof ElderGuardian || entity instanceof Warden || entity instanceof PiglinBrute) {
                        nearbyBosses = true;
                        break;
                    }
                }
                // logic for if boss exists and is near player to be added
                if(adrenaline.getAdrenaline() != 600 && !adrenaline.getAdrenalineUse() && (nearbyBosses)) {
                    adrenaline.addAdrenaline(1);
                    ModMessages.sendToPlayer(new AdrenalineDataSyncS2CPacket(adrenaline.getAdrenaline()), ((ServerPlayer) event.player));
                    if (adrenaline.getAdrenaline() == 600)
                        event.player.level().playSound(null, event.player.getOnPos(), ModSounds.ADRENALINE_FILLED.get(), SoundSource.PLAYERS, 0.9f, 1.0f);
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
                    event.player.level().playSound(null, event.player.getOnPos(), ModSounds.ADRENALINE_EMPTIED.get(), SoundSource.PLAYERS, 0.5f, 1.0f);
                    adrenaline.resetAdrenalineUse();
                    ModMessages.sendToPlayer(new AdrenalineDataSyncS2CPacket(adrenaline.getAdrenaline()), ((ServerPlayer) event.player));
                }
            });
            if (event.player.getY() < -32 && !event.player.hasEffect(MobEffects.NIGHT_VISION)) {
                event.player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60));
            }
            else if (event.player.getY() < 0 && !event.player.hasEffect(MobEffects.NIGHT_VISION)) {
                event.player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60));
            }
            if (event.player.level().dimension() == Level.NETHER && !event.player.hasEffect(MobEffects.NIGHT_VISION))
                event.player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60));
            ServerPlayer advancementChecker = (ServerPlayer) event.player;
            AdvancementProgress progress = advancementChecker.getAdvancements().getOrStartProgress(advancementChecker.createCommandSourceStack().getAdvancement(new ResourceLocation("minecraft", "nether/root")));
            if (event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.AIR && event.player.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.AIR && event.player.getItemBySlot(EquipmentSlot.LEGS).getItem() == Items.AIR && event.player.getItemBySlot(EquipmentSlot.FEET).getItem() == Items.AIR && event.player.isCrouching() && !progress.isDone()) {
                event.player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20));
                event.player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20));  // giving player time to re-sneak if needed
            }
            if (event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.LEATHER_HELMET && event.player.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.LEATHER_CHESTPLATE && event.player.getItemBySlot(EquipmentSlot.LEGS).getItem() == Items.LEATHER_LEGGINGS && event.player.getItemBySlot(EquipmentSlot.FEET).getItem() == Items.LEATHER_BOOTS) {
                event.player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1));
            }
            if (event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GOLDEN_HELMET && event.player.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.GOLDEN_CHESTPLATE && event.player.getItemBySlot(EquipmentSlot.LEGS).getItem() == Items.GOLDEN_LEGGINGS && event.player.getItemBySlot(EquipmentSlot.FEET).getItem() == Items.GOLDEN_BOOTS) {
                // Luck-based Armor Bonus, only good effects, but rare.
                int rand1 = new Random().nextInt(5000);
                int rand2 = new Random().nextInt(5000);
                if (rand1 == rand2) {
                    MobEffect[] effectArray = { MobEffects.MOVEMENT_SPEED, MobEffects.DIG_SPEED, MobEffects.DAMAGE_BOOST, MobEffects.HEAL, MobEffects.JUMP, MobEffects.REGENERATION, MobEffects.DAMAGE_RESISTANCE, MobEffects.FIRE_RESISTANCE, MobEffects.WATER_BREATHING, MobEffects.NIGHT_VISION };
                    int ticks = 200;
                    int randEffect = new Random().nextInt(10);
                    if (randEffect == 3)
                        ticks = 1;
                    event.player.addEffect(new MobEffectInstance(effectArray[randEffect], ticks));
                }
            }
            if (event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.IRON_HELMET && event.player.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.IRON_CHESTPLATE && event.player.getItemBySlot(EquipmentSlot.LEGS).getItem() == Items.IRON_LEGGINGS && event.player.getItemBySlot(EquipmentSlot.FEET).getItem() == Items.IRON_BOOTS) {
                event.player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1));
            }
            if (event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CHAINMAIL_HELMET && event.player.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.CHAINMAIL_CHESTPLATE && event.player.getItemBySlot(EquipmentSlot.LEGS).getItem() == Items.CHAINMAIL_LEGGINGS && event.player.getItemBySlot(EquipmentSlot.FEET).getItem() == Items.CHAINMAIL_BOOTS) {
                event.player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1));
            }
            if ((event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.DIAMOND_HELMET || event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.WITHER_SKULL.get() || event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.ENDER_DRAGON_HEAD.get()) && event.player.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.DIAMOND_CHESTPLATE && event.player.getItemBySlot(EquipmentSlot.LEGS).getItem() == Items.DIAMOND_LEGGINGS && event.player.getItemBySlot(EquipmentSlot.FEET).getItem() == Items.DIAMOND_BOOTS) {
                event.player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 1));
            }
            if ((event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.NETHERITE_HELMET || event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.WITHER_SKULL.get() || event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.ENDER_DRAGON_HEAD.get()) && event.player.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.NETHERITE_CHESTPLATE && event.player.getItemBySlot(EquipmentSlot.LEGS).getItem() == Items.NETHERITE_LEGGINGS && event.player.getItemBySlot(EquipmentSlot.FEET).getItem() == Items.NETHERITE_BOOTS) {
                event.player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 1));
                event.player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1));
            }
            if (event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.LAVA_DIVING_HELMET.get() && event.player.getItemBySlot(EquipmentSlot.CHEST).getItem() == ModItems.LAVA_DIVING_CHESTPLATE.get() && event.player.getItemBySlot(EquipmentSlot.LEGS).getItem() == ModItems.LAVA_DIVING_LEGGINGS.get() && event.player.getItemBySlot(EquipmentSlot.FEET).getItem() == ModItems.LAVA_DIVING_BOOTS.get()) {
                event.player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 1));
                event.player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1));
            }
            if (event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.COPPER_HELMET.get() && event.player.getItemBySlot(EquipmentSlot.CHEST).getItem() == ModItems.COPPER_CHESTPLATE.get() && event.player.getItemBySlot(EquipmentSlot.LEGS).getItem() == ModItems.COPPER_LEGGINGS.get() && event.player.getItemBySlot(EquipmentSlot.FEET).getItem() == ModItems.COPPER_BOOTS.get()) {
                event.player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1, 0));  // lowers melee damage in favor of ranged damage
            }
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END) {
            EndgameSaveData endgameFlag = EndgameSaveData.manage(event.getServer());
            DragonKilledSaveData dragonFlag = DragonKilledSaveData.manage(event.getServer());
            WitherKilledSaveData witherFlag = WitherKilledSaveData.manage(event.getServer());
            CompoundTag nbt = new CompoundTag();
            boolean endgame = endgameFlag.get();
            boolean dragon = dragonFlag.get();
            boolean wither = witherFlag.get();
            if (!endgame) {
                if (!dragon) {
                    List<ServerPlayer> players = event.getServer().overworld().players();
                    for (ServerPlayer player : players) {
                        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(player.createCommandSourceStack().getAdvancement(new ResourceLocation("minecraft", "end/kill_dragon")));
                        if (progress.isDone()) {
                            dragonFlag.set(true);
                            dragonFlag.save(nbt);
                            break;
                        }
                    }
                }
                if (!wither) {
                    List<ServerPlayer> players = event.getServer().overworld().players();
                    for (ServerPlayer player : players) {
                        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(player.createCommandSourceStack().getAdvancement(new ResourceLocation(ExpertMode.MODID, "kill_real_wither")));
                        if (progress.isDone()) {
                            witherFlag.set(true);
                            witherFlag.save(nbt);
                            break;
                        }
                    }
                }
                if (wither && dragon) {
                    endgameFlag.set(true);
                    endgameFlag.save(nbt);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerRageProvider.PLAYER_RAGE).ifPresent(rage -> ModMessages.sendToPlayer(new RageDataSyncS2CPacket(rage.getRage()), player));
                player.getCapability(PlayerAdrenalineProvider.PLAYER_ADRENALINE).ifPresent(adrenaline -> ModMessages.sendToPlayer(new AdrenalineDataSyncS2CPacket(adrenaline.getAdrenaline()), player));
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        EndgameSaveData data = new EndgameSaveData();
        if (!event.getLevel().isClientSide) {
            data = EndgameSaveData.manage(Objects.requireNonNull(event.getLevel().getServer()));
        }
        if (event.getEntity() instanceof Bat bat) {
            List<Entity> nearbyEntities = bat.level().getEntities(bat, bat.getBoundingBox().inflate(16d));
            for (Entity entity : nearbyEntities) {
                if (entity instanceof ServerPlayer player) {
                    bat.setTarget(player);
                    break;
                }
            }
            bat.setAggressive(true);
        }
        if (event.getEntity() instanceof EnderDragon dragon) {
            List<? extends Player> players = dragon.level().players();
            int numPlayers = players.size() - 1;  // not counting 1 player
            Objects.requireNonNull(dragon.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(400.0f + numPlayers * 132);
            dragon.setHealth((float) Objects.requireNonNull(dragon.getAttribute(Attributes.MAX_HEALTH)).getValue());
            Objects.requireNonNull(dragon.getAttribute(Attributes.ARMOR)).setBaseValue(2.5f);
        }
        if (event.getEntity() instanceof WitherBoss wither) {
            if (wither.addTag("phase2")) {
                wither.removeTag("phase2");
                List<? extends Player> players = wither.level().players();
                int numPlayers = players.size() - 1;  // not counting 1 player
                Objects.requireNonNull(wither.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(300 + numPlayers * 100);
                wither.setHealth((float) Objects.requireNonNull(wither.getAttribute(Attributes.MAX_HEALTH)).getValue());
                wither.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TOTEM_OF_UNDYING));
            }
            else {
                List<? extends Player> players = wither.level().players();
                int numPlayers = players.size() - 1;  // not counting 1 player
                Objects.requireNonNull(wither.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(750 + numPlayers * 100);  // maxes out at 1024 due to memory limit
                wither.setHealth((float) Objects.requireNonNull(wither.getAttribute(Attributes.MAX_HEALTH)).getValue());
                if (data.get())
                    wither.addTag("endgame");
            }
        }

        if (event.getEntity() instanceof Evoker evoker) {
            Objects.requireNonNull(evoker.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(100.0f );
            evoker.setHealth(100.0f);
        }
        if (event.getEntity() instanceof ElderGuardian elderGuardian) {
            Objects.requireNonNull(elderGuardian.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(250.0f);
            elderGuardian.setHealth(250.0f);
        }
        if (event.getEntity() instanceof PiglinBrute piglinBrute) {
            Objects.requireNonNull(piglinBrute.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(100.0f);
            piglinBrute.setHealth(100.0f);
            Objects.requireNonNull(piglinBrute.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.6125f);
            Objects.requireNonNull(piglinBrute.getAttribute(Attributes.ARMOR)).setBaseValue(5.0f);
            ItemStack bruteVest = new ItemStack(ModItems.BRUTE_CHESTPLATE.get());
            bruteVest.enchant(Enchantments.THORNS, 3);
            bruteVest.enchant(Enchantments.UNBREAKING, 5);
            piglinBrute.setItemSlot(EquipmentSlot.CHEST, bruteVest);
            piglinBrute.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MOLTEN_AXE.get()));
        }
        if (event.getEntity() instanceof Warden warden) {
            Objects.requireNonNull(warden.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(1024.f);
            warden.setHealth(1024.0f);
        }
        if (event.getEntity() instanceof Zombie zombie && !(event.getEntity() instanceof ZombieVillager || event.getEntity() instanceof Husk || event.getEntity() instanceof Drowned || event.getEntity() instanceof ZombifiedPiglin)) {
            if (zombie.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.AIR)
                zombie.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.OAK_LEAVES, 1));
            if (zombie.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.AIR) {
                if (data.get()) {
                    ItemStack knockbackAxe = new ItemStack(Items.IRON_AXE);
                    knockbackAxe.enchant(Enchantments.KNOCKBACK, 1);
                    zombie.setItemSlot(EquipmentSlot.MAINHAND, knockbackAxe);
                }
                else
                    zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_AXE, 1));
            }

            if (data.get()) {  // endgame mode
                Objects.requireNonNull(zombie.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(40.0);
                Objects.requireNonNull(zombie.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(10.0);
                Objects.requireNonNull(zombie.getAttribute(Attributes.ARMOR)).setBaseValue(4.0);
            }
            else
                Objects.requireNonNull(zombie.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(30.0);  // not endgame mode
            zombie.setHealth(40.0f);  // also accounts for endgame stat
            Objects.requireNonNull(zombie.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.345);
        }
        if (event.getEntity() instanceof ZombieVillager zombieVillager) {
            Objects.requireNonNull(zombieVillager.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.4025);
            if (data.get()) {
                Objects.requireNonNull(zombieVillager.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(40.0);
                Objects.requireNonNull(zombieVillager.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(10.0);
                Objects.requireNonNull(zombieVillager.getAttribute(Attributes.ARMOR)).setBaseValue(4.0);
            }
            else
                Objects.requireNonNull(zombieVillager.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(30.0);

            zombieVillager.setHealth(40.0f);  // also accounts for endgame stat
        }
        if (event.getEntity() instanceof Drowned drowned) {
            Objects.requireNonNull(drowned.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.345);
            ItemStack boots;
            if (drowned.getItemBySlot(EquipmentSlot.FEET).getItem() == Items.AIR)
                boots = new ItemStack(Items.LEATHER_BOOTS);
            else
                boots = drowned.getItemBySlot(EquipmentSlot.FEET);
            boots.enchant(Enchantments.DEPTH_STRIDER, 3);
            drowned.setItemSlot(EquipmentSlot.FEET, boots);
            if (drowned.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.AIR)
                drowned.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GLASS));
            ItemStack trident;
            if (!drowned.getItemBySlot(EquipmentSlot.MAINHAND).equals(new ItemStack(Items.TRIDENT)))
                trident = new ItemStack(Items.TRIDENT);
            else
                trident = drowned.getItemBySlot(EquipmentSlot.MAINHAND);
            if (data.get()) {
                Objects.requireNonNull(drowned.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(40.0);
                Objects.requireNonNull(drowned.getAttribute(Attributes.ARMOR)).setBaseValue(4.0);
                trident.enchant(Enchantments.IMPALING, 4);
            }
            else {
                Objects.requireNonNull(drowned.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(30.0);
                trident.enchant(Enchantments.IMPALING, 1);
            }
            drowned.setItemSlot(EquipmentSlot.MAINHAND, trident);
            drowned.setHealth(40.0f);
        }

        if (event.getEntity() instanceof Husk husk) {
            Objects.requireNonNull(husk.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.253);
            if (data.get()) {
                Objects.requireNonNull(husk.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(40.0);
                Objects.requireNonNull(husk.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(10.0);
                Objects.requireNonNull(husk.getAttribute(Attributes.ARMOR)).setBaseValue(10.0);
                if (husk.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.AIR)
                    husk.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_AXE));
            }
            else {
                Objects.requireNonNull(husk.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(30.0);
                if (husk.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.AIR)
                    husk.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_AXE));
            }
            husk.setHealth(40.0f);
        }

        if (event.getEntity() instanceof ZombifiedPiglin zombifiedPiglin) {
            Objects.requireNonNull(zombifiedPiglin.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.345);
            ItemStack fireAxe = new ItemStack(Items.GOLDEN_AXE);
            fireAxe.enchant(Enchantments.FIRE_ASPECT, 3);
            zombifiedPiglin.setItemSlot(EquipmentSlot.MAINHAND, fireAxe);
            if (data.get()) {
                Objects.requireNonNull(zombifiedPiglin.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(40.0);
                Objects.requireNonNull(zombifiedPiglin.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(14.0);
                Objects.requireNonNull(zombifiedPiglin.getAttribute(Attributes.ARMOR)).setBaseValue(4.0);
            }
            else
                Objects.requireNonNull(zombifiedPiglin.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(30.0);
            zombifiedPiglin.setHealth(40.0f);
        }

        if (event.getEntity() instanceof Skeleton skeleton) {
            if (skeleton.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.AIR)
                skeleton.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.OAK_LEAVES));
            Random randStack = new Random();
            ItemStack arrows = new ItemStack(Items.TIPPED_ARROW, randStack.nextInt(1, 10));

            Objects.requireNonNull(skeleton.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.375);
            if (data.get()) {
                Objects.requireNonNull(skeleton.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(40.0);
                Objects.requireNonNull(skeleton.getAttribute(Attributes.ARMOR)).setBaseValue(4.0);
                Objects.requireNonNull(skeleton.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(9.0);
                PotionUtils.setPotion(arrows, ModPotions.CURSED.get());
            }
            else {
                Objects.requireNonNull(skeleton.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(30.0);
                Objects.requireNonNull(skeleton.getAttribute(Attributes.ARMOR)).setBaseValue(2.0);
                PotionUtils.setPotion(arrows, ModPotions.ROTTEN.get());
            }
            skeleton.setItemInHand(InteractionHand.OFF_HAND, arrows);
            skeleton.setHealth(40.0f);
        }

        if (event.getEntity() instanceof WitherSkeleton witherSkeleton) {
            witherSkeleton.setItemSlot(EquipmentSlot.FEET, new ItemStack(ModItems.PARADOX_BOOTS.get()));
            Random randStack = new Random();
            ItemStack arrows = new ItemStack(Items.TIPPED_ARROW, randStack.nextInt(1, 10));
            PotionUtils.setPotion(arrows, ModPotions.WITHER.get());

            if (data.get()) {
                Objects.requireNonNull(witherSkeleton.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(40.0f);
                Objects.requireNonNull(witherSkeleton.getAttribute(Attributes.ARMOR)).setBaseValue(1.5f);
                Objects.requireNonNull(witherSkeleton.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(10.0f);
            }
            else
                Objects.requireNonNull(witherSkeleton.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(30.0f);
            witherSkeleton.setHealth(40.0f);
            witherSkeleton.setItemInHand(InteractionHand.OFF_HAND, arrows);
            witherSkeleton.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
        }

        if (event.getEntity() instanceof Slime slime && !(slime instanceof MagmaCube)) {
            Random rand = new Random();
            if (rand.nextInt(1000) == rand.nextInt(1000)) {  // 1 in 1000 chance
                slime.setSize(10, true);
                Objects.requireNonNull(slime.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(181.5);
                Objects.requireNonNull(slime.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(16.5);
                slime.setCustomName(Component.literal("King Slime"));  // behold, a boss
            }
            // Stats are dependent on size
            if (data.get()) {
                if (slime.getSize() == 1) {
                    Objects.requireNonNull(slime.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(4.5);
                    Objects.requireNonNull(slime.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(4.5);
                }
                if (slime.getSize() == 2) {
                    Objects.requireNonNull(slime.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(18);
                    Objects.requireNonNull(slime.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(9);
                }
                if (slime.getSize() == 3) {
                    Objects.requireNonNull(slime.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(72);
                    Objects.requireNonNull(slime.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(18);
                }
            }
            else {
                if (slime.getSize() == 1) {
                    Objects.requireNonNull(slime.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(1.5);
                    Objects.requireNonNull(slime.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(1.5);
                }
                if (slime.getSize() == 2) {
                    Objects.requireNonNull(slime.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(6);
                    Objects.requireNonNull(slime.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(3);
                }
                if (slime.getSize() == 3) {
                    Objects.requireNonNull(slime.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(24);
                    Objects.requireNonNull(slime.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(6);
                }
            }
            Objects.requireNonNull(slime.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(1.0);
            slime.setHealth(181.5f);
        }

        if (event.getEntity() instanceof MagmaCube magmaCube) {
            Random rand = new Random();
            if (rand.nextInt(1000) == rand.nextInt(1000)) {  // 1 in 1000 chance
                magmaCube.setSize(10, true);
                Objects.requireNonNull(magmaCube.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(242);
                Objects.requireNonNull(magmaCube.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(22);
                magmaCube.setCustomName(Component.literal("King Magma Cube"));  // behold, a boss
            }
            // Stats are dependent on size
            if (data.get()) {
                if (magmaCube.getSize() == 1) {
                    Objects.requireNonNull(magmaCube.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(5);
                    Objects.requireNonNull(magmaCube.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(5);
                }
                if (magmaCube.getSize() == 2) {
                    Objects.requireNonNull(magmaCube.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(20);
                    Objects.requireNonNull(magmaCube.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(10);
                }
                if (magmaCube.getSize() == 3) {
                    Objects.requireNonNull(magmaCube.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(80);
                    Objects.requireNonNull(magmaCube.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(20);
                }
            }
            else {
                if (magmaCube.getSize() == 1) {
                    Objects.requireNonNull(magmaCube.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(2);
                    Objects.requireNonNull(magmaCube.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(2);
                }
                if (magmaCube.getSize() == 2) {
                    Objects.requireNonNull(magmaCube.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(8);
                    Objects.requireNonNull(magmaCube.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(4);
                }
                if (magmaCube.getSize() == 3) {
                    Objects.requireNonNull(magmaCube.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(32);
                    Objects.requireNonNull(magmaCube.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(8);
                }
            }
            Objects.requireNonNull(magmaCube.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(1.5);
            magmaCube.setHealth(242f);
        }

        if (event.getEntity() instanceof Creeper creeper) {
            Objects.requireNonNull(creeper.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.375);
            CompoundTag nbt = creeper.serializeNBT();
            nbt.putBoolean("powered", true);
            nbt.putShort("Fuse", (short) 20);
            if (data.get()) {
                Objects.requireNonNull(creeper.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(35);
                Objects.requireNonNull(creeper.getAttribute(Attributes.ARMOR)).setBaseValue(1.5);
                nbt.putBoolean("Silent", true);
            }
            else
                Objects.requireNonNull(creeper.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(25);
            creeper.deserializeNBT(nbt);
            creeper.setHealth(35f);
        }

        if (event.getEntity() instanceof Blaze blaze) {
            if (data.get()) {
                Objects.requireNonNull(blaze.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(35);
                Objects.requireNonNull(blaze.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(9);
                Objects.requireNonNull(blaze.getAttribute(Attributes.ARMOR)).setBaseValue(1.5);
            }
            else {
                Objects.requireNonNull(blaze.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(25);
                Objects.requireNonNull(blaze.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(7.5);
            }
            blaze.setHealth(35f);
        }

        if (event.getEntity() instanceof Endermite endermite) {
            Objects.requireNonNull(endermite.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.375);
            if (data.get()) {
                Objects.requireNonNull(endermite.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(20);
                Objects.requireNonNull(endermite.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(9);
                Objects.requireNonNull(endermite.getAttribute(Attributes.ARMOR)).setBaseValue(2);
            }
            else {
                Objects.requireNonNull(endermite.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(12);
                Objects.requireNonNull(endermite.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(3);
                Objects.requireNonNull(endermite.getAttribute(Attributes.ARMOR)).setBaseValue(1);
            }
            endermite.setHealth(20f);
        }

        if (event.getEntity() instanceof EnderMan enderMan) {
            if (data.get()) {
                Objects.requireNonNull(enderMan.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(70);
                Objects.requireNonNull(enderMan.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(22);
                Objects.requireNonNull(enderMan.getAttribute(Attributes.ARMOR)).setBaseValue(1.5);
            }
            else {
                Objects.requireNonNull(enderMan.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(50);
                Objects.requireNonNull(enderMan.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(10.5);
            }
            enderMan.setHealth(70f);
            if (enderMan.level().dimension() != Level.END) {
                enderMan.setCarriedBlock(Blocks.TNT.defaultBlockState());
            }
        }

        if (event.getEntity() instanceof Ghast ghast) {
            if (data.get()) {
                Objects.requireNonNull(ghast.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(25);
                Objects.requireNonNull(ghast.getAttribute(Attributes.ARMOR)).setBaseValue(1.5);
            }
            else
                Objects.requireNonNull(ghast.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(15);
            ghast.setHealth(25f);
        }

        if (event.getEntity() instanceof Guardian guardian && !(guardian instanceof ElderGuardian)) {
            if (data.get()) {
                Objects.requireNonNull(guardian.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(60);
                Objects.requireNonNull(guardian.getAttribute(Attributes.ARMOR)).setBaseValue(3);
            }
            else {
                Objects.requireNonNull(guardian.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(45);
                Objects.requireNonNull(guardian.getAttribute(Attributes.ARMOR)).setBaseValue(2);
            }
            guardian.setHealth(60f);
        }

        if (event.getEntity() instanceof Hoglin hoglin) {
            if (data.get()) {
                Objects.requireNonNull(hoglin.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(60);
                Objects.requireNonNull(hoglin.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(24);
                Objects.requireNonNull(hoglin.getAttribute(Attributes.ARMOR)).setBaseValue(2);
            }
            else {
                Objects.requireNonNull(hoglin.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(50);
                Objects.requireNonNull(hoglin.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(8);
            }
            hoglin.setHealth(60f);
        }

        if (event.getEntity() instanceof Zoglin zoglin) {
            if (data.get()) {
                Objects.requireNonNull(zoglin.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(60);
                Objects.requireNonNull(zoglin.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(24);
                Objects.requireNonNull(zoglin.getAttribute(Attributes.ARMOR)).setBaseValue(2);
            }
            else {
                Objects.requireNonNull(zoglin.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(50);
                Objects.requireNonNull(zoglin.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(8);
            }
            zoglin.setHealth(60f);
        }

        if (event.getEntity() instanceof Phantom phantom) {
            if (data.get()) {
                Objects.requireNonNull(phantom.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(40);
                Objects.requireNonNull(phantom.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(12);
                Objects.requireNonNull(phantom.getAttribute(Attributes.ARMOR)).setBaseValue(1.5);
            }
            else {
                Objects.requireNonNull(phantom.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(30);
                Objects.requireNonNull(phantom.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(3);
            }
            phantom.setHealth(40f);
        }

        if (event.getEntity() instanceof Piglin piglin) {
            if (piglin.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.GOLDEN_SWORD) {
                ItemStack goldSword = piglin.getItemInHand(InteractionHand.MAIN_HAND);
                goldSword.enchant(Enchantments.FIRE_ASPECT, 2);
                piglin.setItemInHand(InteractionHand.MAIN_HAND, goldSword);
            }
            else if (piglin.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.CROSSBOW) {
                ItemStack crossbow = piglin.getItemInHand(InteractionHand.MAIN_HAND);
                crossbow.enchant(Enchantments.QUICK_CHARGE, 3);
                crossbow.enchant(Enchantments.MULTISHOT, 1);
                piglin.setItemInHand(InteractionHand.MAIN_HAND, crossbow);
            }
            if (data.get()) {
                Objects.requireNonNull(piglin.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(40);
                Objects.requireNonNull(piglin.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(14);
                Objects.requireNonNull(piglin.getAttribute(Attributes.ARMOR)).setBaseValue(1.5);
            }
            else
                Objects.requireNonNull(piglin.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(24);
            piglin.setHealth(40f);
        }

        if (event.getEntity() instanceof Pillager pillager) {
            ItemStack crossbow = pillager.getItemInHand(InteractionHand.MAIN_HAND);
            crossbow.enchant(Enchantments.PIERCING, 1);
            crossbow.enchant(Enchantments.QUICK_CHARGE, 3);
            pillager.setItemInHand(InteractionHand.MAIN_HAND, crossbow);
            ItemStack fireworkStar = new ItemStack(Items.FIREWORK_STAR);
            CompoundTag starCompoundTag = fireworkStar.getOrCreateTagElement("Explosion");
            FireworkRocketItem.Shape fireworkrocketitem$shape = FireworkRocketItem.Shape.SMALL_BALL;
            List<Integer> list = Lists.newArrayList();
            list.add(((DyeItem)Items.GREEN_DYE).getDyeColor().getFireworkColor());
            list.add(((DyeItem)Items.LIME_DYE).getDyeColor().getFireworkColor());

            starCompoundTag.putIntArray("Colors", list);
            starCompoundTag.putByte("Type", (byte)fireworkrocketitem$shape.getId());

            ItemStack fireworks = new ItemStack(Items.FIREWORK_ROCKET, 5);
            CompoundTag fireworksTag = fireworks.getOrCreateTagElement("Fireworks");
            ListTag listtag = new ListTag();
            CompoundTag explosionTag = fireworkStar.getOrCreateTagElement("Explosion");
            listtag.add(explosionTag);

            fireworksTag.putByte("Flight", (byte)3);
            fireworksTag.put("Explosions", listtag);
            pillager.setItemInHand(InteractionHand.OFF_HAND, fireworks);
            if (data.get()) {
                Objects.requireNonNull(pillager.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(48);
                Objects.requireNonNull(pillager.getAttribute(Attributes.ARMOR)).setBaseValue(1.5);
            }
            else
                Objects.requireNonNull(pillager.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(36);
            pillager.setHealth(48f);
        }

        if (event.getEntity() instanceof Ravager ravager) {
            if (data.get()) {
                Objects.requireNonNull(ravager.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(150);
                Objects.requireNonNull(ravager.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(22);
                Objects.requireNonNull(ravager.getAttribute(Attributes.ARMOR)).setBaseValue(1.5);
            }
            else {
                Objects.requireNonNull(ravager.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(125);
                Objects.requireNonNull(ravager.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(18);
            }
            ravager.setHealth(150f);
        }

        if (event.getEntity() instanceof Shulker shulker) {
            if (data.get())
                Objects.requireNonNull(shulker.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(60);
            else
                Objects.requireNonNull(shulker.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(45);
            shulker.setHealth(60f);
        }

        if (event.getEntity() instanceof Silverfish silverfish) {
            Objects.requireNonNull(silverfish.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.375);
            if (data.get()) {
                Objects.requireNonNull(silverfish.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(20);
                Objects.requireNonNull(silverfish.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(9);
                Objects.requireNonNull(silverfish.getAttribute(Attributes.ARMOR)).setBaseValue(2);
            }
            else {
                Objects.requireNonNull(silverfish.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(12);
                Objects.requireNonNull(silverfish.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(1.5);
                Objects.requireNonNull(silverfish.getAttribute(Attributes.ARMOR)).setBaseValue(1);
            }
            silverfish.setHealth(20f);
        }

        if (event.getEntity() instanceof Spider spider && !(spider instanceof CaveSpider)) {
            if (data.get()) {
                Objects.requireNonNull(spider.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(40);
                Objects.requireNonNull(spider.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(12);
                Objects.requireNonNull(spider.getAttribute(Attributes.ARMOR)).setBaseValue(1.5);
            }
            else {
                Objects.requireNonNull(spider.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(24);
                Objects.requireNonNull(spider.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(3);
            }
            spider.setHealth(40f);
        }

        if (event.getEntity() instanceof CaveSpider caveSpider) {
            Objects.requireNonNull(caveSpider.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.33);
            if (data.get()) {
                Objects.requireNonNull(caveSpider.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(30);
                Objects.requireNonNull(caveSpider.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(12);
                Objects.requireNonNull(caveSpider.getAttribute(Attributes.ARMOR)).setBaseValue(1.5);
            }
            else {
                Objects.requireNonNull(caveSpider.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(18);
                Objects.requireNonNull(caveSpider.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(3);
            }
            caveSpider.setHealth(30f);
        }

        if (event.getEntity() instanceof Stray stray) {
            if (stray.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.AIR)
                stray.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.ICE));
            if (data.get()) {
                ItemStack bow = stray.getItemInHand(InteractionHand.MAIN_HAND);
                bow.enchant(Enchantments.POWER_ARROWS, 4);
                stray.setItemInHand(InteractionHand.MAIN_HAND, bow);
                Objects.requireNonNull(stray.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(35);
                Objects.requireNonNull(stray.getAttribute(Attributes.ARMOR)).setBaseValue(4);
            }
            else {
                Objects.requireNonNull(stray.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(25);
                Objects.requireNonNull(stray.getAttribute(Attributes.ARMOR)).setBaseValue(2);
            }
            Random randStack = new Random();
            int stackNumber = randStack.nextInt(1, 10);
            ItemStack frostArrow = new ItemStack(Items.TIPPED_ARROW, stackNumber);
            PotionUtils.setPotion(frostArrow, ModPotions.FROSTBURN.get());
            stray.setItemInHand(InteractionHand.OFF_HAND, frostArrow);
            stray.setHealth(35f);
        }

        if (event.getEntity() instanceof Vex vex) {
            vex.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_SWORD));
            vex.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.TOTEM_OF_UNDYING));
            if (data.get()) {
                Objects.requireNonNull(vex.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(22);
                Objects.requireNonNull(vex.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(8);
            }
            else
                Objects.requireNonNull(vex.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(18);
            vex.setHealth(22f);
        }

        if (event.getEntity() instanceof Vindicator vindicator) {
            if (data.get()) {
                Objects.requireNonNull(vindicator.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(48);
                Objects.requireNonNull(vindicator.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(10);
                Objects.requireNonNull(vindicator.getAttribute(Attributes.ARMOR)).setBaseValue(1.5);
            }
            else {
                Objects.requireNonNull(vindicator.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(36);
                Objects.requireNonNull(vindicator.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(7.5);
            }
            vindicator.setHealth(48f);
        }

        if (event.getEntity() instanceof Witch witch) {
            if (data.get()) {
                Objects.requireNonNull(witch.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(38);
                Objects.requireNonNull(witch.getAttribute(Attributes.ARMOR)).setBaseValue(1.5);
            }
            else
                Objects.requireNonNull(witch.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(32);
            witch.setHealth(38f);
        }

        if (event.getEntity() instanceof LargeFireball fireball && data.get() && fireball.getOwner() instanceof Ghast) {
            CompoundTag nbt = fireball.serializeNBT();
            nbt.putByte("ExplosionPower", (byte) 2);  // larger explosions in endgame mode
            fireball.deserializeNBT(nbt);
        }

        if (event.getEntity() instanceof Arrow arrow && !event.getLevel().isClientSide() && !((event.getEntity() instanceof LightningArrow) || (event.getEntity() instanceof DragonArrow) || (event.getEntity() instanceof WitherArrow))) {
            Entity owner1 = arrow.getOwner();
            // Converts arrows to lightning arrows if it is storming in the level.
            if (owner1 instanceof LivingEntity owner && owner.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.COPPER_HELMET.get() && owner.getItemBySlot(EquipmentSlot.CHEST).getItem() == ModItems.COPPER_CHESTPLATE.get() && owner.getItemBySlot(EquipmentSlot.LEGS).getItem() == ModItems.COPPER_LEGGINGS.get() && owner.getItemBySlot(EquipmentSlot.FEET).getItem() == ModItems.COPPER_BOOTS.get() && event.getLevel().isThundering()) {
                LightningArrow lightningArrow = new LightningArrow(arrow.level(), owner);
                lightningArrow.setPos(arrow.getX(), arrow.getY(), arrow.getZ());
                int ticks = owner.getTicksUsingItem();
                if (ticks > 22) {
                    ticks = 22;
                }
                lightningArrow.setDeltaMovement(arrow.getDeltaMovement());
                if (ticks == 22) {
                    lightningArrow.setCritArrow(true);
                }
                int j = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.POWER_ARROWS, owner.getUseItem());
                if (j > 0) {
                    lightningArrow.setBaseDamage(lightningArrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
                }

                int k = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PUNCH_ARROWS, owner.getUseItem());
                if (k > 0) {
                    lightningArrow.setKnockback(k);
                }

                if (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.FLAMING_ARROWS, owner.getUseItem()) > 0) {
                    lightningArrow.setSecondsOnFire(100);
                }
                arrow.teleportTo(0, -255, 0);
                arrow.setPos(0, -255, 0);
                arrow.discard();
                event.getLevel().addFreshEntity(lightningArrow);
            }
        }

        if (event.getEntity() instanceof PrimedTnt tnt && tnt.level().dimension() == Level.NETHER) {
            tnt.setFuse(0);  // instant tnt explosion
        }
    }

    @SubscribeEvent
    public static void onEatenFood(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            boolean currentlySick = false;
            Collection<MobEffectInstance> checkEffects = player.getActiveEffects();
            for (MobEffectInstance instance : checkEffects) {
                if (instance.getEffect() == ModEffects.GAPPLE_SICKNESS.get())
                    currentlySick = true;
            }
            if (event.getItem().getItem().equals(Items.ROTTEN_FLESH)) {
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 2));
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 600, 1));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0));
                player.displayClientMessage(Component.literal("Maybe I shouldn't have eaten that...").withStyle(ChatFormatting.RED), true);
            }
            if ((event.getItem().getItem().equals(Items.GOLDEN_APPLE) || event.getItem().getItem().equals(Items.ENCHANTED_GOLDEN_APPLE)) && currentlySick) {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 900, 9));
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1200, 2));
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 2400));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 4));
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 600, 4));
                player.addEffect(new MobEffectInstance(MobEffects.HARM));  // Punish player for eating more
                if (event.getItem().getItem().equals(Items.ENCHANTED_GOLDEN_APPLE))
                    player.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 9));  // Enchanted Golden Apples will at least full-heal the player
                player.displayClientMessage(Component.literal("I knew eating more gold would be a bad idea...").withStyle(ChatFormatting.RED), true);
            }
            else if (event.getItem().getItem().equals(Items.GOLDEN_APPLE) || event.getItem().getItem().equals(Items.ENCHANTED_GOLDEN_APPLE)) {
                if (event.getItem().getItem().equals(Items.ENCHANTED_GOLDEN_APPLE))
                    player.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 9));  // Enchanted Golden Apples will at least full-heal the player
                player.addEffect(new MobEffectInstance(ModEffects.GAPPLE_SICKNESS.get(), 2400));
                player.displayClientMessage(Component.literal("I probably shouldn't eat any gold too soon after that...").withStyle(ChatFormatting.YELLOW), true);
            }
        }
    }

    @SubscribeEvent
    public static void ridingEntityTick(LivingEvent.LivingTickEvent event) {
        if (!event.getEntity().isPassenger()) {
            if (event.getEntity() instanceof Zombie || event.getEntity() instanceof AbstractSkeleton) {
                List<Entity> nearbyEntities = event.getEntity().level().getEntities(event.getEntity(), event.getEntity().getBoundingBox().inflate(1.0));
                if (event.getEntity() instanceof AbstractSkeleton skeleton) {
                    for (Entity entity : nearbyEntities) {
                        if (entity.getPassengers().isEmpty() && (/*entity instanceof AbstractHorse || entity instanceof Pig ||*/ entity instanceof Chicken || entity instanceof Cow || entity instanceof Fox || entity instanceof Ocelot || entity instanceof Rabbit || entity instanceof Sheep || entity instanceof Turtle || entity instanceof AbstractVillager || entity instanceof Dolphin || entity instanceof Goat || entity instanceof PolarBear || entity instanceof Spider || entity instanceof Bat)) {
                            skeleton.startRiding(entity);
                            // Entities controlling horses/pigs is broken in 1.19.4. Will restore when porting to 1.20.1
                            if (entity instanceof AbstractHorse horse)
                                horse.setTamed(true);
                            break;
                        }
                    }
                }
                if (event.getEntity() instanceof Zombie zombie) {
                    for (Entity entity : nearbyEntities) {
                        if (entity.getPassengers().isEmpty() && (/*entity instanceof AbstractHorse || entity instanceof Pig ||*/ entity instanceof Chicken || entity instanceof Cow || entity instanceof Fox || entity instanceof Ocelot || entity instanceof Rabbit || entity instanceof Sheep || entity instanceof Turtle || entity instanceof AbstractVillager || entity instanceof Dolphin || entity instanceof Goat || entity instanceof PolarBear)) {
                            zombie.startRiding(entity);
                            // Entities controlling horses/pigs is broken in 1.19.4. Will restore when porting to 1.20.1
                            if (entity instanceof AbstractHorse horse)
                                horse.setTamed(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void zombieBlockTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Zombie zombie) {
            if (zombie.level().getBlockState(new BlockPos(zombie.getBlockX(), zombie.getBlockY() + 1, zombie.getBlockZ())).getBlock() == Blocks.AIR) {
                zombie.addEffect(new MobEffectInstance(MobEffects.GLOWING));
            }
        }
    }

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        Random rand = new Random();
        EndgameSaveData data;
        if (!event.getEntity().level().isClientSide) {
            data = EndgameSaveData.manage(Objects.requireNonNull(event.getEntity().level().getServer()));

            if (event.getEntity() instanceof ZombieVillager zombieVillager) {  // moving equipment event here because the on join level event is broken with professions
                if (zombieVillager.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.AIR) {
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.ARMORER)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.BUTCHER)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.CARTOGRAPHER)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.CLERIC)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.FARMER)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CARVED_PUMPKIN));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.FISHERMAN)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.BARREL));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.FLETCHER)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.LIBRARIAN)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.MASON)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.LEATHERWORKER)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.NITWIT)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIRT, 1));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.SHEPHERD)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.WHITE_WOOL, 1));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.TOOLSMITH)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.WEAPONSMITH)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.NONE)
                        zombieVillager.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.OAK_LEAVES, 1));
                }
                if (zombieVillager.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.AIR) {
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.ARMORER)
                        zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.BUTCHER)
                        zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.CARTOGRAPHER) {
                        ItemStack sharpPaper = new ItemStack(Items.PAPER, 1);
                        sharpPaper.enchant(Enchantments.SHARPNESS, 5);
                        zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, sharpPaper);
                    }
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.CLERIC) {
                        int staff = rand.nextInt(3);
                        if (staff == 0) {
                            ItemStack fireStaff = new ItemStack(ModItems.FIRE_STAFF.get());  // create new item
                            fireStaff.enchant(Enchantments.FIRE_ASPECT, 2); // enchant created item
                            zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, fireStaff);
                        }
                        if (staff == 1)
                            zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.ICE_STAFF.get()));
                        if (staff == 2)
                            zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.LIGHTNING_STAFF.get()));
                    }
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.FARMER)
                        zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.SCYTHE.get()));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.FISHERMAN)
                        zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.SPEAR.get()));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.FLETCHER) {
                        ItemStack arrow = new ItemStack(Items.ARROW, 1);
                        arrow.enchant(Enchantments.SHARPNESS, 5);
                        zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, arrow);
                    }
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.LEATHERWORKER)
                        zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.KNIFE.get()));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.LIBRARIAN) {
                        ItemStack sharpBook = new ItemStack(Items.BOOK, 1);  // create a new item
                        sharpBook.enchant(Enchantments.SHARPNESS, 5);  // enchant the created item
                        sharpBook.enchant(Enchantments.FIRE_ASPECT, 2);  // enchant the created item
                        sharpBook.enchant(Enchantments.KNOCKBACK, 1);  // enchant the created item
                        zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, sharpBook);  // set equipment for librarian z.villager
                    }
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.MASON)
                        zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.KNIFE.get()));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.NITWIT)
                        zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STICK, 1));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.SHEPHERD)
                        zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.TOOLSMITH)
                        zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_PICKAXE));
                    if (zombieVillager.getVillagerData().getProfession() == VillagerProfession.WEAPONSMITH)
                        zombieVillager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                }
            }
            if (event.getEntity() instanceof Husk husk) {
                if (husk.getBlockStateOn().getBlock() == Blocks.SAND || husk.getBlockStateOn().getBlock() == Blocks.SUSPICIOUS_SAND || husk.getBlockStateOn().getBlock() == Blocks.RED_SAND) {
                    husk.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, 2));  // Husks are faster on sand
                }
            }
            if (event.getEntity() instanceof Drowned drowned) {
                if (drowned.getBlockStateOn().getBlock() == Blocks.WATER)
                    drowned.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, 1));
            }
            if (event.getEntity() instanceof WitherBoss wither) {
                if (!wither.addTag("phase2")) {  // only applies to phase 2
                    if (wither.tickCount % 200 == 0) {  // every 10 seconds (20 ticks * 10 seconds = 200 ticks)
                        WitherSkullBarrier skullBarrier = new WitherSkullBarrier(ModEntityType.SKULL_BARRIER.get(), wither.level());
                        skullBarrier.teleportTo(wither.getX() + 3 * (Math.pow(-1, rand.nextInt(2))), wither.getY() + 1, wither.getZ() + 3 * (Math.pow(-1, rand.nextInt(2)))); // randomly spawn?
                        skullBarrier.setPos(wither.getX() + 3, wither.getY() + 1, wither.getZ() + 3);
                        skullBarrier.setOwner(wither);
                        wither.level().addFreshEntity(skullBarrier);
                    }
                } else
                    wither.removeTag("phase2");
            }
            if (event.getEntity() instanceof Creeper creeper && creeper.isOnFire()) {
                creeper.ignite();
            }

            if (event.getEntity() instanceof Creeper creeper && rand.nextInt(100) == rand.nextInt(100)) {
                List<Entity> entities = creeper.level().getEntities(creeper, creeper.getBoundingBox().inflate(16.0D, 16.0D, 16.0D));  // I am assuming this will randomly select a villager
                for (Entity entity : entities) {
                    if (entity instanceof Villager villager) {
                        creeper.setTarget(villager);
                        break;
                    }
                }
            }

            if (event.getEntity() instanceof EnderMan enderMan) {
                List<Entity> entities = enderMan.level().getEntities(enderMan, enderMan.getBoundingBox().inflate(5.0D, 5.0D, 5.0D));
                for (Entity entity : entities) {
                    if (entity instanceof ServerPlayer player) {
                        player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 40));  // 2 secs
                    }
                }
            }

            if (event.getEntity() instanceof Spider spider) {
                spider.addEffect(new MobEffectInstance(MobEffects.JUMP, 1, 2));  // jump boost
            }

            if (event.getEntity() instanceof Skeleton skeleton) {
                boolean nearbyPlayer = false;
                List<Entity> entities = skeleton.level().getEntities(skeleton, skeleton.getBoundingBox().inflate(5.0D, 5.0D, 5.0D));
                for (Entity entity : entities) {
                    if (entity instanceof ServerPlayer player && !(player.isCreative() || player.isSpectator())) {
                        nearbyPlayer = true;
                        break;
                    }
                }
                if (nearbyPlayer && skeleton.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.BOW) {
                    if (data.get()) {
                        ItemStack sword = new ItemStack(Items.IRON_SWORD);
                        sword.enchant(Enchantments.KNOCKBACK, 2);
                        skeleton.setItemInHand(InteractionHand.MAIN_HAND, sword);
                    } else {
                        ItemStack sword = new ItemStack(Items.STONE_SWORD);
                        sword.enchant(Enchantments.KNOCKBACK, 2);
                        skeleton.setItemInHand(InteractionHand.MAIN_HAND, sword);
                    }
                } else if (!nearbyPlayer && ((skeleton.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.STONE_SWORD) || (skeleton.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.IRON_SWORD))) {
                    if (data.get()) {
                        ItemStack bow = new ItemStack(Items.BOW);
                        bow.enchant(Enchantments.PUNCH_ARROWS, 2);
                        bow.enchant(Enchantments.POWER_ARROWS, 4);
                        skeleton.setItemInHand(InteractionHand.MAIN_HAND, bow);
                    } else {
                        ItemStack bow = new ItemStack(Items.BOW);
                        bow.enchant(Enchantments.PUNCH_ARROWS, 2);
                        skeleton.setItemInHand(InteractionHand.MAIN_HAND, bow);
                    }
                }
            }

            if (event.getEntity() instanceof WitherSkeleton witherSkeleton) {
                boolean nearbyPlayer = false;
                List<Entity> entities = witherSkeleton.level().getEntities(witherSkeleton, witherSkeleton.getBoundingBox().inflate(5.0D, 5.0D, 5.0D));
                for (Entity entity : entities) {
                    if (entity instanceof ServerPlayer player && !(player.isCreative() || player.isSpectator())) {
                        nearbyPlayer = true;
                        break;
                    }
                }
                if (nearbyPlayer && witherSkeleton.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.BOW) {
                    ItemStack axe = new ItemStack(Items.STONE_AXE);
                    axe.enchant(Enchantments.FIRE_ASPECT, 2);
                    witherSkeleton.setItemInHand(InteractionHand.MAIN_HAND, axe);
                } else if (!nearbyPlayer && witherSkeleton.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.STONE_AXE) {
                    ItemStack bow = new ItemStack(Items.BOW);
                    if (data.get()) {
                        bow.enchant(Enchantments.POWER_ARROWS, 4);
                        witherSkeleton.setItemInHand(InteractionHand.MAIN_HAND, bow);
                    } else {
                        bow.enchant(Enchantments.POWER_ARROWS, 2);
                        witherSkeleton.setItemInHand(InteractionHand.MAIN_HAND, bow);
                    }
                }
            }

            if (event.getEntity() instanceof Guardian guardian) {
                if (guardian.tickCount % 10 == 0) {
                    List<Entity> entities = guardian.level().getEntities(guardian, guardian.getBoundingBox().inflate(1.0D, 1.0D, 1.0D));
                    if (guardian instanceof ElderGuardian) {
                        for (Entity entity : entities) {
                            if (entity instanceof ServerPlayer player) {
                                if (data.get())
                                    player.hurt(guardian.damageSources().indirectMagic(guardian, guardian), 12.0f);
                                else
                                    player.hurt(guardian.damageSources().indirectMagic(guardian, guardian), 7.0f);
                            }
                        }
                    }
                    else {
                        for (Entity entity : entities) {
                            if (entity instanceof ServerPlayer player) {
                                if (data.get())
                                    player.hurt(guardian.damageSources().indirectMagic(guardian, guardian), 8.0f);
                                else
                                    player.hurt(guardian.damageSources().indirectMagic(guardian, guardian), 5.0f);
                            }
                        }
                    }
                }
            }

            if (event.getEntity() instanceof ElderGuardian elderGuardian) {
                if (elderGuardian.tickCount % 200 == 0) {
                    ServerPlayer player = (ServerPlayer) elderGuardian.level().getNearestPlayer(elderGuardian, 10.0);
                    if (player != null) {
                        ItemStack gildedTrident = new ItemStack(ModItems.GILDED_TRIDENT.get());
                        gildedTrident.enchant(Enchantments.CHANNELING, 1);
                        gildedTrident.enchant(Enchantments.IMPALING, 2);
                        ThrownGildedTrident trident = new ThrownGildedTrident(elderGuardian.level(), elderGuardian, gildedTrident);
                        trident.setPos(elderGuardian.getX(), elderGuardian.getY(), elderGuardian.getZ());
                        trident.teleportTo(elderGuardian.getX(), elderGuardian.getY(), elderGuardian.getZ());
                        double d0 = player.getX() - elderGuardian.getX();
                        double d1 = player.getY(0.3333333333333333D) - trident.getY();
                        double d2 = player.getZ() - elderGuardian.getZ();
                        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                        trident.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - elderGuardian.level().getDifficulty().getId() * 4));
                        elderGuardian.playSound(SoundEvents.TRIDENT_THROW, 1.0F, 1.0F / (elderGuardian.getRandom().nextFloat() * 0.4F + 0.8F));
                        elderGuardian.level().addFreshEntity(trident);
                    }
                }
            }

            if (event.getEntity() instanceof EnderDragon dragon && dragon.getHealth() > 0f) {
                boolean noCrystals = true;  // checking for crystals
                Random randPlayer = new Random();
                List<? extends Player> players = dragon.level().players();
                List<Player> survivalPlayers = new ArrayList<>();
                if (!players.isEmpty()) {
                    for (Player player : players) {
                        if (!player.isCreative() && !player.isSpectator()) {
                            survivalPlayers.add(player);
                        }
                    }
                }
                List<Entity> nearbyEntities = dragon.level().getEntities(dragon, dragon.getBoundingBox().inflate(200f));
                for (Entity entity : nearbyEntities) {
                    if (entity instanceof EndCrystal) {  // checking for crystals
                        noCrystals = false;
                        break;
                    }
                }
                List<? extends Player> nearbyPlayers = survivalPlayers.stream().distinct().filter(nearbyEntities::contains).collect(Collectors.toSet()).stream().toList();  // intersection
                if (!nearbyPlayers.isEmpty()) {
                    ServerPlayer randomPlayer = (ServerPlayer) nearbyPlayers.get(randPlayer.nextInt(nearbyPlayers.size()));
                    if (dragon.tickCount % 200 == 0) {
                        Vec3 vec3 = dragon.getViewVector(1.0F);
                        double d2 = randomPlayer.getX() - (dragon.getX() + vec3.x * 16.0D);
                        double d3 = randomPlayer.getY(0.5D) - (dragon.getEyeY());
                        double d4 = randomPlayer.getZ() - (dragon.getZ() + vec3.z * 16.0D);
                        DragonFireball fireball = new DragonFireball(dragon.level(), dragon, d2, d3, d4);
                        fireball.setPos(dragon.getX() + vec3.x * 16.0D, dragon.getEyeY(), fireball.getZ() + vec3.z * 16.0D);
                        dragon.playSound(SoundEvents.ENDER_DRAGON_SHOOT, 1.0F, 1.0F / (dragon.getRandom().nextFloat() * 0.4F + 0.8F));
                        dragon.level().addFreshEntity(fireball);
                    } else if (dragon.tickCount % 200 == 40) {
                        Vec3 vec3 = dragon.getViewVector(1.0F);
                        double d2 = randomPlayer.getX() - (dragon.getX() + vec3.x * 16.0D);
                        double d3 = randomPlayer.getY(0.5D) - (dragon.getEyeY());
                        double d4 = randomPlayer.getZ() - (dragon.getZ() + vec3.z * 16.0D);
                        LargeFireball fireball = new LargeFireball(dragon.level(), dragon, d2, d3, d4, 2);
                        fireball.setPos(dragon.getX() + vec3.x * 16.0D, dragon.getEyeY(), fireball.getZ() + vec3.z * 16.0D);
                        dragon.playSound(SoundEvents.ENDER_DRAGON_SHOOT, 1.0F, 1.0F / (dragon.getRandom().nextFloat() * 0.4F + 0.8F));
                        dragon.level().addFreshEntity(fireball);
                    }
                    if (dragon.tickCount % 200 == 0 && noCrystals) {  // Shulker bullet on player
                        ShulkerBullet bullet = new ShulkerBullet(dragon.level(), dragon, randomPlayer, Direction.Axis.Y);
                        bullet.setPos(randomPlayer.getX(), randomPlayer.getY() + 10, randomPlayer.getZ());
                        bullet.setDeltaMovement(0.0, -2.5, 0.0);
                        bullet.playSound(SoundEvents.SHULKER_SHOOT);
                        dragon.level().addFreshEntity(bullet);
                    }
                    if (dragon.tickCount % 20 == 0 && noCrystals) {
                        Vec3 vec3 = dragon.getViewVector(1.0F);
                        Random randDirection = new Random();
                        DragonFireball fireball = new DragonFireball(dragon.level(), dragon, 0, 0, 0);
                        fireball.setPos(dragon.getX() + vec3.x * 16.0D, dragon.getEyeY(), fireball.getZ() + vec3.z * 16.0D);
                        fireball.setDeltaMovement(randDirection.nextDouble(-1.0, 1.0), randDirection.nextDouble(-10, -5), randDirection.nextDouble(-1.0, 1.0));
                        dragon.playSound(SoundEvents.ENDER_DRAGON_SHOOT, 1.0F, 1.0F / (dragon.getRandom().nextFloat() * 0.4F + 0.8F));
                        dragon.level().addFreshEntity(fireball);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLightningStrike(EntityStruckByLightningEvent event) {
        if (event.getEntity() instanceof LivingEntity entity && !(event.getLightning() instanceof ArrowBolt)) {  // No, lightning arrows will not heal you.
            if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.COPPER_HELMET.get() && entity.getItemBySlot(EquipmentSlot.CHEST).getItem() == ModItems.COPPER_CHESTPLATE.get() && entity.getItemBySlot(EquipmentSlot.LEGS).getItem() == ModItems.COPPER_LEGGINGS.get() && entity.getItemBySlot(EquipmentSlot.FEET).getItem() == ModItems.COPPER_BOOTS.get()) {
                // Convert electricity into energy!
                if (entity.isInvertedHealAndHarm()) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 1));
                    entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 400, 1));
                    entity.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));  // heals undead mobs
                }
                else {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 1));
                    entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 400, 1));
                    entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 1));  // heals non-undead mobs
                    entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 1));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GOLDEN_HELMET && player.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.GOLDEN_CHESTPLATE && player.getItemBySlot(EquipmentSlot.LEGS).getItem() == Items.GOLDEN_LEGGINGS && player.getItemBySlot(EquipmentSlot.FEET).getItem() == Items.GOLDEN_BOOTS) {
                Random rand = new Random();
                if (rand.nextInt(10) < 1) {
                    event.setAmount(0);  // 10% chance to negate damage
                    player.level().playSound(null, player.getOnPos(), ModSounds.GOLD_DAMAGE_NEGATE.get(), SoundSource.PLAYERS, 1.0f, 0.75f);
                }
            }
            player.getCapability(PlayerRageProvider.PLAYER_RAGE).ifPresent(rage -> {
                if (rage.getRage() != 10000 && !rage.getRageUse()) {
                    rage.addRage((int) (event.getAmount()) * 50);
                    ModMessages.sendToPlayer(new RageDataSyncS2CPacket(rage.getRage()), player);
                    if (rage.getRage() == 10000)
                        player.level().playSound(null, player.getOnPos(), ModSounds.RAGE_FILLED.get(), SoundSource.PLAYERS, 0.9f, 1.0f);
                }
            });
            player.getCapability(PlayerAdrenalineProvider.PLAYER_ADRENALINE).ifPresent(adrenaline -> {
                if (!adrenaline.getAdrenalineUse()) {
                    if (adrenaline.getAdrenaline() == 600)
                        player.level().playSound(null, player.getOnPos(), ModSounds.ADRENALINE_EMPTIED.get(), SoundSource.PLAYERS, 0.5f, 1.0f);
                    adrenaline.resetAdrenaline();
                    ModMessages.sendToPlayer(new AdrenalineDataSyncS2CPacket(adrenaline.getAdrenaline()), player);
                }
            });
            if (event.getSource().getEntity() instanceof Spider) {
                BlockPos playerPos = player.getOnPos();
                BlockPos cobwebPos = new BlockPos(playerPos.getX(), playerPos.getY() + 1, playerPos.getZ());
                player.level().setBlockAndUpdate(cobwebPos, Blocks.COBWEB.defaultBlockState());
            }
            if (event.getSource().getEntity() instanceof EnderMan) {
                Random random = new Random();
                BlockPos playerPos = player.getOnPos();
                playerPos = new BlockPos(playerPos.getX(), playerPos.getY() + 1, playerPos.getZ());
                double x_change = random.nextInt(-10, 11);
                double y_change = random.nextInt(-10, 11);
                double z_change = random.nextInt(-10, 11);
                double new_x = playerPos.getX() + x_change;
                double new_y = Math.max(playerPos.getY() + y_change, -63.0);
                double new_z = playerPos.getZ() + z_change;
                player.teleportTo(new_x, new_y, new_z);
                player.setPos(new_x, new_y, new_z);
                player.level().playSound(null, player.getOnPos(), ModSounds.PLAYER_TELEPORTED.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            }
            if (event.getSource().getEntity() instanceof PiglinBrute)
                player.setSecondsOnFire(16);  // molten axe for some reason doesn't work when a non-player hits with it
            if (event.getSource().getEntity() instanceof Zombie zombie && !(zombie instanceof Husk || zombie instanceof Drowned || zombie instanceof ZombifiedPiglin || zombie instanceof ZombieVillager))
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100));
            if (event.getSource().getEntity() instanceof ZombieVillager)
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));
            if (event.getSource().getEntity() instanceof Husk)
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100));
        }
        if (event.getEntity() instanceof Villager villager && event.getSource().getEntity() instanceof ZombieVillager)
            villager.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN));
    }

    @SubscribeEvent
    public static void onPlayerFall(LivingFallEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && player.getItemBySlot(EquipmentSlot.FEET).getItem() == ModItems.SLIME_BOOTS.get() && event.getDistance() > 3.0f) {
            event.setDamageMultiplier(0f);
            player.getItemBySlot(EquipmentSlot.FEET).hurtAndBreak(1, player, (p_276007_) -> p_276007_.broadcastBreakEvent(EquipmentSlot.FEET));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBossDeath(LivingDeathEvent event) {  // Adds drops to bosses, this method makes it compatible with other mods that make bosses drop items
        ItemStack mendingBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(mendingBook, new EnchantmentInstance(Enchantments.MENDING, 1));
        Random mendChance1 = new Random();
        Random mendChance2 = new Random();
        boolean dropMending = mendChance1.nextInt(10) == mendChance2.nextInt(10); // 1 in 10 chance
        EndgameSaveData data = new EndgameSaveData();
        if (!event.getEntity().level().isClientSide) {
            data = EndgameSaveData.manage(Objects.requireNonNull(event.getEntity().getServer()));
        }
        if (event.getEntity() instanceof WitherBoss wither && event.getSource().getEntity() instanceof ServerPlayer) {
            wither.spawnAtLocation(ModItems.WITHER_BOW.get());
            if (data.get())
                wither.spawnAtLocation(ModItems.WITHER_SKULL.get());
            if (dropMending)
                wither.spawnAtLocation(mendingBook);
        }
        if (event.getEntity() instanceof EnderDragon dragon && event.getSource().getEntity() instanceof ServerPlayer) {
            dragon.spawnAtLocation(ModItems.ENDER_DRAGON_HEAD.get());
            if (data.get())
                dragon.spawnAtLocation(ModItems.DRAGON_BOW.get());
            if (dropMending)
                dragon.spawnAtLocation(mendingBook);
        }
        if (event.getEntity() instanceof Guardian guardian && !(event.getEntity() instanceof ElderGuardian) && event.getSource().getEntity() instanceof ServerPlayer) {
            Random rand1 = new Random();
            Random rand2 = new Random();
            ItemStack pot = new ItemStack(Items.POTION);
            PotionUtils.setPotion(pot, ModPotions.HASTE.get());
            if (rand1.nextInt(10) == rand2.nextInt(10))
                guardian.spawnAtLocation(pot);
        }
        if (event.getEntity() instanceof ElderGuardian guardian && event.getSource().getEntity() instanceof ServerPlayer) {
            Random rand1 = new Random();
            Random rand2 = new Random();
            ItemStack pot = new ItemStack(Items.POTION);
            PotionUtils.setPotion(pot, ModPotions.STRONG_HASTE.get());
            guardian.spawnAtLocation(pot);
            if (dropMending)
                guardian.spawnAtLocation(mendingBook);
            if (rand1.nextInt(10) == rand2.nextInt(10)) {
                guardian.spawnAtLocation(ModItems.GILDED_TRIDENT.get());
            }
            else {
                if (rand1.nextInt(3) == rand2.nextInt(3))
                    guardian.spawnAtLocation(ModItems.FAKE_GILDED_TRIDENT.get());
            }
            if (data.get()) {
                if (rand1.nextInt(3) == rand2.nextInt(3)) {
                    ItemStack p_trident = new ItemStack(ModItems.POSEIDON_TRIDENT.get());
                    p_trident.enchant(Enchantments.LOYALTY, 3);
                    p_trident.enchant(Enchantments.UNBREAKING, 3);
                    guardian.spawnAtLocation(p_trident);
                }
            }
        }
        if (event.getEntity() instanceof Evoker evoker && event.getSource().getEntity() instanceof ServerPlayer) {
            if (data.get())
                evoker.spawnAtLocation(ModItems.VEX_SWORD.get());
            if (dropMending)
                evoker.spawnAtLocation(mendingBook);
        }
        if (event.getEntity() instanceof PiglinBrute brute && event.getSource().getEntity() instanceof ServerPlayer) {
            if (dropMending)
                brute.spawnAtLocation(mendingBook);
            if (data.get())
                brute.spawnAtLocation(ModItems.DAMNED_AXE.get());
        }

        if (event.getEntity() instanceof Warden warden && event.getSource().getEntity() instanceof ServerPlayer) {
            warden.spawnAtLocation(ModItems.JITTER_SHOTBOW.get());

            ItemStack fireworkStar = new ItemStack(Items.FIREWORK_STAR);
            CompoundTag starCompoundTag = fireworkStar.getOrCreateTagElement("Explosion");
            FireworkRocketItem.Shape fireworkrocketitem$shape = FireworkRocketItem.Shape.BURST;
            List<Integer> list = Lists.newArrayList();
            list.add(((DyeItem)Items.BLACK_DYE).getDyeColor().getFireworkColor());
            list.add(((DyeItem)Items.CYAN_DYE).getDyeColor().getFireworkColor());
            list.add(((DyeItem)Items.BLUE_DYE).getDyeColor().getFireworkColor());

            starCompoundTag.putIntArray("Colors", list);
            starCompoundTag.putByte("Type", (byte)fireworkrocketitem$shape.getId());
            Random randStack = new Random();
            ItemStack fireworks = new ItemStack(Items.FIREWORK_ROCKET, randStack.nextInt(50, 64));
            CompoundTag fireworksTag = fireworks.getOrCreateTagElement("Fireworks");
            ListTag listtag = new ListTag();
            CompoundTag explosionTag = fireworkStar.getOrCreateTagElement("Explosion");
            listtag.add(explosionTag);

            fireworksTag.putByte("Flight", (byte)5);
            fireworksTag.put("Explosions", listtag);
            warden.spawnAtLocation(fireworks);

            warden.spawnAtLocation(mendingBook);  // Always drops a mending book, superboss status
        }

        if (event.getEntity() instanceof Slime slime && event.getSource().getEntity() instanceof ServerPlayer && slime.getSize() == 10 && !(slime instanceof MagmaCube)) {
            if (dropMending)
                slime.spawnAtLocation(mendingBook);
            slime.spawnAtLocation(ModItems.SLIME_BOOTS.get());
        }

        if (event.getEntity() instanceof MagmaCube magmaCube && event.getSource().getEntity() instanceof ServerPlayer && magmaCube.getSize() == 10) {
            if (dropMending)
                magmaCube.spawnAtLocation(mendingBook);
            Random divingChance = new Random();
            List<ItemLike> armors = new ArrayList<>();
            armors.add(ModItems.LAVA_DIVING_HELMET.get());
            armors.add(ModItems.LAVA_DIVING_CHESTPLATE.get());
            armors.add(ModItems.LAVA_DIVING_LEGGINGS.get());
            armors.add(ModItems.LAVA_DIVING_BOOTS.get());
            int armorDropped = divingChance.nextInt(4);
            magmaCube.spawnAtLocation(armors.get(armorDropped));
        }

        if (event.getEntity() instanceof MagmaCube magmaCube && magmaCube.getSize() == 1 && !magmaCube.level().isClientSide) {
            BlockPos cubePos = magmaCube.getOnPos();
            BlockPos lavaPos = new BlockPos(cubePos.getX(), cubePos.getY() + 1, cubePos.getZ());
            // Spawn temporary lava
            magmaCube.level().setBlockAndUpdate(lavaPos, Blocks.LAVA.defaultBlockState().setValue(BlockStateProperties.LEVEL, 1));
        }
    }
}
