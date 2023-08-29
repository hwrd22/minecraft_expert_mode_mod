function hwrd22expertmode:wither_desperation

execute in minecraft:the_end unless entity @e[type=minecraft:ender_dragon,distance=0..] run kill @e[type=minecraft:dragon_fireball]

execute as @e[type=armor_stand,tag=WitherCheck] at @s unless entity @e[type=wither,tag=phase2,distance=..25] run function hwrd22expertmode:wither_cleanup

schedule function hwrd22expertmode:long_tick 10s