package com.athkar.lock;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;
import android.widget.LinearLayout;
import java.util.regex.Pattern;
class Ui {
  static final int BG = 0xFF0A1410, GOLD = 0xFFD4AF37, CREAM = 0xFFF3E5AB, CARD = 0xFF0F1D17;
  interface Cb { void on(int i); }
  static Typeface tf;
  static Typeface font(Context c) { if (tf == null) { try { tf = Typeface.createFromAsset(c.getAssets(), "amiri.ttf"); } catch (Exception e) { tf = Typeface.DEFAULT; } } return tf; }
  static int dp(Context c, float v) { return (int) (v * c.getResources().getDisplayMetrics().density + .5f); }
  static GradientDrawable box(Context c, int fill, int stroke, float rad, float w) {
    GradientDrawable g = new GradientDrawable(); g.setColor(fill); g.setStroke(dp(c, w), stroke); g.setCornerRadius(dp(c, rad)); return g;
  }
  static final Pattern MARKS = Pattern.compile("[\\u064B-\\u0652\\u0670\\u0640]");
  static String norm(String s) {
    s = MARKS.matcher(s).replaceAll("");
    return s.replace('أ', 'ا').replace('إ', 'ا').replace('آ', 'ا').replace('ى', 'ي').replace('ة', 'ه').toLowerCase();
  }
  static void drawPattern(Canvas cv, int w, int h, int s, Paint p) {
    int hh = s / 2; Path q = new Path();
    for (int y = 0; y < h + s; y += s) for (int x = 0; x < w + s; x += s) {
      q.reset(); q.moveTo(x, y - hh); q.lineTo(x + hh, y); q.lineTo(x, y + hh); q.lineTo(x - hh, y); q.close(); cv.drawPath(q, p);
      cv.drawCircle(x, y, hh * .45f, p);
    }
  }
  static class Pattern2 extends View {
    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    Pattern2(Context c) { super(c); p.setColor(0x1AD4AF37); p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(2); }
    protected void onDraw(Canvas cv) { drawPattern(cv, getWidth(), getHeight(), dp(getContext(), 56), p); }
  }
  static TextView tv(Context c, String s, float sz, int col) {
    TextView t = new TextView(c); t.setText(s); t.setTextSize(sz); t.setTextColor(col); t.setTypeface(font(c)); return t;
  }
  // a row of selectable chips
  static LinearLayout chips(Context c, String[] labels, int sel, Cb cb) {
    LinearLayout r = new LinearLayout(c); r.setOrientation(LinearLayout.HORIZONTAL);
    final TextView[] ts = new TextView[labels.length];
    for (int i = 0; i < labels.length; i++) {
      TextView t = tv(c, labels[i], 14, Ui.GOLD); t.setGravity(android.view.Gravity.CENTER);
      t.setPadding(dp(c, 6), dp(c, 10), dp(c, 6), dp(c, 8));
      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2, 1); lp.setMargins(dp(c, 3), 0, dp(c, 3), 0);
      ts[i] = t; r.addView(t, lp); final int k = i;
      t.setOnClickListener(v -> { paint(c, ts, k); cb.on(k); });
    }
    paint(c, ts, sel); return r;
  }
  static void paint(Context c, TextView[] ts, int sel) {
    for (int i = 0; i < ts.length; i++) {
      boolean s = i == sel; ts[i].setTextColor(s ? BG : GOLD);
      ts[i].setBackground(box(c, s ? GOLD : 0, GOLD, 12, 1));
    }
  }
}
