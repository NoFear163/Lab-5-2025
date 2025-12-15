package functions;

import functions.tabulated.ArrayTabulatedFunction;
import functions.tabulated.LinkedListTabulatedFunction;
import java.io.*;

public final class TabulatedFunctions {

    private TabulatedFunctions() {
        throw new UnsupportedOperationException("Нельзя создавать экземпляры утилитного класса");
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        if (leftX < function.getLeftDomainBorder() ||
                rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интервал табулирования [" + leftX + ", " + rightX + "] " + "выходит за область определения функции [" + function.getLeftDomainBorder() + ", " + function.getRightDomainBorder() + "]"
            );
        }

        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        return new ArrayTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction tabulateLinkedList(Function function, double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        if (leftX < function.getLeftDomainBorder() ||
                rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException(
                    "Интервал табулирования выходит за область определения функции"
            );
        }

        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        return new LinkedListTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return new ArrayTabulatedFunction(points);
    }

    public static TabulatedFunction createTabulatedFunctionLinkedList(FunctionPoint[] points) {
        return new LinkedListTabulatedFunction(points);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out)
            throws IOException {

        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeInt(function.getPointsCount());

        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }

        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in)
            throws IOException {

        DataInputStream dataIn = new DataInputStream(in);
        int pointsCount = dataIn.readInt();

        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        for (int i = 1; i < pointsCount; i++) {
            if (points[i].getX() <= points[i-1].getX()) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по X");
            }
        }

        return new ArrayTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out)
            throws IOException {

        PrintWriter writer = new PrintWriter(out);
        writer.print(function.getPointsCount());

        for (int i = 0; i < function.getPointsCount(); i++) {
            writer.print(" " + function.getPointX(i));
            writer.print(" " + function.getPointY(i));
        }

        writer.println();
        writer.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in)
            throws IOException {

        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.resetSyntax();
        tokenizer.wordChars('0', '9');
        tokenizer.wordChars('.', '.');
        tokenizer.wordChars('-', '-');
        tokenizer.wordChars('+', '+');
        tokenizer.wordChars('e', 'e');
        tokenizer.wordChars('E', 'E');
        tokenizer.whitespaceChars(' ', ' ');
        tokenizer.whitespaceChars('\t', '\t');
        tokenizer.whitespaceChars('\n', '\n');
        tokenizer.whitespaceChars('\r', '\r');

        tokenizer.nextToken();
        if (tokenizer.ttype != StreamTokenizer.TT_WORD) {
            throw new IOException("Ожидалось количество точек");
        }

        int pointsCount;
        try {
            pointsCount = Integer.parseInt(tokenizer.sval);
        } catch (NumberFormatException e) {
            throw new IOException("Некорректное количество точек: " + tokenizer.sval);
        }

        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            tokenizer.nextToken();
            if (tokenizer.ttype != StreamTokenizer.TT_WORD) {
                throw new IOException("Ожидалось значение X для точки " + (i+1));
            }

            double x;
            try {
                x = Double.parseDouble(tokenizer.sval);
            } catch (NumberFormatException e) {
                throw new IOException("Некорректное значение X: " + tokenizer.sval);
            }

            tokenizer.nextToken();
            if (tokenizer.ttype != StreamTokenizer.TT_WORD) {
                throw new IOException("Ожидалось значение Y для точки " + (i+1));
            }

            double y;
            try {
                y = Double.parseDouble(tokenizer.sval);
            } catch (NumberFormatException e) {
                throw new IOException("Некорректное значение Y: " + tokenizer.sval);
            }

            points[i] = new FunctionPoint(x, y);
        }

        for (int i = 1; i < pointsCount; i++) {
            if (points[i].getX() <= points[i-1].getX()) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по X");
            }
        }

        return new ArrayTabulatedFunction(points);
    }

    public static void outputTabulatedFunctionToFile(TabulatedFunction function, String filename)
            throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            outputTabulatedFunction(function, fos);
        }
    }

    public static TabulatedFunction inputTabulatedFunctionFromFile(String filename)
            throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            return inputTabulatedFunction(fis);
        }
    }

    public static void writeTabulatedFunctionToFile(TabulatedFunction function, String filename)
            throws IOException {
        try (FileWriter fw = new FileWriter(filename)) {
            writeTabulatedFunction(function, fw);
        }
    }

    public static TabulatedFunction readTabulatedFunctionFromFile(String filename)
            throws IOException {
        try (FileReader fr = new FileReader(filename)) {
            return readTabulatedFunction(fr);
        }
    }

    public static void serialize(TabulatedFunction function, String filename)
            throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(filename)))) {
            oos.writeObject(function);
        }
    }

    public static TabulatedFunction deserialize(String filename)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(filename)))) {
            return (TabulatedFunction) ois.readObject();
        }
    }

    public static void externalize(TabulatedFunction function, String filename)
            throws IOException {
        if (!(function instanceof Externalizable)) {
            throw new IllegalArgumentException("Функция должна реализовывать Externalizable");
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(filename)))) {
            oos.writeObject(function);
        }
    }

    public static TabulatedFunction externalizeRead(String filename)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(filename)))) {
            return (TabulatedFunction) ois.readObject();
        }
    }

    public static void print(TabulatedFunction function) {
        System.out.println("Табулированная функция:");
        System.out.printf("  Область определения: [%.4f, %.4f]\n",
                function.getLeftDomainBorder(),
                function.getRightDomainBorder());
        System.out.println("  Количество точек: " + function.getPointsCount());

        System.out.println("  Точки функции:");
        for (int i = 0; i < function.getPointsCount(); i++) {
            System.out.printf("    [%3d] x = %10.6f, y = %10.6f\n",
                    i, function.getPointX(i), function.getPointY(i));
        }
    }

    public static void compare(TabulatedFunction f1, TabulatedFunction f2) {
        System.out.println("Сравнение функций:");

        if (f1.getPointsCount() != f2.getPointsCount()) {
            System.out.println("  ВНИМАНИЕ: разное количество точек!");
        }

        double maxDiff = 0;
        int diffCount = 0;

        for (int i = 0; i < Math.min(f1.getPointsCount(), f2.getPointsCount()); i++) {
            double x1 = f1.getPointX(i);
            double y1 = f1.getPointY(i);

            double y2 = f2.getFunctionValue(x1);

            if (!Double.isNaN(y2)) {
                double diff = Math.abs(y1 - y2);
                if (diff > maxDiff) {
                    maxDiff = diff;
                }

                if (diff > 1e-10) {
                    diffCount++;
                }
            }
        }

        System.out.printf("  Максимальная разница: %e\n", maxDiff);
        System.out.println("  Точек с различием > 1e-10: " + diffCount);

        if (maxDiff < 1e-10) {
            System.out.println("  ✓ Функции практически идентичны");
        } else {
            System.out.println("  ✗ Обнаружены значительные различия");
        }
    }
}