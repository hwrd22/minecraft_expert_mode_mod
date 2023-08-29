execute as @e[type=minecraft:wither_skull,tag=shield] run data modify entity @s Motion set value [0.0, -1.5, 0.0]
fill ~12 ~-3 ~12 ~-12 ~19 ~-12 air replace

# No more void drops
fill ~12 -64 ~12 ~-12 -64 ~-12 bedrock replace

kill @e[type=minecraft:wither_skull,tag=bullets]
kill @s