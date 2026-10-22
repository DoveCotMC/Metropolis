file_content = '''{
    "variants": {
        "facing=north,half=lower,flipped=false": {
            "model": "metropolis:platform_doors/half_height/platform_door_half_height_glass{color}",
            "y": 0
        },
        "facing=east,half=lower,flipped=false": {
            "model": "metropolis:platform_doors/half_height/platform_door_half_height_glass{color}",
            "y": 90
        },
        "facing=south,half=lower,flipped=false": {
            "model": "metropolis:platform_doors/half_height/platform_door_half_height_glass{color}",
            "y": 180
        },
        "facing=west,half=lower,flipped=false": {
            "model": "metropolis:platform_doors/half_height/platform_door_half_height_glass{color}",
            "y": 270
        },
        "facing=north,half=upper,flipped=false": {
            "model": "minecraft:block/air",
            "y": 0
        },
        "facing=east,half=upper,flipped=false": {
            "model": "minecraft:block/air",
            "y": 90
        },
        "facing=south,half=upper,flipped=false": {
            "model": "minecraft:block/air",
            "y": 180
        },
        "facing=west,half=upper,flipped=false": {
            "model": "minecraft:block/air",
            "y": 270
        },
        "facing=north,half=lower,flipped=true": {
            "model": "metropolis:platform_doors/half_height/platform_door_half_height_glass{color}_flipped",
            "y": 0
        },
        "facing=east,half=lower,flipped=true": {
            "model": "metropolis:platform_doors/half_height/platform_door_half_height_glass{color}_flipped",
            "y": 90
        },
        "facing=south,half=lower,flipped=true": {
            "model": "metropolis:platform_doors/half_height/platform_door_half_height_glass{color}_flipped",
            "y": 180
        },
        "facing=west,half=lower,flipped=true": {
            "model": "metropolis:platform_doors/half_height/platform_door_half_height_glass{color}_flipped",
            "y": 270
        },
        "facing=north,half=upper,flipped=true": {
            "model": "minecraft:block/air",
            "y": 0
        },
        "facing=east,half=upper,flipped=true": {
            "model": "minecraft:block/air",
            "y": 90
        },
        "facing=south,half=upper,flipped=true": {
            "model": "minecraft:block/air",
            "y": 180
        },
        "facing=west,half=upper,flipped=true": {
            "model": "minecraft:block/air",
            "y": 270
        }
    }
}'''

colors = ["white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"]

file_name = "platform_door_half_height_glass{color}.json"

if __name__ == "__main__":
    print("Generate model files...")

    for color in colors:
        if color == "white":
            continue
        else:
            color = "_" + color
        
        name = file_name.replace("{color}", color)
        print(name)

        with open(name, "w", encoding="utf-8") as file:
                  file.write(file_content.replace("{color}", color))
        print("File generated: " + name)
