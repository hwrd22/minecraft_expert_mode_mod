# Gamerules
gamerule universalAnger true
gamerule forgiveDeadPlayers false

# Scoreboard objectives
scoreboard objectives add buff dummy
scoreboard objectives add Health dummy

# Create Enrageable Mobs
team add enrageable
team modify enrageable color red

# Start long tick functions
function hwrd22expertmode:long_tick
function hwrd22expertmode:warden_tick

#Inform player
tellraw @a "Expert mode has been loaded!"