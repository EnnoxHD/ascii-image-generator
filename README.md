[![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/ennoxhd/ascii-image-generator?include_prereleases&label=version&sort=semver)](https://github.com/ennoxhd/ascii-image-generator/tags)
[![Code](https://img.shields.io/badge/code-Java%2015-blue)](https://jdk.java.net/15/)
[![Build Tool](https://img.shields.io/badge/build%20tool-Gradle%206.8.3-yellow)](https://gradle.org/releases/)
[![GitHub](https://img.shields.io/github/license/ennoxhd/ascii-image-generator)](https://opensource.org/licenses/MIT)

# ascii-image-generator
Generates ASCII art from a given image.

| **Table of contents** |
| --------------------- |
| **[Demo](#demo)**<br />**[Features](#features)**<br />**[Quick start](#quick-start)**<br />**[Documentation](#documentation) (with Screenshots)**<br />**[Motivation](#motivation)**<br />**[Development](#development)** |

## Demo
| Original | ASCII art |
| -------- | --------- |
| ![./demo/demo.jpg](./demo/demo.jpg) | ![./demo/demo_ascii.png](./demo/demo_ascii.png) |
| ["Wallpaper: The Way" by OiMax](https://www.flickr.com/photos/72396314@N00/3733544507)<br />is licensed with [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/). | Screenshot of the<br />[resulting text file](./demo/demo.txt) |

## Features
- Supported image formats: **bmp, gif, jpg, jpeg, png, tiff, wbmp**
- Independent width and height **pre-scaling**
- Multiple conversion parameters
    - Interpolation types for pre-scaling the image: **Bicubic, Bilinear, Nearest Neighbor**
    - Rounding method for quantization: **Ceil, Floor, Round**
    - Character variation: **10 characters, 70 characters**

## Quick start

### Requirements
The program requires at least the installation of [Java 8 or higher to run Gradle](https://docs.gradle.org/current/userguide/installation.html#sec:prerequisites).

### How to run
To start the application simply run the following command from the command line.

| Linux / macOS   | Windows             |
| --------------- | ------------------- |
| `./gradlew run` | `.\gradlew.bat run` |

## Documentation
### "Open file" dialog
Choose an image file in one of the supported formats to start.

**Supported formats:** bmp, gif, jpg, jpeg, png, tiff, wbmp

![./screenshots/01.png](./screenshots/01.png)


### "Scaling factors" dialog
Change the factors for pre-scaling the input image. It may be useful to change the aspect ratio of the input image since pixels are converted 1:1 into characters and the font you are displaying the ASCII image with is likely to not have an aspect ratio of 1:1. You can also take a tradeoff in image resolution and therefore reduce the resulting file size.

**Input range for _width_ and _height_:** 5-500% (Default: 100%)\
**Half height:** Set the _height_ to one half the _width_ resulting in a 2:1 ratio.\
**Proportional:** Set the _height_ to the _width_ resulting in a 1:1 ratio.

![./screenshots/02.png](./screenshots/02.png)

### "Image conversion methods" dialog
Change the appearance of the resulting ASCII image by changing the following settings to influence the conversion algorithm.

**Interpolation type for image scaling:**\
_Bicubic_: Uses [Bicubic interpolation](https://en.wikipedia.org/wiki/Bicubic_interpolation) for scaling. This results in a smoother image. This takes 16 pixels into account.\
_Bilinear_ (default): Uses [Bilinear interpolation](https://en.wikipedia.org/wiki/Bilinear_interpolation) for scaling. This is good for most purposes but may lead to some interpolation artifacts. This takes 4 pixels into account.\
_Nearest Neighbor_: Uses [Nearest-neighbor interpolation](https://en.wikipedia.org/wiki/Nearest-neighbor_interpolation) for scaling. This is useful for pixel art and preserves the blocky details.\
**Rounding method for quantization:**\
_Ceil_: Use [`Math::ceil`](https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/lang/Math.html#ceil(double)) for rounding values in the quantization process. This leads to more white details and reduces almost black details.\
_Floor_: Use [`Math::floor`](https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/lang/Math.html#floor(double)) for rounding values in the quantization process. This leads to more black details and reduces almost white details.\
_Round_ (default): Use [`Math::round`](https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/lang/Math.html#round(double)) for rounding values in the quantization process. This leads to an even distribution between almost white and almost black details.\
**Character variation in resulting image:**\
_10 characters_ (default): Uses the character sequence `@%#*+=-:. ` to represent the different levels of grey in the ASCII image.\
_70 characters_: Uses the character sequence ``$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. `` to represent the different levels of grey in the ASCII image.

![./screenshots/03.png](./screenshots/03.png)

### "Success" dialog
The image has been successfully converted and the resulting file name of the ASCII image is indicated.

![./screenshots/04.png](./screenshots/04.png)

## Motivation
There were many aspects that motivated me to start this project and
to program some things the way they are now:
1. Create awesome ASCII art with a simple algorithm.
1. Recreate a known grayscale algorithm from mathematical notation in self-describing code referencing the given notation
(after realizing that an algorithm like that is not as simple as taking the average of all color channels).
1. Work with some low level bit operations in Java.
1. Explore the usage of Java Swing in a small side project in contrast to JavaFX.
1. Explore the benefits and drawbacks of using `java.util.Optional` instead of `null` as a return value.

## Development

### Gradle Tasks
| Task          | Description |
| ------------- | ----------- |
| `run`         | Runs this project as a JVM application. |
| `debug`       | Runs this project as a JVM application with debugging enabled.<br />- add program arguments with `-Pargs="..."`<br />- attach a remote debugger via port `localhost:5005` |
| `showJavadoc` | Opens the generated Javadoc API documentation in the default browser. |

### ARGB32 color model
The color of a pixel is represented as a 32-bit integer as descibed by the
[ARGB32 color model](https://en.wikipedia.org/wiki/RGBA_color_model#ARGB32).
The 32 bits represent 4 channels of 8 bits each where the first one is alpha (`A`) and
the three remaining channels are for red (`R`), green (`G`) and blue (`B`).
The range of values for each channel is based on the sample length.
So in this case the 8 bit wide channels allow for values ranging from `0` to `255` in decimal or
`00` to `FF` in hexadecimal notation.
`00` means black (in case of channels `R`, `G` or `B`) or
fully transparent (in case of the alpha channel `A`).
`FF` means full brightness of the color or fully opaque.

[![PixelSamples32bppRGBA.png](https://upload.wikimedia.org/wikipedia/commons/0/0e/PixelSamples32bppRGBA.png)](https://upload.wikimedia.org/wikipedia/commons/0/0e/PixelSamples32bppRGBA.png)

To get the different channel values out of a full representation of a pixel color
some bit manipulation is needed.
In Java this may look like the following:

```Java
// Get a pixel sample
BufferedImage image;
int pixel = image.getRGB(0, 0); // 0xff306090 (#FF306090)

// Extract the channel values
int alpha = pixel >>> 24; // 0xff (255)
int red = (pixel >>> 16) & 0xff; // 0x30 (48)
int green = (pixel >>> 8) & 0xff; // 0x60 (96)
int blue = pixel & 0xff; // 0x90 (144)
```

A more detailed explanation with visualized bit operations steps:
```text
pixel        = [11111111 00110000 01100000 10010000]
pixel >>> 24 = [00000000 00000000 00000000 11111111] = alpha

pixel        = [11111111 00110000 01100000 10010000]
pixel >>> 16 = [00000000 00000000 11111111 00110000]
             & [00000000 00000000 00000000 11111111]
               [00000000 00000000 00000000 00110000] = red

pixel        = [11111111 00110000 01100000 10010000]
pixel >>>  8 = [00000000 11111111 00110000 01100000]
             & [00000000 00000000 00000000 11111111]
               [00000000 00000000 00000000 01100000] = green

pixel        = [11111111 00110000 01100000 10010000]
             & [00000000 00000000 00000000 11111111]
               [00000000 00000000 00000000 10010000] = blue
```

### Grayscale algorithm
[Grayscale algorithm](https://en.m.wikipedia.org/wiki/Grayscale#Colorimetric_(perceptual_luminance-preserving)_conversion_to_grayscale)

### Character sequences for ASCII art
[Character sequences](http://paulbourke.net/dataformats/asciiart/)
