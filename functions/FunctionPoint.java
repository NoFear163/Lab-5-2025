package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private double x;
    private double y;

    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public FunctionPoint() {
        this(0, 0);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    // 1. Переопределение toString()
    @Override
    public String toString() {
        return String.format("(%.2f; %.2f)", x, y);
    }

    // 2. Переопределение equals()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionPoint that = (FunctionPoint) o;

        // Сравнение чисел с плавающей точкой с учетом погрешности
        final double EPS = 1e-10;
        return Math.abs(x - that.x) < EPS && Math.abs(y - that.y) < EPS;
    }

    // 3. Переопределение hashCode()
    @Override
    public int hashCode() {
        // Преобразование double в long bits
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);

        // Разделение каждого long на два int (старшие и младшие 32 бита)
        int x1 = (int)(xBits >> 32);        // Старшие 32 бита x
        int x2 = (int)(xBits & 0xFFFFFFFFL); // Младшие 32 бита x
        int y1 = (int)(yBits >> 32);        // Старшие 32 бита y
        int y2 = (int)(yBits & 0xFFFFFFFFL); // Младшие 32 бита y

        // XOR всех компонентов
        return x1 ^ x2 ^ y1 ^ y2;
    }

    // 4. Переопределение clone()
    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            return super.clone(); // Простое клонирование (shallow copy), так как нет ссылок на объекты
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Не может произойти, так как мы реализуем Cloneable
        }
    }

    // Дополнительный метод для получения копии (без исключений)
    public FunctionPoint copy() {
        return new FunctionPoint(this);
    }
}