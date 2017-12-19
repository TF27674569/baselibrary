package org.log.mode;

public interface LogStrategy {

  void log(int priority, String tag, String message);
}
