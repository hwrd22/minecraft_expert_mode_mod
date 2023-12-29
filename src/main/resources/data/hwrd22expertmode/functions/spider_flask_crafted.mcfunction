playsound minecraft:block.brewing_stand.brew master @s
give @s potion{CustomPotionColor:3881787,display:{Name:'[{"text":"Spider Flask","italic":false,"color":"gray"}]',Lore:['[{"text":"Are you really going to drink","italic":false}]','[{"text":"this...?","italic":false}]']},CustomPotionEffects:[{Id:8,Duration:3600,Amplifier:1},{Id:16,Duration:1800},{Id:19,Duration:100}]}
recipe take @s hwrd22expertmode:spider_flask
clear @s minecraft:knowledge_book
advancement revoke @s only hwrd22expertmode:spider_flask