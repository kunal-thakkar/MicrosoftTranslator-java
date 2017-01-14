package com.microsoft;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class Try1 {

	public static void main(String[] args) throws IOException {
	    System.out.println(readText(new File("chineseChars.txt")));
	  }
	
	private static final Charset[] UTF_ENCODINGS = { Charset.forName("UTF-8"),
		      Charset.forName("UTF-16LE"), Charset.forName("UTF-16BE") };

		  private static Charset getEncoding(InputStream in) throws IOException {
		    charsetLoop: for (Charset encodings : UTF_ENCODINGS) {
		      byte[] bom = "\uFEFF".getBytes(encodings);
		      in.mark(bom.length);
		      for (byte b : bom) {
		        if ((0xFF & b) != in.read()) {
		          in.reset();
		          continue charsetLoop;
		        }
		      }
		      return encodings;
		    }
		    return Charset.defaultCharset();
		  }

		  private static String readText(File file) throws IOException {
		    Closer res = new Closer();
		    try {
		      InputStream in = res.using(new FileInputStream(file));
		      InputStream bin = res.using(new BufferedInputStream(in));
		      Reader reader = res.using(new InputStreamReader(bin, getEncoding(bin)));
		      StringBuilder out = new StringBuilder();
		      for (int ch = reader.read(); ch != -1; ch = reader.read())
		        out.append((char) ch);
		      return out.toString();
		    } finally {
		      res.close();
		    }
		  }

}
class Closer implements Closeable {
	  private Closeable closeable;

	  public <T extends Closeable> T using(T t) {
	    closeable = t;
	    return t;
	  }

	  @Override public void close() throws IOException {
	    if (closeable != null) {
	      closeable.close();
	    }
	  }
	}
