playsound minecraft:block.brewing_stand.brew master @s
give @s potion{CustomPotionColor:3881787,custom_potion_effects:[{id:night_vision,duration:3600},{id:poison,duration:100},{id:jump_boost,duration:3600,amplifier:1}],display:{Name:'["",{"text":"Spider Flask","italic":false,"color":"#"}]',Lore:['["",{"text":"Are you really going to drink this?","italic":false}]']}}
recipe take @s hwrd22expertmode:spider_flask
clear @s minecraft:knowledge_book
advancement revoke @s only hwrd22expertmode:spider_flask