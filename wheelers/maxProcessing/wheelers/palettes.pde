// creates a set of color palettes by sampling from image files in the data/palettes folder.
void makePalettes(String palettesFolder){
  File[] files = listFiles(palettesFolder);
  if (files == null){
      println("NO COLOR PALETTE FILES FOUND");
    } else {
      for (int i = 0; i < files.length; i++) {
        File f = files[i];
        if (!f.isHidden()){  
          println("Name: " + f.getName());
          println("Is directory: " + f.isDirectory());
          println("Size: " + f.length());
          String lastModified = new Date(f.lastModified()).toString();
          println("Last Modified: " + lastModified);
          PImage paletteImage = loadImage(dataPath("palettes") + "/" + f.getName());
          
          color[] currentPalette = new color[4];
          currentPalette[0] = paletteImage.get(25, 50);
          currentPalette[1] = paletteImage.get(75, 50);
          currentPalette[2] = paletteImage.get(125, 50);
          currentPalette[3] = paletteImage.get(175, 50);
          
          palettes.add(currentPalette);
          
          println("-----------------------");
        }
    }
  }
}


// This function returns all the files in a directory as an array of File objects
// This is useful if you want more info about the file
File[] listFiles(String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    File[] files = file.listFiles();
    return files;
  } else {
    // If it's not a directory
    return null;
  }
}
