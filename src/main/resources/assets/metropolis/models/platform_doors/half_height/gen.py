file_content = '''{
	"parent": "metropolis:platform_doors/half_height/platform_door_half_height",
	"textures": {
		"0": "metropolis:block/platform_doors/half_height/platform_door_half_height{color}",
		"particle": "metropolis:block/platform_doors/half_height/platform_door_half_height{color}"
	}
}'''

colors = ["white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"]

file_name = "platform_door_half_height{color}.json"

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
