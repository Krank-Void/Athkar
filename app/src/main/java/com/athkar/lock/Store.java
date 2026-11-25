package com.athkar.lock;
import android.content.Context;
import android.content.SharedPreferences;
import java.io.*;
import java.util.*;
class Store {
  static class Item { int id; char cat; String text, src; }
  static List<Item> cache;
  static synchronized List<Item> load(Context c) {
    if (cache != null) return cache;
    List<Item> l = new ArrayList<>();
    try (BufferedReader r = new BufferedReader(new InputStreamReader(c.getAssets().open("athkar.txt"), "UTF-8"))) {
      String s; int n = 0;
      while ((s = r.readLine()) != null) {
        int a = s.indexOf('|'), b = s.lastIndexOf('|');
        if (a == 1 && b > a) { Item it = new Item(); it.id = n++; it.cat = s.charAt(0); it.text = s.substring(a + 1, b); it.src = s.substring(b + 1); l.add(it); }
      }
    } catch (Exception e) {}
    cache = l; return l;
  }
  static SharedPreferences p(Context c) { return c.getSharedPreferences("p", 0); }
  static boolean on(Context c) { return p(c).getBoolean("on", false); }
  static void set(Context c, boolean v) { p(c).edit().putBoolean("on", v).apply(); }
  static int freq(Context c) { return p(c).getInt("freq", 0); }
  static int cat(Context c) { return p(c).getInt("cat", 0); }
  static int font(Context c) { return p(c).getInt("font", 20); }
  static void putInt(Context c, String k, int v) { p(c).edit().putInt(k, v).apply(); }
  static Set<Integer> favs(Context c) {
    Set<Integer> s = new HashSet<>();
    for (String x : p(c).getString("favs", "").split(",")) if (!x.isEmpty()) try { s.add(Integer.parseInt(x)); } catch (Exception e) {}
    return s;
  }
  static boolean toggleFav(Context c, int id) {
    Set<Integer> s = favs(c); boolean now = !s.remove(id); if (now) s.add(id);
    StringBuilder b = new StringBuilder(); for (int x : s) b.append(x).append(',');
    p(c).edit().putString("favs", b.toString()).apply(); return now;
  }
  // true when the popup is due under the chosen frequency
  static boolean due(Context c) {
    int f = freq(c);
    if (f == 0) return true;
    SharedPreferences sp = p(c);
    if (f == 1) { int n = sp.getInt("cnt", 0); sp.edit().putInt("cnt", n + 1).apply(); return n % 3 == 0; }
    long now = System.currentTimeMillis();
    if (now - sp.getLong("last", 0) >= 3600000L || now < sp.getLong("last", 0)) { sp.edit().putLong("last", now).apply(); return true; }
    return false;
  }
}
