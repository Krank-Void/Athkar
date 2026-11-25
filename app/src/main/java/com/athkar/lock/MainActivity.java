package com.athkar.lock;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.*;
import android.net.Uri;
import android.os.*;
import android.provider.Settings;
import android.text.*;
import android.view.*;
import android.widget.*;
import java.util.*;
public class MainActivity extends Activity {
  Switch sw; TextView perm; FrameLayout body; View settingsPage, libPage; TextView tabS, tabL;
  BaseAdapter ad; List<Store.Item> all, shown = new ArrayList<>(); int filter = 0; String query = ""; Set<Integer> favs;
  int m;
  protected void onCreate(Bundle b) {
    super.onCreate(b);
    getWindow().setStatusBarColor(Ui.BG); getWindow().setNavigationBarColor(Ui.BG);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    m = Ui.dp(this, 16); all = Store.load(this); favs = Store.favs(this);
    FrameLayout root = new FrameLayout(this); root.setBackgroundColor(Ui.BG); root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    root.addView(new Ui.Pattern2(this));
    LinearLayout col = new LinearLayout(this); col.setOrientation(LinearLayout.VERTICAL); col.setPadding(m, m * 2, m, 0);
    TextView h = tv("أذكار وأحاديث صحيحة", 26, Ui.GOLD); h.setGravity(Gravity.CENTER); col.addView(h);
    LinearLayout tabs = Ui.chips(this, new String[]{"الإعدادات", "الأذكار"}, 0, i -> showTab(i));
    LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2); tp.topMargin = m / 2; tp.bottomMargin = m / 2; col.addView(tabs, tp);
    body = new FrameLayout(this); col.addView(body, new LinearLayout.LayoutParams(-1, 0, 1));
    settingsPage = buildSettings(); libPage = buildLib(); body.addView(settingsPage); body.addView(libPage);
    root.addView(col, new FrameLayout.LayoutParams(-1, -1)); setContentView(root);
    showTab(0);
  }
  void showTab(int i) { settingsPage.setVisibility(i == 0 ? View.VISIBLE : View.GONE); libPage.setVisibility(i == 1 ? View.VISIBLE : View.GONE); }
  TextView label(String s) { TextView t = tv(s, 17, Ui.CREAM); t.setPadding(m / 2, m, m / 2, m / 3); return t; }
  View buildSettings() {
    ScrollView sc = new ScrollView(this); sc.setFillViewport(false);
    LinearLayout col = new LinearLayout(this); col.setOrientation(LinearLayout.VERTICAL); col.setPadding(0, 0, 0, m * 2);
    sw = new Switch(this); sw.setText("إظهار عند فتح الشاشة"); sw.setTextColor(Ui.CREAM); sw.setTextSize(18); sw.setTypeface(Ui.font(this)); sw.setPadding(m, m, m, m);
    sw.setChecked(Store.on(this));
    sw.setOnCheckedChangeListener((v, on) -> {
      if (on && !Settings.canDrawOverlays(this)) { v.setChecked(false); askOverlay(); return; }
      Store.set(this, on); Intent s = new Intent(this, UnlockService.class);
      if (on) { if (Build.VERSION.SDK_INT >= 26) startForegroundService(s); else startService(s); } else stopService(s);
    });
    col.addView(sw);
    perm = tv("", 15, Ui.GOLD); perm.setBackground(Ui.box(this, 0, Ui.GOLD, 12, 1)); perm.setPadding(m, m, m, m); perm.setGravity(Gravity.CENTER);
    perm.setOnClickListener(v -> askOverlay()); col.addView(perm);
    col.addView(label("عدد مرات الظهور"));
    col.addView(Ui.chips(this, new String[]{"مع كل فتح", "كل 3 مرات", "مرة كل ساعة"}, Store.freq(this), i -> Store.putInt(this, "freq", i)));
    col.addView(label("نوع المحتوى"));
    col.addView(Ui.chips(this, new String[]{"أذكار وأحاديث", "أذكار فقط", "أحاديث فقط"}, Store.cat(this), i -> Store.putInt(this, "cat", i)));
    TextView note = tv("تُغلق النافذة تلقائياً بعد 10 ثوانٍ، أو اضغط ✕", 14, Ui.GOLD); note.setAlpha(.7f); note.setPadding(m / 2, m, m / 2, m / 2); col.addView(note);
    TextView bat = tv("إذا توقفت النافذة بعد مرة: اضغط هنا ثم افتح «البطارية» واختر «بدون قيود»، وفعّل «التشغيل التلقائي» إن وجد", 14, Ui.GOLD);
    bat.setBackground(Ui.box(this, 0, Ui.GOLD, 12, 1)); bat.setPadding(m, m, m, m); bat.setGravity(Gravity.CENTER); bat.setAlpha(.8f);
    LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2); bp.topMargin = m / 2;
    bat.setOnClickListener(v -> { try { startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()))); }
      catch (Exception e) { startActivity(new Intent(Settings.ACTION_SETTINGS)); } });
    col.addView(bat, bp);
    TextView pv = tv("سياسة الخصوصية", 14, Ui.GOLD); pv.setGravity(Gravity.CENTER); pv.setPadding(m, m, m, m); pv.setAlpha(.8f);
    pv.setOnClickListener(v -> { TextView t = tv(PRIVACY, 15, Ui.CREAM); t.setPadding(m, m, m, m); ScrollView z = new ScrollView(this); z.addView(t);
      new AlertDialog.Builder(this).setTitle("سياسة الخصوصية").setView(z).setPositiveButton("حسناً", null).show(); });
    col.addView(pv, new LinearLayout.LayoutParams(-1, -2));
    sc.addView(col); return sc;
  }
  static final String PRIVACY = "التطبيق لا يجمع أي بيانات ولا يرسل أي شيء خارج جهازك، ولا يحتوي على إذن الإنترنت ولا إعلانات ولا حسابات.\n\nالمفضلة والإعدادات تُحفظ على جهازك فقط.\n\nالأذونات المستخدمة:\n• الظهور فوق التطبيقات (SYSTEM_ALERT_WINDOW): لعرض نافذة الذكر.\n• خدمة تعمل في المقدمة (FOREGROUND_SERVICE): لاستقبال فتح الشاشة.\n• التشغيل بعد إعادة التشغيل (RECEIVE_BOOT_COMPLETED): لإعادة تشغيل الخدمة بعد إقلاع الجهاز.\n\nيمكنك حذف كل بيانات التطبيق من إعدادات الجهاز أو بإلغاء تثبيته.";
  View buildLib() {
    LinearLayout col = new LinearLayout(this); col.setOrientation(LinearLayout.VERTICAL);
    EditText q = new EditText(this); q.setHint("بحث في الأذكار والأحاديث"); q.setHintTextColor(0x88F3E5AB); q.setTextColor(Ui.CREAM); q.setTypeface(Ui.font(this)); q.setTextSize(17);
    q.setSingleLine(true); q.setBackground(Ui.box(this, Ui.CARD, Ui.GOLD, 12, 1)); q.setPadding(m, m / 2, m, m / 2); q.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
    q.addTextChangedListener(new TextWatcher() {
      public void beforeTextChanged(CharSequence s, int a, int b, int c) {} public void onTextChanged(CharSequence s, int a, int b, int c) {}
      public void afterTextChanged(Editable e) { query = Ui.norm(e.toString().trim()); refresh(); }
    });
    col.addView(q, new LinearLayout.LayoutParams(-1, -2));
    LinearLayout fr = Ui.chips(this, new String[]{"الكل", "أذكار", "أحاديث", "★ المفضلة"}, 0, i -> { filter = i; refresh(); });
    LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(-1, -2); fp.topMargin = m / 2; col.addView(fr, fp);
    LinearLayout sz = new LinearLayout(this); sz.setGravity(Gravity.CENTER_VERTICAL);
    TextView szl = tv("حجم الخط", 15, Ui.CREAM); sz.addView(szl, new LinearLayout.LayoutParams(0, -2, 1));
    sz.addView(btn("A−", () -> { Store.putInt(this, "font", Math.max(14, Store.font(this) - 2)); ad.notifyDataSetChanged(); }));
    sz.addView(btn("A+", () -> { Store.putInt(this, "font", Math.min(40, Store.font(this) + 2)); ad.notifyDataSetChanged(); }));
    col.addView(sz);
    ListView lv = new ListView(this); lv.setDivider(null); lv.setClipToPadding(false); lv.setPadding(0, 0, 0, m);
    ad = new BaseAdapter() {
      public int getCount() { return shown.size(); } public Object getItem(int i) { return shown.get(i); } public long getItemId(int i) { return i; }
      public View getView(int i, View cv, ViewGroup p) { return card(shown.get(i)); }
    };
    lv.setAdapter(ad); col.addView(lv, new LinearLayout.LayoutParams(-1, 0, 1));
    refresh(); return col;
  }
  void refresh() {
    shown.clear();
    for (Store.Item it : all) {
      if (filter == 1 && it.cat != 'A') continue; if (filter == 2 && it.cat != 'H') continue; if (filter == 3 && !favs.contains(it.id)) continue;
      if (!query.isEmpty() && !Ui.norm(it.text + " " + it.src).contains(query)) continue;
      shown.add(it);
    }
    ad.notifyDataSetChanged();
  }
  View card(Store.Item it) {
    int fs = Store.font(this);
    LinearLayout c = new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL);
    c.setBackground(Ui.box(this, Ui.CARD, Ui.GOLD, 16, 1)); c.setPadding(m, m, m, m);
    TextView a = tv(it.text, fs, Ui.CREAM); a.setLineSpacing(0, 1.3f); TextView s = tv(it.src, Math.max(12, fs * 0.7f), Ui.GOLD); s.setPadding(0, m / 2, 0, 0);
    c.addView(a); c.addView(s);
    LinearLayout r = new LinearLayout(this);
    final TextView star = btn(favs.contains(it.id) ? "★" : "☆", null); star.setTextSize(22);
    star.setOnClickListener(v -> { boolean on = Store.toggleFav(this, it.id); favs = Store.favs(this); if (filter == 3) refresh(); else star.setText(on ? "★" : "☆"); });
    r.addView(star);
    String full = it.text + "\n" + it.src;
    r.addView(btn("نسخ", () -> { ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("t", full)); Toast.makeText(this, "تم النسخ", Toast.LENGTH_SHORT).show(); }));
    r.addView(btn("نص", () -> startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, full), null))));
    r.addView(btn("صورة", () -> ShareImg.share(this, it)));
    c.addView(r);
    FrameLayout w = new FrameLayout(this); w.setPadding(0, 0, 0, m / 2); w.addView(c); return w;
  }
  TextView tv(String s, float sz, int col) { return Ui.tv(this, s, sz, col); }
  TextView btn(String s, Runnable r) { TextView t = tv(s, 15, Ui.GOLD); t.setPadding(Ui.dp(this, 14), Ui.dp(this, 8), Ui.dp(this, 14), Ui.dp(this, 4)); if (r != null) t.setOnClickListener(v -> r.run()); return t; }
  void askOverlay() { startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()))); }
  protected void onResume() {
    super.onResume(); boolean ok = Settings.canDrawOverlays(this);
    perm.setText(ok ? "إذن العرض فوق التطبيقات: مفعّل ✓" : "اضغط هنا لتفعيل إذن «العرض فوق التطبيقات»"); perm.setAlpha(ok ? .6f : 1f);
    if (!ok && sw.isChecked()) sw.setChecked(false);
  }
}
