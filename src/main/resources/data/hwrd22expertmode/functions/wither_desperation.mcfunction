# Wither
execute as @e[type=wither] store result score @s Health run data get entity @s Health
execute as @e[type=wither,scores={Health=..150}] at @s run summon minecraft:wither_skeleton ~ ~ ~ {Tags:["Minion"]}
execute as @e[type=wither,scores={Health=..375},tag=phase2] at @s run summon minecraft:wither_skeleton ~ ~ ~ {Tags:["Minion"]}
execute as @e[type=wither,scores={Health=..375},tag=phase2] at @s run summon minecraft:wither_skeleton ~ ~ ~ {Tags:["Minion"]}
execute as @e[type=wither,scores={Health=..375},tag=phase2] at @s run summon minecraft:wither_skeleton ~ ~ ~ {Tags:["Minion"]}
execute as @e[type=wither,scores={Health=..375},tag=phase2] at @s run summon minecraft:wither_skeleton ~ ~ ~ {Tags:["Minion"]}
scoreboard players add @e[type=minecraft:wither_skeleton,tag=Minion] buff 0
item replace entity @e[type=minecraft:wither_skeleton,scores={buff=0},tag=Minion] weapon.mainhand with stone_sword
scoreboard players set @e[type=minecraft:wither_skeleton,scores={buff=0},tag=Minion] buff 1

# Giving Elder Guardians some attention
execute as @e[type=elder_guardian] store result score @s Health run data get entity @s Health
execute as @e[type=elder_guardian,scores={Health=..125}] at @s run summon minecraft:drowned ~1 ~1 ~1
execute as @e[type=elder_guardian,scores={Health=..50}] at @s run summon minecraft:drowned ~-1 ~1 ~-1
execute as @e[type=elder_guardian,scores={Health=..50}] at @s run summon minecraft:drowned ~-1 ~1 ~-1

# Warden
execute as @e[type=warden,scores={Health=..768}] at @s run attribute @s minecraft:generic.movement_speed base set 0.8
execute as @e[type=warden,scores={Health=..512}] at @s run effect give @a[distance=..20] minecraft:blindness 11