package com.athkar.lock;
import android.content.*;
import android.graphics.*;
import android.net.Uri;
import android.text.*;
import java.io.*;
class ShareImg {
  static void share(Context c, Store.Item it) {
    try {
      final int W = 1080, M = 110;
      String text = it.text;
      TextPaint tp = new TextPaint(Paint.ANTI_ALIAS_FLAG); tp.setTypeface(Ui.font(c)); tp.setColor(Ui.CREAM);
      tp.setTextSize(text.length() > 260 ? 44 : text.length() > 140 ? 52 : 62);
      StaticLayout tl = new StaticLayout(text, tp, W - 2 * M, Layout.Alignment.ALIGN_CENTER, 1.45f, 0, false);
      TextPaint sp = new TextPaint(Paint.ANTI_ALIAS_FLAG); sp.setTypeface(Ui.font(c)); sp.setColor(Ui.GOLD); sp.setTextSize(40);
      StaticLayout sl = new StaticLayout(it.src, sp, W - 2 * M, Layout.Alignment.ALIGN_CENTER, 1.2f, 0, false);
      int top = 230, gap = 80, bottom = 260;
      int H = Math.max(1080, top + tl.getHeight() + gap + sl.getHeight() + bottom);
      Bitmap bm = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888); Canvas cv = new Canvas(bm);
      cv.drawColor(Ui.BG);
      Paint pp = new Paint(Paint.ANTI_ALIAS_FLAG); pp.setColor(0x1AD4AF37); pp.setStyle(Paint.Style.STROKE); pp.setStrokeWidth(2);
      Ui.drawPattern(cv, W, H, 84, pp);
      RectF r = new RectF(50, 50, W - 50, H - 50);
      Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG); fill.setColor(Ui.CARD); cv.drawRoundRect(r, 48, 48, fill);
      Paint st = new Paint(Paint.ANTI_ALIAS_FLAG); st.setColor(Ui.GOLD); st.setStyle(Paint.Style.STROKE); st.setStrokeWidth(4); cv.drawRoundRect(r, 48, 48, st);
      st.setStrokeWidth(2); st.setAlpha(120); cv.drawRoundRect(new RectF(72, 72, W - 72, H - 72), 38, 38, st);
      // ornament: line with a diamond in the middle
      Paint o = new Paint(Paint.ANTI_ALIAS_FLAG); o.setColor(Ui.GOLD); o.setStrokeWidth(3);
      float cx = W / 2f, oy = 150; cv.drawLine(cx - 260, oy, cx - 40, oy, o); cv.drawLine(cx + 40, oy, cx + 260, oy, o);
      Path d = new Path(); d.moveTo(cx, oy - 22); d.lineTo(cx + 22, oy); d.lineTo(cx, oy + 22); d.lineTo(cx - 22, oy); d.close(); o.setStyle(Paint.Style.FILL); cv.drawPath(d, o);
      int ch = tl.getHeight() + gap + sl.getHeight(); int y0 = top + Math.max(0, (H - bottom - top - ch) / 2);
      cv.save(); cv.translate(M, y0); tl.draw(cv); cv.restore();
      cv.save(); cv.translate(M, y0 + tl.getHeight() + gap); sl.draw(cv); cv.restore();
      float by = H - 190; o.setStyle(Paint.Style.STROKE); cv.drawLine(cx - 260, by, cx + 260, by, o);
      TextPaint fp = new TextPaint(Paint.ANTI_ALIAS_FLAG); fp.setTypeface(Ui.font(c)); fp.setColor(Ui.GOLD); fp.setAlpha(170); fp.setTextSize(36); fp.setTextAlign(Paint.Align.CENTER);
      cv.drawText("أذكار وأحاديث صحيحة", cx, by + 58, fp);
      File dir = new File(c.getCacheDir(), "share"); dir.mkdirs();
      for (File f : dir.listFiles()) f.delete();
      File out = new File(dir, "thikr" + it.id + ".png");
      try (FileOutputStream fo = new FileOutputStream(out)) { bm.compress(Bitmap.CompressFormat.PNG, 100, fo); }
      bm.recycle();
      Uri u = Uri.parse("content://" + c.getPackageName() + ".share/" + out.getName());
      Intent x = new Intent(Intent.ACTION_SEND).setType("image/png").putExtra(Intent.EXTRA_STREAM, u);
      x.setClipData(ClipData.newRawUri("", u)); x.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      c.startActivity(Intent.createChooser(x, null).addFlags(c instanceof android.app.Activity ? 0 : Intent.FLAG_ACTIVITY_NEW_TASK));
    } catch (Exception e) { android.widget.Toast.makeText(c, "تعذر إنشاء الصورة", android.widget.Toast.LENGTH_SHORT).show(); }
  }
}
