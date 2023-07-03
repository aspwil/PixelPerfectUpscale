package io.github.aspwil.pixelperfectupscale;

import javax.imageio.ImageIO;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        int scale = 2;
        Path inputPath = null;
        Path outputPath = null;

        String version = "V1.0";
        String help = "Pixel Perfect Upscale" + version + "\n"
                + "the command structure is: PPUP <flags> [input file/folder] <output file/folder>"
                + "the flags are as follows:\n"
                + "-h --help : This shows help (this dialog)\n"
                + "-s --scale [int]: integer to scale images by\n"
                + "example: PPUP -s 3 \"path\\to\\input.png\" \"path\\to\\output.png\n"
                + "would scale the input.png image up by 3 pixels per original pixel and save it to a file named output.png\n"
                + "specifying only an input FILE will overwrite the original image\n"
                + "specifying only an input FOLDER will overwrite all the original images in the folder\n"
                + "specifying an input FILE and output FOLDER will output the original file, upscaled, to the output folder, preserving name\n"
                + "specifying an input FOLDER and output FOLDER will output all the files from the input folder, upscaled, to the output folder, preserving names\n"
                + "specifying an input FOLDER and output FILE will error\n"
                + "if no scale number is provided the default is 2x";

        if (args.length == 0) {
            //no inputs
            System.out.println(help);
            System.exit(1);
        } else {
            for (int i = 0; i < args.length; i++) { //for each arg
                switch (args[i]) { //switch depending on the arg
                    case "-h":
                    case "--help":
                        System.out.println(help); //print help
                        System.exit(1); //exit
                    case "-s":
                    case "--scale":
                        try {
                            scale = Integer.parseInt(args[i + 1]); //try to read the scale
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) { //error or no scale
                            error("invalid scale, need an integer. ex: \"-s 3\""); //print error
                        }
                        i++; //increase by an extra to skip the number as an input
                        break;
                    default:
                        if(inputPath == null){// if input is null, then input path
                            try {
                                inputPath = Paths.get(args[i]); //get input path
                            } catch (InvalidPathException e) { //path is invalid
                                error("invalid input path syntax: \""+args[i]+"\""); //print error
                            }
                            if (!Files.exists(inputPath)){ //path does not exist
                                error("non-existent input path: \""+args[i]+"\""); //print error
                            }
                        } else { //if input is not null, then it's an output path
                            try {
                                outputPath = Paths.get(args[i]); //get output path
                            } catch (InvalidPathException e) { //path is invalid
                                error("invalid input path syntax: \""+args[i]+"\""); //print error
                            }
                        }
                }
            }
        }

        //error if there is no input path
        if (inputPath == null){
            error("no input path");
        }

        //if input is folder, and output is file
        if (outputPath != null) {
            if (Files.isDirectory(inputPath) && !Files.isDirectory(outputPath)) {
                error("cant convert a folder into a file");
            }
        }
        //convert
        convert(inputPath, outputPath, scale);
    }


    private static void convert(Path inPath, Path outPath, int scale){

        File[] files = null;
        File outFile = null;

        //get files
        if(Files.isDirectory(inPath)){//if its a directory
            files = inPath.toFile().listFiles(new FileFilter() { //scan the path for files that are pngs
                public boolean accept(File f) {
                    return f.isFile() && f.getName().toLowerCase().endsWith(".png");
                }
            });
            if(files.length == 0){
                error("no png files found in directory"); //error if no pngs found
            }

        } else if (Files.isRegularFile(inPath)) { //if it's a single file
            if (inPath.toFile().getName().toLowerCase().endsWith(".png")) { //check for png
                files = new File[] {inPath.toFile()};
            } else {
                error("not png file");
            }
        } else {
            error("input path is not file or directory");
        }


        //for each input file
        for(File inFile : files) {
            //find output location
            if (outPath == null){ //no out path, override original file
                outFile = inFile;
            }else if (Files.isDirectory(outPath)){  //if it's a directory
                outFile = new File(outPath.toFile(), inFile.getName()); //make a new file, same name as in file
            }else if (outPath.toString().toLowerCase().endsWith(".png")) { //if the output file is a png
                outFile = outPath.toFile(); //set outpath
            }else{
                error("output path is not a png file or directory");
            }
            //upscale file
            try {
                System.out.print("writing \""+inFile.getAbsolutePath()+"\" @"+scale+"x to \""+outFile.getAbsolutePath()+"\" :");
                ImageIO.write(ImageResize.resize(ImageIO.read(inFile), scale), "png", outFile);
                System.out.print(" done\n");
            } catch (IOException e) {
                error("could not write image");
            }
        }



    }


    //tells user error and ends program
    private static void error(String str) {
        System.out.println("ERROR: " + str);
        System.exit(1);
    }
}