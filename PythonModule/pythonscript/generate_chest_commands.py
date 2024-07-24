import json

print("JSON module is available")


def generate_chest_commands(json_file, output_file):
    try:
        with open(json_file, 'r') as f:
            chests = json.load(f)

        with open(output_file, 'w') as f:
            for chest in chests:
                pos = chest['position']
                x, y, z = pos['x'], pos['y'], pos['z']

                # Place the chest
                f.write(f"setblock {x} {y} {z} minecraft:chest[facing=north] replace\n")

                # Prepare items
                items = []
                for slot, item in chest['items'].items():
                    item_id = item['item']
                    count = item['count']
                    # Skip empty items
                    if count > 0:
                        slot_index = int(slot)
                        items.append(f"{{Slot:{slot_index}b,id:\"{item_id}\",count:{count}b}}")

                if items:
                    items_data = ",".join(items)
                    f.write(f"data merge block {x} {y} {z} {{Items:[{items_data}]}}\n")

    except FileNotFoundError:
        print(f"Error: The file {json_file} does not exist.")
    except json.JSONDecodeError:
        print(f"Error: The file {json_file} contains invalid JSON.")
    except Exception as e:
        print(f"An unexpected error occurred: {e}")


# Specify the paths to your files
generate_chest_commands('C:/Users/creeh/OneDrive/Main/PROGRAMS/Therium-Mod/run/chests.json', 'generated_commands')
