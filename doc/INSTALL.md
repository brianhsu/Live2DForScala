Step 1. Install Open JDK 11.
================================

- For Linux, install it through your distro's package manager.
  - For Ubuntu, run `sudo apt-get install openjdk-11-jre`
  - For ArchLinux, run `sudo pacman -S jre-11-openjdk`
  - For Gentoo, run `sudo emerge -pv virtual/jre:11`
- For Windows, download OpenJDK 11 (`microsoft-jdk-11.0.15-windows-x64.msi`) From [Microsoft Build of OpenJDK](https://docs.microsoft.com/en-us/java/openjdk/download)
- For MacOS
    1. Install Homebrew
    2. Run `brew install openjdk@11` to install

Step 2. Download corresponding compressed files.
==========================================

1. Please download JAR files from the `Release` tab on GitHub:

    - For Linux, both `Live2DForScala-Swing-X.Y.Z.zip` / `Live2DForScala-SWT-Linux-X.Y.Z.tar.gz` should work.
    - For Windows,  both `Live2DForScala-Swing-X.Y.Z.zip` / `Live2DForScala-SWT-Windows-X.Y.Z.zip` should work.
    - For Mac, download `Live2DForScala-Swing-X.Y.Z.jar` 

2. Extract the downloaded file.

Step 3. Run the Demo Application
==================================

- If your system is setup correctly, you should able to double click on the jar file in the extracted folder to run it.
- If double click does not work, open a terminal (command line window), and run the following command
    ```text
    java -jar [put the downloaded file name here]
    ```
- If you want to run SWT version on Wayland, you need `GDK_BACKEND=x11` environment variable set.
    ```text
    GDK_BACKEND=x11 java -jar Live2DForScala-SWT-Linux-v{x.y.z}.jar
    ```

Step 4. Download Live2D Model
==============================

1. Download Live2D model from [here](https://www.live2d.com/en/download/sample-data/).
    - `Tsumiki Harugasa` / `Chitose` and `Jin Natori` are good choices, as them contains both expressions and motions.
    - If you want to play with lip sync from motion sound file, please download `Cubism Native SDK` from [here](https://www.live2d.com/en/download/cubism-sdk/download-native/), unzip it, and use the model at the `Samples/Resources/Haru/` folder.    

2. Extract the zip file to somewhere of your computer. 

Step 5. Controls
============================

 - Click the `Load Avatar` button of left corner, and choose the `runtime` directory (or any direction contains the `.moc3` file) of the model you just downloaded and unzipped.
 - Right click and drag to move Live2D model.
 - Use mouse wheel to zoom-in / zoom-out Live2D model.
 - Have fun!!
