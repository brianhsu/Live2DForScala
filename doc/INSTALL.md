Step 1. Install Open JDK 11.
================================

- For Windows, download [Microsoft Build of OpenJDK](https://docs.microsoft.com/en-us/java/openjdk/download)
- For Linux, install it through your distro's package manager.
- For MacOS
    1. Install Homebrew
    2. Run `brew install openjdk@11` to install

Step 2. Download corresponding JAR files.
==========================================

Please download JAR files from the `Release` tab on GitHub:

- For Linux, both `Live2DForScala-Swing-0.0.X.jar` / `Live2DForScala-SWT-Linux-0.0.X.jar` should work.
- For Windows,  both `Live2DForScala-Swing-0.0.X.jar` / `Live2DForScala-SWT-Windows-0.0.X.jar` should work.
- For Mac, download `Live2DForScala-Swing-0.0.X.jar` 

Step 3. Run the Demo Application
==================================

- If your system is setup correctly, you should able to double click on the downloaded jar file to run it.
- If double click does not work, open a terminal (command line window), and run the following command
    ```text
    java -jar [put the downloaded file name here]
    ```

Step 4. Download Live2D Model
==============================

1. Download Live2D model from [here](https://www.live2d.com/en/download/sample-data/).
    - Please note `Niziiro Mao` is NOT supported.
    - `Tsumiki Harugasa` / `Chitose` and `Jin Natori` are good choices, as them contains both expressions and motions.

2. Extract the zip file to somewhere of your computer. 

Step 5. Controls
============================

 - Click the `Load Avatar` button of left corner, and choose the `runtime` directory (or any direction contains the `.moc3` file) of the model you just downloaded and unzipped.
 - Right click and drag to move Live2D model.
 - Use mouse wheel to zoom-in / zoom-out Live2D model.
 - Have fun!!
