#NO UNDERGROUND CHEESE
execute at @e[type=armor_stand,tag=Intermission] run fill ~12 ~-2 ~12 ~-12 ~20 ~-12 air replace
execute at @e[type=armor_stand,tag=Intermission] run fill ~12 ~-2 ~12 ~-12 ~20 ~-12 bedrock outline
execute at @e[type=armor_stand,tag=Intermission] run fill ~11 ~-1 ~11 ~-11 ~-1 ~-11 light keep

# No more void drops
fill ~12 -64 ~12 ~-12 -64 ~-12 bedrock replace

execute at @e[type=armor_stand,tag=Intermission] run summon armor_stand ~ ~1 ~ {NoGravity:1b,Small:1,Marker:1b,Invisible:1,NoBasePlate:1,PersistenceRequired:1,Tags:["WitherCheck"]}
#SUMMON WITHER
execute at @e[type=armor_stand,tag=Intermission] run summon tnt ~ ~5 ~
execute at @e[type=armor_stand,tag=Intermission] run summon wither ~ ~5 ~ {Tags:["phase2"],Health:750,ActiveEffects:[{Id:11,Duration:20,Amplifier:255,ShowParticles:1b}],Attributes:[{Name:"generic.max_health",Base:750f}]}
execute at @e[type=armor_stand,tag=Intermission] run playsound minecraft:entity.wither.spawn hostile @a ~ ~5 ~
execute at @e[type=armor_stand,tag=Intermission] run tellraw @a[distance=..25] {"text":"It's not over yet, kid!","bold":true,"color":"light_purple"}
execute at @e[type=armor_stand,tag=Intermission] run advancement grant @a[distance=..25] only hwrd22expertmode:wither_phase_two
execute if entity @e[type=wither,tag=phase2,limit=1] run schedule function hwrd22expertmode:wither_phase2_shield 1s
execute if entity @e[type=wither,tag=phase2,limit=1] run schedule function hwrd22expertmode:wither_phase2_attack1 15s
execute at @e[type=armor_stand,tag=Intermission] run tp @a[distance=..25] ~10 ~-1 ~
execute as @e[type=armor_stand,tag=Intermission] run kill @s