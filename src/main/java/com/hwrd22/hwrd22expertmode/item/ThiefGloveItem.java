package com.hwrd22.hwrd22expertmode.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThiefGloveItem extends Item {
    public ThiefGloveItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, @NotNull TooltipFlag p_41424_) {
        p_41423_.add(Component.literal("Allows you to steal a held item from an entity except for players.\n Don't ask questions.").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
