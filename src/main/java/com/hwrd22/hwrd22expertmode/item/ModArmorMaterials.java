package com.hwrd22.hwrd22expertmode.item;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record ModArmorMaterials(String name, int durability, int[] protection, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) implements ArmorMaterial {
    private static final int[] DURABILITY_PER_SLOT = { 13, 15, 16, 11 };

    @Override
    public int getDurabilityForType(ArmorItem.Type armorType) {
        return this.durability * DURABILITY_PER_SLOT[armorType.getSlot().getIndex()];
    }

    @Override
    public int getDefenseForType(ArmorItem.Type armorType) {
        return this.protection[armorType.getSlot().getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public @NotNull SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    @Override
    public @NotNull String getName() {
        return ExpertMode.MODID + ":" +  this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
