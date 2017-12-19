package org.log.mode;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CsvFormatStrategy implements FormatStrategy {

  private static final String NEW_LINE = System.getProperty("line.separator");
  private static final String NEW_LINE_REPLACEMENT = " <br> ";
  private static final String SEPARATOR = ",";

  private final Date date;
  private final SimpleDateFormat dateFormat;
  private final LogStrategy logStrategy;
  private final String tag;

  private CsvFormatStrategy(Builder builder) {
    date = builder.date;
    dateFormat = builder.dateFormat;
    logStrategy = builder.logStrategy;
    tag = builder.tag;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  @Override public void log(int priority, String onceOnlyTag, String message) {
    String tag = formatTag(onceOnlyTag);

    date.setTime(System.currentTimeMillis());

    StringBuilder builder = new StringBuilder();

    builder.append(Long.toString(date.getTime()));

    builder.append(SEPARATOR);
    builder.append(dateFormat.format(date));

    builder.append(SEPARATOR);
    builder.append(Utils.logLevel(priority));

    builder.append(SEPARATOR);
    builder.append(tag);

    // 是否需要新的一行
    if (message.contains(NEW_LINE)) {
      message = message.replaceAll(NEW_LINE, NEW_LINE_REPLACEMENT);
    }
    builder.append(SEPARATOR);
    builder.append(message);

    // 添加新的一行
    builder.append(NEW_LINE);

    logStrategy.log(priority, tag, builder.toString());
  }

  private String formatTag(String tag) {
    if (!Utils.isEmpty(tag) && !Utils.equals(this.tag, tag)) {
      return this.tag + "-" + tag;
    }
    return this.tag;
  }

  public static final class Builder {
    private static final int MAX_BYTES = 500 * 1024; // 一行最大的字节

    Date date;
    SimpleDateFormat dateFormat;
    LogStrategy logStrategy;
    String tag = "PRETTY_LOGGER";

    private Builder() {
    }

    public Builder date(Date val) {
      date = val;
      return this;
    }

    public Builder dateFormat(SimpleDateFormat val) {
      dateFormat = val;
      return this;
    }

    public Builder logStrategy(LogStrategy val) {
      logStrategy = val;
      return this;
    }

    public Builder tag(String tag) {
      this.tag = tag;
      return this;
    }

    public CsvFormatStrategy build() {
      if (date == null) {
        date = new Date();
      }
      if (dateFormat == null) {
        dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.UK);
      }
      if (logStrategy == null) {
        String diskPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder = diskPath + File.separatorChar + "log";

        HandlerThread ht = new HandlerThread("AndroidFileLogger." + folder);
        ht.start();
        Handler handler = new DiskLogStrategy.WriteHandler(ht.getLooper(), folder, MAX_BYTES);
        logStrategy = new DiskLogStrategy(handler);
      }
      return new CsvFormatStrategy(this);
    }
  }
}
