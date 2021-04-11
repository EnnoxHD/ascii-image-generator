[![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/ennoxhd/ascii-image-generator?label=version&sort=semver)](https://github.com/ennoxhd/ascii-image-generator/tags)
[![Code](https://img.shields.io/badge/code-Java%2015-blue)](https://jdk.java.net/15/)
[![Build Tool](https://img.shields.io/badge/build%20tool-Gradle%206.8.3-yellow)](https://gradle.org/releases/)
[![GitHub](https://img.shields.io/github/license/ennoxhd/glyph-creator)](https://opensource.org/licenses/MIT)

# ascii-image-generator
Generates ASCII art from a given image.

## Features
- Supported image formats: **bmp, gif, jpg, jpeg, png, tiff, wbmp**
- Independent width and height **pre-scaling**
- Multiple conversion parameters
    - Interpolation types for pre-scaling the image: **Bicubic, Bilinear, Nearest Neighbor**
    - Rounding method for quantization: **Ceil, Floor, Round**
    - Character variation: **10 characters, 70 characters**

### Demo
| original | ASCII art |
| -------- | --------- |
| ![./demo/demo.jpg](./demo/demo.jpg) | ![./demo/demo_ascii.png](./demo/demo_ascii.png) |
| ["Wallpaper: The Way" by OiMax](https://www.flickr.com/photos/72396314@N00/3733544507) is licensed with [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/). | screenshot of resulting [./demo/demo.txt](./demo/demo.txt) |

### Screenshots
#### "Open file" dialog
![./screenshots/01.png](./screenshots/01.png)

#### "Scaling factors" dialog
![./screenshots/02.png](./screenshots/02.png)

#### "Image conversion methods" dialog
![./screenshots/03.png](./screenshots/03.png)

#### "Success" dialog
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

## Conversion technology

### ARGB32 color model
[ARGB32 color model](https://en.wikipedia.org/wiki/RGBA_color_model#ARGB32)

### Grayscale algorithm
[Grayscale algorithm](https://en.m.wikipedia.org/wiki/Grayscale#Colorimetric_(perceptual_luminance-preserving)_conversion_to_grayscale)

### Character sequences for ASCII art
[Character sequences](http://paulbourke.net/dataformats/asciiart/)
