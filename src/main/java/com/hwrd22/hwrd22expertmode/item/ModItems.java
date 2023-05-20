package com.hwrd22.hwrd22expertmode.item;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // Rarities
    public static final Rarity ILLAGER = Rarity.create("illager", ChatFormatting.GREEN);
    public static final Rarity NETHER = Rarity.create("nether", ChatFormatting.DARK_RED);
    public static final Rarity WITHER = Rarity.create("wither", ChatFormatting.DARK_GRAY);
    public static final Rarity END = Rarity.create("end", ChatFormatting.DARK_PURPLE);
    public static final Rarity DEEP_DARK = Rarity.create("deep_dark", ChatFormatting.DARK_BLUE);
    public static final Rarity ENDGAME = Rarity.create("endgame", ChatFormatting.GOLD);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExpertMode.MODID);

    public static final RegistryObject<BowItem> WITHER_BOW = ITEMS.register("wither_bow", () -> new BowItem(new Item.Properties().defaultDurability(768).rarity(WITHER)));

    public static final RegistryObject<BowItem> DRAGON_BOW = ITEMS.register("dragon_bow", () -> new BowItem(new Item.Properties().defaultDurability(1152).rarity(END)));

    public static final RegistryObject<SwordItem> VEX_SWORD = ITEMS.register("vex_sword", () -> new SwordItem(new Tier() {
        @Override
        public int getUses() {
            return 3124;
        }

        @Override
        public float getSpeed() {
            return -2.25f;
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
        public Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(Items.EMERALD_BLOCK));
        }
    }, 0, -2.25f, new Item.Properties().rarity(ILLAGER)));

    public static final RegistryObject<SpearItem> SPEAR = ITEMS.register("spear", () -> new SpearItem(new Item.Properties().defaultDurability(250)));

    public static final RegistryObject<HoeItem> SCYTHE = ITEMS.register("scythe", () -> new HoeItem(new Tier() {
        @Override
        public int getUses() {
            return 500;
        }

        @Override
        public float getSpeed() {
            return -1.0f;
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
        public Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(Items.IRON_INGOT));
        }
    }, 0, -1.0f, new Item.Properties()));
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
