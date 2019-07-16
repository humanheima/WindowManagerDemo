# WindowManagerDemo

WindowManager对应的实现类是WindowManagerImpl

[Session](http://androidxref.com/9.0.0_r3/xref/frameworks/base/services/core/java/com/android/server/wm/Session.java)

```
final WindowManagerService mService;

@Override
public int addToDisplay(IWindow window, int seq, WindowManager.LayoutParams attrs,
        int viewVisibility, int displayId, Rect outFrame, Rect outContentInsets,
        Rect outStableInsets, Rect outOutsets,
        DisplayCutout.ParcelableWrapper outDisplayCutout, InputChannel outInputChannel) {
    return mService.addWindow(this, window, seq, attrs, viewVisibility, displayId, outFrame,
            outContentInsets, outStableInsets, outOutsets, outDisplayCutout, outInputChannel);
}

```