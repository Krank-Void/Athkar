package com.athkar.lock;
import android.content.*;
import android.os.Build;
public class BootReceiver extends BroadcastReceiver {
  public void onReceive(Context c, Intent i) {
    if (Store.on(c)) { Intent s = new Intent(c, UnlockService.class); if (Build.VERSION.SDK_INT >= 26) c.startForegroundService(s); else c.startService(s); }
  }
}
