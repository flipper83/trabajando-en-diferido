package com.karumi.trabajandoendiferido.ui;

/**
 *
 */
public class ThreadColor {
  private final int offset;
  private final int size;
  private final int color;

  public ThreadColor(int offset, int size, int color) {
    this.offset = offset;
    this.size = size;
    this.color = color;
  }

  public int getOffset() {
    return offset;
  }

  public int getSize() {
    return size;
  }

  public int getColor() {
    return color;
  }
}
