import json

print("JSON module is available")

def generate_container_commands(json_file, output_file):
    try:
        with open(json_file, 'r') as f:
            containers = json.load(f)

        with open(output_file, 'w') as f:
            for container in containers:
                # Retrieve position and container type
                pos = container.get('pos', {})
                x, y, z = pos.get('x', 0), pos.get('y', 0), pos.get('z', 0)
                container_type = container.get('containerType', 'chest')  # Default to chest if not specified

                # Determine the block type and orientation based on the container type
                if container_type == 'chest':
                    block_type = 'minecraft:chest'
                    orientation = 'facing=north'
                elif container_type == 'shulker':
                    block_type = 'minecraft:shulker_box'
                    orientation = 'facing=up'
                elif container_type == 'barrel':
                    block_type = 'minecraft:barrel'
                    orientation = 'facing=up'
                else:
                    print(f"Warning: Unknown container type '{container_type}' at position ({x}, {y}, {z}). Skipping...")
                    continue

                # Write the command to place the container block
                f.write(f"setblock {x} {y} {z} {block_type}[{orientation}] replace\n")

                # Prepare items data
                items = []
                container_items = container.get('items', {})
                for slot, item in container_items.items():
                    item_id = item.get('item', 'minecraft:air')
                    count = item.get('count', 1)
                    # Skip empty items
                    if count > 0:
                        slot_index = int(slot)
                        items.append(f"{{Slot:{slot_index}b,id:\"{item_id}\",count:{count}b}}")

                if items:
                    items_data = ",".join(items)
                    # Write the command to merge item data with the block
                    f.write(f"data merge block {x} {y} {z} {{Items:[{items_data}]}}\n")
                else:
                    print(f"No items found for container at ({x}, {y}, {z})")

    except FileNotFoundError:
        print(f"Error: The file {json_file} does not exist.")
    except json.JSONDecodeError:
        print(f"Error: The file {json_file} contains invalid JSON.")
    except Exception as e:
        print(f"An unexpected error occurred: {e}")

# Specify the paths to your files
generate_container_commands('C:/Users/creeh/OneDrive/Main/PROGRAMS/Therium-Mod/run/chests.json', 'generated_commands')
