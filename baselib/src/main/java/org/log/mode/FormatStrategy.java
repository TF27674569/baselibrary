package org.log.mode;

public interface FormatStrategy {

  void log(int priority, String tag, String message);
}
