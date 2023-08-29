playsound minecraft:entity.wither.death hostile @a ~ ~ ~
summon armor_stand ~ ~ ~ {NoGravity:1b,Small:1,Marker:1b,Invisible:1,NoBasePlate:1,PersistenceRequired:1,Tags:["Intermission"]}
execute as @e[type=armor_stand,tag=Intermission,limit=1] run schedule function hwrd22expertmode:wither_phase2_start 10s
tp @s ~ -255 ~
kill @s