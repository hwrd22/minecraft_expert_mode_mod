function hwrd22expertmode:crystal_cage

execute as @e[type=wither,nbt=!{HandItems:[{id:"minecraft:totem_of_undying"},{}]}] unless entity @s[tag=phase2] at @s run function hwrd22expertmode:wither_phase2_intermission

execute as @e[type=armor_stand,tag=WitherCheck] at @s unless entity @a[distance=..25] run function hwrd22expertmode:wither_phase2_failure

execute at @e[type=minecraft:elder_guardian] run particle minecraft:dragon_breath ~ ~1 ~ 1.125 1.125 1.125 0 10 force

execute at @e[type=minecraft:guardian] run particle minecraft:dragon_breath ~ ~.5 ~ .25 .25 .25 0 5 force

execute as @e[type=warden] store result score @s Health run data get entity @s Health
