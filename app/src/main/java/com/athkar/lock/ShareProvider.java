package com.athkar.lock;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import java.io.File;
import java.io.FileNotFoundException;
public class ShareProvider extends ContentProvider {
  public boolean onCreate() { return true; }
  File f(Uri u) { return new File(new File(getContext().getCacheDir(), "share"), new File(u.getLastPathSegment()).getName()); }
  public ParcelFileDescriptor openFile(Uri u, String m) throws FileNotFoundException { return ParcelFileDescriptor.open(f(u), ParcelFileDescriptor.MODE_READ_ONLY); }
  public Cursor query(Uri u, String[] p, String s, String[] a, String o) {
    MatrixCursor c = new MatrixCursor(new String[]{OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE});
    File x = f(u); c.addRow(new Object[]{x.getName(), x.length()}); return c;
  }
  public String getType(Uri u) { return "image/png"; }
  public Uri insert(Uri u, ContentValues v) { return null; }
  public int delete(Uri u, String s, String[] a) { return 0; }
  public int update(Uri u, ContentValues v, String s, String[] a) { return 0; }
}
