package com.athkar.lock;
import android.app.*;
import android.content.*;
import android.graphics.PixelFormat;
import android.os.*;
import android.provider.Settings;
import android.view.*;
import android.widget.*;
import java.util.*;
public class UnlockService extends Service {
  View popup; WindowManager wm; List<Store.Item> items; final Handler h = new Handler(Looper.getMainLooper()); final Runnable closer = new Runnable() { public void run() { close(); } }; final Random rnd = new Random();
  final BroadcastReceiver rcv = new BroadcastReceiver() { public void onReceive(Context c, Intent i) { if (Intent.ACTION_SCREEN_OFF.equals(i.getAction())) close(); else if (Store.due(UnlockService.this)) show(); } };
  public IBinder onBind(Intent i) { return null; }
  public void onCreate() {
    super.onCreate(); items = Store.load(this); wm = (WindowManager) getSystemService(WINDOW_SERVICE);
    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    if (Build.VERSION.SDK_INT >= 26) nm.createNotificationChannel(new NotificationChannel("b", "أذكار", NotificationManager.IMPORTANCE_LOW));
    Notification.Builder b = Build.VERSION.SDK_INT >= 26 ? new Notification.Builder(this, "b") : new Notification.Builder(this);
    b.setSmallIcon(android.R.drawable.ic_menu_info_details).setContentTitle("أذكار").setContentText("يعمل عند فتح الشاشة");
    startForeground(1, b.build());
    IntentFilter f = new IntentFilter(Intent.ACTION_USER_PRESENT); f.addAction(Intent.ACTION_SCREEN_OFF);
    registerReceiver(rcv, f);
  }
  public int onStartCommand(Intent i, int f, int id) { return START_STICKY; }
  public void onDestroy() { try { unregisterReceiver(rcv); } catch (Exception e) {} close(); super.onDestroy(); }
  void close() { h.removeCallbacks(closer); if (popup != null) { try { wm.removeView(popup); } catch (Exception e) {} popup = null; } }
  void show() {
    if (items.isEmpty() || !Settings.canDrawOverlays(this)) return;
    close();
    int cf = Store.cat(this); List<Store.Item> pool = new ArrayList<>();
    for (Store.Item x : items) if (cf == 0 || (cf == 1 && x.cat == 'A') || (cf == 2 && x.cat == 'H')) pool.add(x);
    if (pool.isEmpty()) pool = items;
    Store.Item it = pool.get(rnd.nextInt(pool.size()));
    FrameLayout root = new FrameLayout(this); root.setBackgroundColor(0xE60A1410); root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    root.addView(new Ui.Pattern2(this));
    LinearLayout col = new LinearLayout(this); col.setOrientation(LinearLayout.VERTICAL); col.setGravity(Gravity.CENTER);
    int m = Ui.dp(this, 24); col.setPadding(m, m, m, m);
    LinearLayout card = new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL); card.setGravity(Gravity.CENTER);
    card.setBackground(Ui.box(this, 0xFF0F1D17, Ui.GOLD, 24, 1.5f)); card.setPadding(m, m * 2, m, m * 2);
    if (Build.VERSION.SDK_INT >= 28) { card.setOutlineAmbientShadowColor(Ui.GOLD); card.setOutlineSpotShadowColor(Ui.GOLD); card.setElevation(Ui.dp(this, 14)); }
    TextView t = new TextView(this); t.setText(it.text); t.setTypeface(Ui.font(this)); t.setTextColor(Ui.CREAM); t.setTextSize(24); t.setGravity(Gravity.CENTER); t.setLineSpacing(0, 1.4f);
    TextView s = new TextView(this); s.setText(it.src); s.setTypeface(Ui.font(this)); s.setTextColor(Ui.GOLD); s.setTextSize(15); s.setGravity(Gravity.CENTER); s.setPadding(0, m, 0, 0);
    ScrollView sv = new ScrollView(this); LinearLayout in = new LinearLayout(this); in.setOrientation(LinearLayout.VERTICAL); in.addView(t); in.addView(s); sv.addView(in);
    card.addView(sv, new LinearLayout.LayoutParams(-1, -2));
    col.addView(card, new LinearLayout.LayoutParams(-1, -2));
    View bar = new View(this); bar.setBackgroundColor(Ui.GOLD); bar.setAlpha(.7f);
    LinearLayout.LayoutParams bl = new LinearLayout.LayoutParams(-1, Ui.dp(this, 3)); bl.topMargin = m / 2; col.addView(bar, bl);
    bar.animate().scaleX(0f).setDuration(10000).setInterpolator(new android.view.animation.LinearInterpolator()).start();
    TextView x = new TextView(this); x.setText("✕"); x.setTextColor(Ui.GOLD); x.setTextSize(22); x.setGravity(Gravity.CENTER);
    x.setBackground(Ui.box(this, 0x00000000, Ui.GOLD, 100, 1)); x.setOnClickListener(v -> close());
    LinearLayout.LayoutParams xp = new LinearLayout.LayoutParams(Ui.dp(this, 56), Ui.dp(this, 56)); xp.topMargin = m; col.addView(x, xp);
    root.addView(col, new FrameLayout.LayoutParams(-1, -1));
    int type = Build.VERSION.SDK_INT >= 26 ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE;
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams(-1, -1, type, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT);
    popup = root; try { wm.addView(root, lp); h.postDelayed(closer, 10000); } catch (Exception e) { popup = null; }
  }
}
