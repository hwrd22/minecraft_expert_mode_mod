scoreboard players add @e[type=minecraft:end_crystal] buff 0
execute as @e[type=minecraft:end_crystal,scores={buff=0},nbt={ShowBottom:1b}] at @s run fill ~-1 ~2 ~1 ~1 ~2 ~-1 minecraft:obsidian
execute as @e[type=minecraft:end_crystal,scores={buff=0},nbt={ShowBottom:1b}] at @s run fill ~-1 ~1 ~2 ~1 ~-1 ~2 minecraft:obsidian
execute as @e[type=minecraft:end_crystal,scores={buff=0},nbt={ShowBottom:1b}] at @s run fill ~-2 ~1 ~-1 ~-2 ~-1 ~1 minecraft:obsidian
execute as @e[type=minecraft:end_crystal,scores={buff=0},nbt={ShowBottom:1b}] at @s run fill ~1 ~1 ~-2 ~-1 ~-1 ~-2 minecraft:obsidian
execute as @e[type=minecraft:end_crystal,scores={buff=0},nbt={ShowBottom:1b}] at @s run fill ~2 ~1 ~1 ~2 ~-1 ~-1 minecraft:obsidian
execute as @e[type=minecraft:end_crystal,scores={buff=0},nbt={ShowBottom:1b}] at @s run fill ~-2 ~2 ~-2 ~2 ~-1 ~2 minecraft:air replace minecraft:iron_bars
execute as @e[type=minecraft:end_crystal,scores={buff=0},nbt={ShowBottom:1b}] at @s run scoreboard players set @s buff 1