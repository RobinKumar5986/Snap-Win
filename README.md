# Snap Win

**Snap Win** is an Android application that allows users to dynamically create and interact with UI elements within a workspace.  
Users can **create**, **move**, **resize**, **delete**, and **customize** elements using simple text commands.  

---

## Features

- Create multiple dynamic UI elements (Buttons, Text Inputs, Labels).
- Move and resize elements anywhere in the workspace.
- Change colors of elements using command-based interactions.
- Delete elements when no longer needed.
- Command-driven architecture for flexible operations.

---

## Command Reference

Below are the supported commands and their descriptions:

### 1. Create Button
```
c b
```
<img src="https://github.com/user-attachments/assets/ac4e5e91-d2ff-4e70-a85b-67048a296b5f" height="200"/>

### 2. Create Text Input
```
c ti
```
<img src="https://github.com/user-attachments/assets/aaa69895-8c39-4d88-ac5d-7348eb576e8b" height="200"/>


### 3. Create Lables 
```
c l <lable>
```
<img src="https://github.com/user-attachments/assets/9b506a98-9507-4ca1-9ed0-5c771df15a96" height="200"/>

here `<lable> ` is a varieable word passed by the user.

### 4. Move Command
  - you can directly move the element but you can also move the elemets using below command
```
mv <viewId> <x> <y>
```
<img src="https://github.com/user-attachments/assets/9aceee1e-1e51-428c-85f5-0f37779ec6f8" height="200"/>

where `<viewId>` is varieable passed by the used i.e which view to move and `<x>` , `<y>` are the coordinates where to move.

### 5. Resize View
  - You can resze the views directly using the gesture but you can do this also by commands.
  - use the below command to do so.
```
rz <viewId> <width> <height>
```
<img src="https://github.com/user-attachments/assets/413bd6b8-0980-4faa-833c-d792ea2af949" height="200"/>

where `<viewId>` is varieable passed by the used i.e which view to move and `<width>` , `<height>` are the new width and height for the view.

### Changing Color
  - The are some predefined set of color which can be used to change used to chnage the color of the view using the command below.
```
cc <viewId> <ColorName>
```
<img src="https://github.com/user-attachments/assets/599e28b5-16b4-4b48-832f-6bd9432b88eb" height="200"/>

### Deleting View
  - Views can be deleted direcly but it can also be deleted usinf commands.
```
dl <viewId>
```
<img src="https://github.com/user-attachments/assets/9472f067-0484-4c1e-b153-aa9479971ed3" height="200"/>



