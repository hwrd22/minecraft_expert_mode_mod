fill ~12 ~-3 ~12 ~-12 ~19 ~-12 air replace

# No more void drops
fill ~12 -64 ~12 ~-12 -64 ~-12 bedrock replace

kill @e[type=minecraft:wither_skull,tag=bullets]
kill @e[type=minecraft:wither_skull,tag=shield]
tag @e[type=wither,tag=phase2,limit=1,sort=nearest] add dead
tp @e[type=wither,tag=dead] ~ -255 ~
kill @e[type=wither,tag=dead]
kill @s