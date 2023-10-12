package com.hwrd22.hwrd22expertmode.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.hwrd22.hwrd22expertmode.client.renderer.models.EnderDragonHeadModel;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

import static net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH;

public class EnderDragonHead extends ArmorItem {
    private static final EnumMap<Type, UUID> ARMOR_MODIFIER_UUID_PER_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266744_) -> {
        p_266744_.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        p_266744_.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        p_266744_.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        p_266744_.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    public EnderDragonHead(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
        super(p_40386_, p_266831_, p_40388_);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIER_UUID_PER_TYPE.get(p_266831_);
        builder.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Health modifier", 20.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", 4.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", 4.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", 0.2, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_40390_) {
        return p_40390_ == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_40390_);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (!world.isClientSide()) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1));
        }
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new ModelSupplier());
    }
    static class ModelSupplier implements IClientItemExtensions {
        public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
            return new EnderDragonHeadModel(Minecraft.getInstance().getEntityModels().bakeLayer(EnderDragonHeadModel.LAYER_LOCATION));
        }
        public ResourceLocation getArmorTexture() {
            return new ResourceLocation("minecraft:textures/entity/enderdragon/dragon.png");
        }
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "minecraft:textures/entity/enderdragon/dragon.png";
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.literal("The head of the Ender Dragon. Wearing this increases max health and provides strength.\nCounts as a Diamond or Netherite Helmet for the set bonuses.").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
