# java-icon-theme
freedesktop.org's Icon Theme Specification implemented in Java.

## Resolving an icon path

```java
import fd.theme.IconTheme;
import fd.theme.icons.BatteryIcon;

// get the current theme using gsettings
IconTheme theme = IconTheme.getCurrentTheme();

// find an icon using a helper class
File batteryFull = theme.findIcon(BatteryIcon.FULL_CHARGED);

// find an icon using its name
File firefox = theme.findIcon("firefox");

// find an icon of the given size (64) and scale (1)
File chrome = theme.findIcon("google-chrome", 64, 1);

```
The findIcon method takes about 0-1ms to return a value :)

## Limitations
1. Currently this only find themes located in /usr/share/icons
2. This implementation doesn't lookups for an icon on parent themes if an icon is not found in the current theme
3. No fallback icon is returned when an icon is not found
