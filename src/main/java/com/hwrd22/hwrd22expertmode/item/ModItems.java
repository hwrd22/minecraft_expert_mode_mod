package com.hwrd22.hwrd22expertmode.item;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ModItems {
    // Rarities
    public static final Rarity ILLAGER = Rarity.create("illager", ChatFormatting.GREEN);
    public static final Rarity NETHER = Rarity.create("nether", ChatFormatting.DARK_RED);
    public static final Rarity GUARDIAN = Rarity.create("guardian", ChatFormatting.BLUE);
    public static final Rarity WITHER = Rarity.create("wither", ChatFormatting.DARK_GRAY);
    public static final Rarity END = Rarity.create("end", ChatFormatting.DARK_PURPLE);
    public static final Rarity DEEP_DARK = Rarity.create("deep_dark", ChatFormatting.DARK_BLUE);
    public static final Rarity ENDGAME = Rarity.create("endgame", ChatFormatting.GOLD);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExpertMode.MODID);

    public static final RegistryObject<BlazeBowItem> BLAZE_BOW = ITEMS.register("blaze_bow", () -> new BlazeBowItem(new Item.Properties().defaultDurability(384).fireResistant()));
    public static final RegistryObject<WitherBowItem> WITHER_BOW = ITEMS.register("wither_bow", () -> new WitherBowItem(new Item.Properties().defaultDurability(768).rarity(WITHER).fireResistant()));

    public static final RegistryObject<DragonBowItem> DRAGON_BOW = ITEMS.register("dragon_bow", () -> new DragonBowItem(new Item.Properties().defaultDurability(1152).rarity(ENDGAME).fireResistant()));

    public static final RegistryObject<SwordItem> VEX_SWORD = ITEMS.register("vex_sword", () -> new VexSwordItem(new Tier() {
        @Override
        public int getUses() {
            return 3124;
        }

        @Override
        public float getSpeed() {
            return 8.0f;
        }

        @Override
        public float getAttackDamageBonus() {
            return 6;
        }

        @Override
        public int getLevel() {
            return 4;
        }

        @Override
        public int getEnchantmentValue() {
            return 20;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(Items.EMERALD_BLOCK));
        }
    }, 0, -2.25f, new Item.Properties().rarity(ILLAGER).fireResistant()));

    public static final RegistryObject<SpearItem> SPEAR = ITEMS.register("spear", () -> new SpearItem(new Item.Properties().defaultDurability(250)));

    public static final RegistryObject<HoeItem> SCYTHE = ITEMS.register("scythe", () -> new HoeItem(new Tier() {
        @Override
        public int getUses() {
            return 500;
        }

        @Override
        public float getSpeed() {
            return 6.0f;
        }

        @Override
        public float getAttackDamageBonus() {
            return 3;
        }

        @Override
        public int getLevel() {
            return 2;
        }

        @Override
        public int getEnchantmentValue() {
            return 17;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(Items.IRON_INGOT));
        }
    }, 0, -1.0f, new Item.Properties()));

    public static final RegistryObject<SwordItem> KNIFE = ITEMS.register("knife", () -> new SwordItem(new Tier() {

        @Override
        public int getUses() {
            return 251;
        }

        @Override
        public float getSpeed() {
            return 6.0f;
        }

        @Override
        public float getAttackDamageBonus() {
            return 2;
        }

        @Override
        public int getLevel() {
            return 2;
        }

        @Override
        public int getEnchantmentValue() {
            return 14;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(Items.IRON_INGOT));
        }
    }, 0, -1.0f, new Item.Properties()));

    public static final RegistryObject<MoltenAxeItem> MOLTEN_AXE = ITEMS.register("molten_axe", () -> new MoltenAxeItem(new Tier() {
        @Override
        public int getUses() {
            return 64;
        }

        @Override
        public float getSpeed() {
            return 12.0f;
        }

        @Override
        public float getAttackDamageBonus() {
            return 6;
        }

        @Override
        public int getLevel() {
            return 0;
        }

        @Override
        public int getEnchantmentValue() {
            return 22;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(Items.GOLD_INGOT);
        }
    }, 0, -3.0f, new Item.Properties().rarity(ModItems.NETHER).fireResistant()));

    public static final RegistryObject<DamnedAxeItem> DAMNED_AXE = ITEMS.register("damned_axe", () -> new DamnedAxeItem(new Tier() {
        @Override
        public int getUses() {
            return 2031;
        }

        @Override
        public float getSpeed() {
            return 9.0f;
        }

        @Override
        public float getAttackDamageBonus() {
            return 12.5f;
        }

        @Override
        public int getLevel() {
            return 4;
        }

        @Override
        public int getEnchantmentValue() {
            return 12;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(Items.NETHERITE_INGOT);
        }
    }, 0, -3.0f, new Item.Properties().rarity(ModItems.ENDGAME).fireResistant()));

    public static final RegistryObject<ArmorItem> COPPER_HELMET = ITEMS.register("copper_helmet", () -> new ArmorItem(ArmorTiers.COPPER, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<ArmorItem> COPPER_CHESTPLATE = ITEMS.register("copper_chestplate", () -> new ArmorItem(ArmorTiers.COPPER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<ArmorItem> COPPER_LEGGINGS = ITEMS.register("copper_leggings", () -> new ArmorItem(ArmorTiers.COPPER, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<ArmorItem> COPPER_BOOTS = ITEMS.register("copper_boots", () -> new ArmorItem(ArmorTiers.COPPER, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<ArmorItem> LAVA_DIVING_HELMET = ITEMS.register("lava_diving_helmet", () -> new ArmorItem(ArmorTiers.LAVA_DIVING, ArmorItem.Type.HELMET, new Item.Properties().fireResistant()));
    public static final RegistryObject<ArmorItem> LAVA_DIVING_CHESTPLATE = ITEMS.register("lava_diving_chestplate", () -> new ArmorItem(ArmorTiers.LAVA_DIVING, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant()));
    public static final RegistryObject<ArmorItem> LAVA_DIVING_LEGGINGS = ITEMS.register("lava_diving_leggings", () -> new ArmorItem(ArmorTiers.LAVA_DIVING, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant()));
    public static final RegistryObject<ArmorItem> LAVA_DIVING_BOOTS = ITEMS.register("lava_diving_boots", () -> new ArmorItem(ArmorTiers.LAVA_DIVING, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant()));
    public static final RegistryObject<SlimeBootsItem> SLIME_BOOTS = ITEMS.register("slime_boots", () -> new SlimeBootsItem(ArmorTiers.SLIME, ArmorItem.Type.BOOTS, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ParadoxBootsItem> PARADOX_BOOTS = ITEMS.register("paradox_boots", () -> new ParadoxBootsItem(ArmorMaterials.CHAIN, ArmorItem.Type.BOOTS, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ArmorItem> BRUTE_CHESTPLATE = ITEMS.register("brute_chestplate", () -> new ArmorItem(ArmorMaterials.NETHERITE, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(NETHER).fireResistant()));
    public static final RegistryObject<ArmorItem> ENDER_DRAGON_HEAD = ITEMS.register("ender_dragon_head", () -> new EnderDragonHead(ArmorTiers.DRAGON, ArmorItem.Type.HELMET, new Item.Properties().rarity(END).fireResistant()));
    public static final RegistryObject<ArmorItem> WITHER_SKULL = ITEMS.register("wither_skull", () -> new WitherSkull(ArmorTiers.WITHER, ArmorItem.Type.HELMET, new Item.Properties().rarity(ENDGAME).fireResistant()));
    public static final RegistryObject<GildedTridentItem> GILDED_TRIDENT = ITEMS.register("gilded_trident", () -> new GildedTridentItem(new Item.Properties().durability(375).rarity(GUARDIAN).fireResistant()));
    public static final RegistryObject<FakeGildedTridentItem> FAKE_GILDED_TRIDENT = ITEMS.register("fake_gilded_trident", () -> new FakeGildedTridentItem(new Item.Properties().durability(250)));
    public static final RegistryObject<PoseidonTridentItem> POSEIDON_TRIDENT = ITEMS.register("poseidon_trident", () -> new PoseidonTridentItem(new Item.Properties().durability(500).rarity(ENDGAME).fireResistant()));
    public static final RegistryObject<JitterShotbowItem> JITTER_SHOTBOW = ITEMS.register("jitter_shotbow", () -> new JitterShotbowItem(new Item.Properties().durability(930).rarity(DEEP_DARK).fireResistant()));

    public static class ArmorTiers {
        static final int[] copper_armor_points = {1, 4, 5, 1};
        public static final ArmorMaterial COPPER = new ModArmorMaterials("copper", 13, copper_armor_points, 10, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f, new Supplier<Ingredient>() {
            @Override
            public Ingredient get() {
                return Ingredient.of(Items.COPPER_INGOT);
            }
        });
        static final int[] lava_diving_armor_points = {2, 4, 5, 2};
        public static final ArmorMaterial LAVA_DIVING = new ModArmorMaterials("lava_diving", 25, lava_diving_armor_points, 8, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.1f, new Supplier<Ingredient>() {
            @Override
            public Ingredient get() {
                return Ingredient.of(Items.OBSIDIAN);
            }
        });

        static final int[] dragon_armor_point = {0, 0, 0, 4};
        public static final ArmorMaterial DRAGON = new ModArmorMaterials("dragon", 100, dragon_armor_point, 10, SoundEvents.ARMOR_EQUIP_GENERIC, 4.0f, 0.2f, new Supplier<Ingredient>() {
            @Override
            public Ingredient get() {
                return Ingredient.of(Items.DRAGON_BREATH);
            }
        });

        static final int[] wither_armor_point = {0, 0, 0, 5};
        public static final ArmorMaterial WITHER = new ModArmorMaterials("wither", 150, wither_armor_point, 15, SoundEvents.ARMOR_EQUIP_GENERIC, 4.0f, 0.4f, new Supplier<Ingredient>() {
            @Override
            public Ingredient get() {
                return Ingredient.of(Items.WITHER_SKELETON_SKULL);
            }
        });

        static final int[] slime_armor_point = {0, 0, 0, 0};  // only purpose is to stop fall damage
        public static final ArmorMaterial SLIME = new ModArmorMaterials("slime", 1, slime_armor_point, 1, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0f, 0.0f, new Supplier<Ingredient>() {
            @Override
            public Ingredient get() {
                return Ingredient.of(Items.SLIME_BALL);
            }
        });
    }

    public static final RegistryObject<FireStaffItem> FIRE_STAFF = ITEMS.register("fire_staff", () -> new FireStaffItem(new Item.Properties().defaultDurability(32).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<IceStaffItem> ICE_STAFF = ITEMS.register("ice_staff", () -> new IceStaffItem(new Item.Properties().defaultDurability(32).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<LightningStaffItem> LIGHTNING_STAFF = ITEMS.register("lightning_staff", () -> new LightningStaffItem(new Item.Properties().defaultDurability(32).rarity(Rarity.UNCOMMON)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
