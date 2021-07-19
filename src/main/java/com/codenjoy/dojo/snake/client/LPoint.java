package com.codenjoy.dojo.snake.client;

public class LPoint {
  public final int x;
  public final int y;

  public LPoint(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public static LPoint of(int x, int y) {
    return new LPoint(x, y);
  }

  public LPoint move(LPoint delta) {
    return LPoint.of(
        x + delta.x,
        y + delta.y
    );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LPoint lPoint = (LPoint) o;

    if (x != lPoint.x) return false;
    return y == lPoint.y;
  }

  @Override
  public int hashCode() { // int size: 0..2^32
    return x << 16 + y;   // 0..2^16
  }

  @Override
  public String toString() {
    return String.format("[%2d:%2d]", x, y);
  }

  public static void main(String[] args) {
    LPoint p1 = new LPoint(1, 2);
    LPoint p2 = LPoint.of(1, 2);
  }

}
