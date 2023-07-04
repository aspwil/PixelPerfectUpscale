# PixelPerfectUpscale

this is tool that takes png files and upscales them pixel perfectly.

its mainly built for pixel art.

it takes each pixel in the input image and turns it into a square of multipul pixels at a predifined size.

its a command line utility

command: `java -jar PPUP.jar <flags> [input file/folder] <output file/folder>`

flags:
```
-h --help : prints help

-s --scale [int]: integer to scale images by
```

if you want to take a file `test.png` and make it 3x bigger

use `java -jar PPUP.jar -s 3 test.png`

and it will repalce the original file.

you can also use a directory as input and it will repalce the file for every png in the directory

if you specify a diffrent output directory, it will output the files to the new directory and leave the orginials uneffected

IT IS HIGHLY RECOMENDED TO USE AN OUTPUT DIRECTORY, SO AS TO NOT REPLACE THE ORIGINAL IMAGES.

this program supports upscaling images, but cant downscale them back to the original state.

you can also specify an output png file like: `java -jar PPUP.jar "test.png" "test2x.png"`

if you dont specify a scale, it will default to 2x

WARNING: you can get pretty big files from running this, especaily for larger files.





