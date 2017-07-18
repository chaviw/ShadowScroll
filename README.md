# ShadowScroll
A custom view to show a shadow under the header while scrolling

You can run the example app to test out multiple ways to use the api. See [examples](https://github.com/chaviw/ShadowScroll.git).

## Usage
Use `com.scrollviews.shadowedscroll.ShadowedScrollLinearLayout` as the outer most layout. Add a header view and then the body within a scollable view. You can customize orientation, shadow color, and what View will determine when the shadow will be shown and hidden based on scroll.

### Standard Vertical NestedScrollView
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.scrollviews.shadowedscroll.ShadowedScrollLinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- Header -->
    <include layout="@layout/title_view" />

    <android.support.v4.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@+id/shadow">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

            <!-- Body -->
            
        </LinearLayout>
    </android.support.v4.widget.NestedScrollView>
</com.scrollviews.shadowedscroll.ShadowedScrollLinearLayout>
```
### Horizontal Scroll
Change orientation to `horizontal` and make the scrollable body a `HorizontalScrollView`

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.scrollviews.shadowedscroll.ShadowedScrollLinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="horizontal">

    <TextView
        android:id="@+id/settingsTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:rotation="-90"
        android:text="Title"
        android:textSize="32dp" />

    <HorizontalScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:orientation="horizontal">
          
            <!-- Body -->
            
        </LinearLayout>
    </HorizontalScrollView>
</com.scrollviews.shadowedscroll.ShadowedScrollLinearLayout>
```

### Shadow with Color
Use `app:shadowColor="@color/your_color"`

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.scrollviews.shadowedscroll.ShadowedScrollLinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_height="match_parent"
    app:shadowColor="@color/shadowColor">

    <include layout="@layout/title_view" />

    <android.support.v4.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@+id/shadow">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

            <!-- Body -->
            
        </LinearLayout>
    </android.support.v4.widget.NestedScrollView>
</com.scrollviews.shadowedscroll.ShadowedScrollLinearLayout>
```

### Shadow with Drawable
Use `app:shadowDrawable="@drawable/your_drawable"`

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.scrollviews.shadowedscroll.ShadowedScrollLinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:shadowDrawable="@drawable/shadow_bg"
    android:layout_height="match_parent">

    <include layout="@layout/title_view" />

    <android.support.v4.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@+id/shadow">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">
            
            <!-- Body -->
            
        </LinearLayout>
    </android.support.v4.widget.NestedScrollView>
</com.scrollviews.shadowedscroll.ShadowedScrollLinearLayout>
```

### Index the scrollable view
You can also customize the layout so that any view will be considered the scrollable part for the shadow. The shadow will be placed above the specified item when it's scrolled.
Use `app:scrollable="true'` for the View that should control when the shadow is shown and hidden.

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.scrollviews.shadowedscroll.ShadowedScrollLinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <include layout="@layout/title_view" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:gravity="center"
        android:text="SOME TEXT BEFORE SCROLL" />

    <android.support.v4.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@+id/shadow"
        app:scrollable="true">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">
            
            <!-- Body -->
            
        </LinearLayout>
    </android.support.v4.widget.NestedScrollView>
</com.scrollviews.shadowedscroll.ShadowedScrollLinearLayout>
```
