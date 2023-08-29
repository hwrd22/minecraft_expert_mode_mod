# Warden
execute as @e[type=warden,scores={Health=..256}] at @s run playsound minecraft:entity.evoker.prepare_summon master @a ~ ~ ~
execute as @e[type=warden,scores={Health=..256}] at @s run particle minecraft:soul_fire_flame ~-1 ~ ~-1 1 1 1 1 200
execute as @e[type=warden,scores={Health=..256}] at @s run summon minecraft:skeleton ~-1 ~ ~-1 {Silent:1,Tags:["Minion"]}
execute as @e[type=warden,scores={Health=..256}] at @s run particle minecraft:soul_fire_flame ~1 ~ ~-1 1 1 1 1 200
execute as @e[type=warden,scores={Health=..256}] at @s run summon minecraft:skeleton ~1 ~ ~-1 {Silent:1,Tags:["Minion"]}
execute as @e[type=warden,scores={Health=..256}] at @s run particle minecraft:soul_fire_flame ~1 ~ ~1 1 1 1 1 200
execute as @e[type=warden,scores={Health=..256}] at @s run summon minecraft:skeleton ~1 ~ ~1 {Silent:1,Tags:["Minion"]}
execute as @e[type=warden,scores={Health=..256}] at @s run particle minecraft:soul_fire_flame ~1 ~ ~-1 1 1 1 1 200
execute as @e[type=warden,scores={Health=..256}] at @s run summon minecraft:skeleton ~-1 ~ ~1 {Silent:1,Tags:["Minion"]}
scoreboard players add @e[type=minecraft:skeleton,tag=Minion] buff 0
team join enrageable @e[type=minecraft:skeleton,scores={buff=0},tag=Minion]
item replace entity @e[type=minecraft:skeleton,scores={buff=0},tag=Minion] weapon.mainhand with bow{Enchantments:[{id:flame,lvl:1},{id:power,lvl:5},{id:punch,lvl:2}]}
item replace entity @e[type=minecraft:skeleton,scores={buff=0},tag=Minion] weapon.offhand with tipped_arrow{CustomPotionColor:283182,display:{Name:'[{"text":"Deep Dark Arrow","italic":false,"color":"#003333","bold":true}]',Lore:['[{"text":"Not very effective against","italic":false}]','[{"text":"undead mobs.","italic":false}]']},CustomPotionEffects:[{Id:15,Duration:400,Amplifier:4},{Id:17,Duration:200,Amplifier:4},{Id:7,Duration:1,Amplifier:1},{Id:9,Duration:200},{Id:19,Duration:200,Amplifier:4},{Id:2,Duration:200,Amplifier:1},{Id:18,Duration:200,Amplifier:1},{Id:20,Duration:200,Amplifier:4}]} 50
item replace entity @e[type=minecraft:skeleton,scores={buff=0},tag=Minion] armor.head with diamond_helmet{Enchantments:[{id:protection,lvl:4},{id:thorns,lvl:3}]}
item replace entity @e[type=minecraft:skeleton,scores={buff=0},tag=Minion] armor.chest with diamond_chestplate{Enchantments:[{id:protection,lvl:4},{id:thorns,lvl:3}]}
item replace entity @e[type=minecraft:skeleton,scores={buff=0},tag=Minion] armor.legs with diamond_leggings{Enchantments:[{id:protection,lvl:4},{id:thorns,lvl:3}]}
item replace entity @e[type=minecraft:skeleton,scores={buff=0},tag=Minion] armor.feet with diamond_boots{Enchantments:[{id:depth_strider,lvl:3},{id:feather_falling,lvl:4},{id:protection,lvl:4},{id:thorns,lvl:3}]}
scoreboard players set @e[type=minecraft:skeleton,scores={buff=0},tag=Minion] buff 1

schedule function hwrd22expertmode:warden_tick 30s
