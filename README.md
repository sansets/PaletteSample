# PaletteSample
The palette library is a support library that extracts prominent colors from images to help you create visually engaging apps.

For example, you can use a palette to create a color-coordinated title card for a song based on its album cover or to adjust an app’s toolbar color when its background image changes. The Palette object gives you access to the colors in a Bitmap image while also providing six main color profiles from the bitmap to help inform your design choices.

## Sample Implementation
In this sample, the `Palette` is used to get vibrant colors and set it into `CardView` shadows.
![App preview](https://raw.githubusercontent.com/sandistw/JetpackPaletteSample/master/app-preview.gif)

## Setup
```gradle
android {
  compileSdkVersion 28
  ...
}

dependencies {
  ...
  implementation 'androidx.palette:palette:1.0.0'
}
```
    
## Create a Palette
```kotlin
// Generate palette synchronously and return it
fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

// Generate palette asynchronously and use it on a different
// thread using onGenerated()
fun createPaletteAsync(bitmap: Bitmap) {
    Palette.from(bitmap).generate { palette ->
        // Use generated instance
    }
}
```

## References
https://developer.android.com/training/material/palette-colors
