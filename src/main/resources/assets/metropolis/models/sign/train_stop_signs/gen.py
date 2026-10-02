file_content = '''{
    "parent": "metropolis:sign/train_stop_sign_number",
    "textures": {
        "1": "metropolis:block/sign/train_stop_sign/numbers/{index}"
    }
}
'''
file_name = "sign_{index}.json"
amount = 32

if __name__ == "__main__":
    print("Generate model files...")

    for i in range(amount):
        index = i + 1

        with open(file_name.replace("{index}", str(index)), "w", encoding="utf-8") as file:
                  file.write(file_content.replace("{index}", str(index)))
        print("File generated: " + file_name.replace("{index}", str`(index)))
