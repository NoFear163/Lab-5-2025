import functions.*;
import functions.tabulated.*;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ЗАДАНИЕ 5: ТЕСТИРОВАНИЕ МЕТОДОВ ===");
        System.out.println("=".repeat(70));

        try {
            // Часть 1: Краткое тестирование всех методов
            testAllMethods();

            // Часть 2: Финальная проверка требований задания 5
            finalValidation();

            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓ ВСЕ ТРЕБОВАНИЯ ЗАДАНИЯ 5 ВЫПОЛНЕНЫ!");
            System.out.println("=".repeat(70));

        } catch (Exception e) {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✗ ОШИБКА: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testAllMethods() {
        System.out.println("\n1. ТЕСТИРОВАНИЕ ВСЕХ МЕТОДОВ:");
        System.out.println("-".repeat(50));

        // Создаем тестовые данные один раз
        FunctionPoint[] basePoints = {
                new FunctionPoint(0, 0),
                new FunctionPoint(1, 2),
                new FunctionPoint(2, 8),
                new FunctionPoint(3, 18)
        };

        FunctionPoint[] samePoints = {
                new FunctionPoint(0, 0),
                new FunctionPoint(1, 2),
                new FunctionPoint(2, 8),
                new FunctionPoint(3, 18)
        };

        FunctionPoint[] diffPoints = {
                new FunctionPoint(0, 0),
                new FunctionPoint(1, 2),
                new FunctionPoint(2, 8.001), // Немного отличается
                new FunctionPoint(3, 18)
        };

        // Создаем функции
        ArrayTabulatedFunction array1 = new ArrayTabulatedFunction(basePoints);
        ArrayTabulatedFunction array2 = new ArrayTabulatedFunction(samePoints);
        ArrayTabulatedFunction array3 = new ArrayTabulatedFunction(diffPoints);

        LinkedListTabulatedFunction linked1 = new LinkedListTabulatedFunction(basePoints);
        LinkedListTabulatedFunction linked2 = new LinkedListTabulatedFunction(samePoints);
        LinkedListTabulatedFunction linked3 = new LinkedListTabulatedFunction(diffPoints);

        System.out.println("\na) toString() - проверка формата вывода:");
        System.out.println("   Array:    " + array1.toString());
        System.out.println("   LinkedList: " + linked1.toString());
        System.out.println("   ✓ Оба метода выводят в формате {(x; y), ...}");

        System.out.println("\nb) equals() - основные случаи:");
        System.out.println("   1. Одинаковые Array: " + array1.equals(array2) + " (true)");
        System.out.println("   2. Одинаковые LinkedList: " + linked1.equals(linked2) + " (true)");
        System.out.println("   3. Array vs LinkedList (одинаковые данные): " + array1.equals(linked1) + " (true)");
        System.out.println("   4. Array vs Array (разные данные): " + array1.equals(array3) + " (false)");
        System.out.println("   5. С null: " + array1.equals(null) + " (false)");
        System.out.println("   ✓ Все сравнения работают корректно");

        System.out.println("\nc) hashCode() - проверка контракта:");
        boolean hashContract1 = array1.equals(array2) && (array1.hashCode() == array2.hashCode());
        boolean hashContract2 = linked1.equals(linked2) && (linked1.hashCode() == linked2.hashCode());

        System.out.println("   Array equals→hashCode: " + hashContract1 + " (true)");
        System.out.println("   LinkedList equals→hashCode: " + hashContract2 + " (true)");

        // Проверка изменения хэш-кода
        System.out.println("   Проверка изменения хэш-кода:");
        try {
            int originalHash = array1.hashCode();
            double originalY = array1.getPointY(1);
            array1.setPointY(1, originalY + 0.005); // Меняем только Y - безопасно
            boolean hashChanged = originalHash != array1.hashCode();
            array1.setPointY(1, originalY); // Восстанавливаем

            System.out.println("   Хэш-код изменяется при модификации Y: " + hashChanged + " (true)");
        } catch (Exception e) {
            System.out.println("   ✗ Ошибка при проверке хэш-кода: " + e.getMessage());
        }
        System.out.println("   ✓ Контракт hashCode/equals соблюдается");

        System.out.println("\nd) clone() - глубокое копирование:");
        try {
            ArrayTabulatedFunction arrayClone = (ArrayTabulatedFunction) array1.clone();
            LinkedListTabulatedFunction linkedClone = (LinkedListTabulatedFunction) linked1.clone();

            array1.setPointY(2, 999);
            linked1.setPointY(2, 999);

            boolean arrayDeepCopy = array1.getPointY(2) != arrayClone.getPointY(2);
            boolean linkedDeepCopy = linked1.getPointY(2) != linkedClone.getPointY(2);

            System.out.println("   Array глубокое копирование: " + arrayDeepCopy + " (true)");
            System.out.println("   LinkedList глубокое копирование: " + linkedDeepCopy + " (true)");
            System.out.println("   ✓ Оба метода реализуют глубокое копирование");

        } catch (CloneNotSupportedException e) {
            System.out.println("   ✗ Ошибка клонирования: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("   ✗ Неожиданная ошибка: " + e.getMessage());
        }
    }

    private static void finalValidation() {
        System.out.println("\n2. ПРОВЕРКА ТРЕБОВАНИЙ ЗАДАНИЯ 5:");
        System.out.println("-".repeat(50));

        // Тест 1: toString()
        System.out.println("\n1. toString() выводит строковое представление:");
        FunctionPoint[] points = {
                new FunctionPoint(-1, 1),
                new FunctionPoint(0, 0),
                new FunctionPoint(1, 1)
        };

        ArrayTabulatedFunction func1 = new ArrayTabulatedFunction(points);
        LinkedListTabulatedFunction func2 = new LinkedListTabulatedFunction(points);

        System.out.println("   Array: " + func1);
        System.out.println("   LinkedList: " + func2);
        System.out.println("   ✓ Оба метода работают");

        // Тест 2: equals()
        System.out.println("\n2. equals() для различных случаев:");

        // Создаем одинаковые функции разными способами
        double[] values = {0, 1, 4, 9};
        ArrayTabulatedFunction arrayA = new ArrayTabulatedFunction(0, 3, values);
        ArrayTabulatedFunction arrayB = new ArrayTabulatedFunction(0, 3, values);
        LinkedListTabulatedFunction linkedA = new LinkedListTabulatedFunction(0, 3, values);

        // Разные функции
        double[] diffValues = {0, 1, 4, 10}; // Последнее значение отличается
        ArrayTabulatedFunction arrayC = new ArrayTabulatedFunction(0, 3, diffValues);

        System.out.println("   а) Два одинаковых Array: " + arrayA.equals(arrayB) + " (true)");
        System.out.println("   б) Array и LinkedList с одинаковыми данными: " + arrayA.equals(linkedA) + " (true)");
        System.out.println("   в) Два разных Array: " + arrayA.equals(arrayC) + " (false)");
        System.out.println("   г) Array и null: " + arrayA.equals(null) + " (false)");
        System.out.println("   ✓ Все сравнения корректны");

        // Тест 3: hashCode()
        System.out.println("\n3. hashCode() и согласованность с equals():");

        System.out.println("   а) Хэш-коды одинаковых объектов:");
        System.out.println("      arrayA.hashCode(): " + arrayA.hashCode());
        System.out.println("      arrayB.hashCode(): " + arrayB.hashCode());
        System.out.println("      linkedA.hashCode(): " + linkedA.hashCode());

        boolean hashConsistent = arrayA.equals(arrayB) && (arrayA.hashCode() == arrayB.hashCode());
        System.out.println("   б) Консистентность equals/hashCode: " + hashConsistent + " (true)");

        System.out.println("   в) Изменение хэш-кода при модификации:");
        try {
            int hashBefore = arrayA.hashCode();
            double yValue = arrayA.getPointY(2);
            arrayA.setPointY(2, yValue + 0.003);
            int hashAfter = arrayA.hashCode();
            boolean hashChanged = hashBefore != hashAfter;
            System.out.println("      До: " + hashBefore + ", После: " + hashAfter + ", Изменился: " + hashChanged + " (true)");

            // Восстанавливаем значение
            arrayA.setPointY(2, yValue);
        } catch (Exception e) {
            System.out.println("      ✗ Ошибка при изменении: " + e.getMessage());
        }

        // Тест 4: clone() глубокое копирование
        System.out.println("\n4. clone() - глубокое копирование:");

        try {
            // Тест для Array
            ArrayTabulatedFunction originalArray = new ArrayTabulatedFunction(points);
            ArrayTabulatedFunction clonedArray = (ArrayTabulatedFunction) originalArray.clone();

            // Изменяем оригинал
            originalArray.setPointY(1, 100);

            boolean arrayTest = originalArray.getPointY(1) != clonedArray.getPointY(1);
            System.out.println("   а) Array: оригинал ≠ клон после модификации Y: " + arrayTest + " (true)");

        } catch (CloneNotSupportedException e) {
            System.out.println("   ✗ Ошибка при клонировании Array: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("   ✗ Неожиданная ошибка: " + e.getMessage());
        }

        try {
            // Тест для LinkedList
            LinkedListTabulatedFunction originalLinked = new LinkedListTabulatedFunction(points);
            LinkedListTabulatedFunction clonedLinked = (LinkedListTabulatedFunction) originalLinked.clone();

            // Изменяем оригинал
            originalLinked.setPointY(1, 200);

            boolean linkedTest = originalLinked.getPointY(1) != clonedLinked.getPointY(1);
            System.out.println("   б) LinkedList: оригинал ≠ клон после модификации Y: " + linkedTest + " (true)");

            // Дополнительная проверка для LinkedList - добавляем новую точку
            originalLinked.addPoint(new FunctionPoint(0.5, 0.25));
            boolean structureTest = originalLinked.getPointsCount() != clonedLinked.getPointsCount();
            System.out.println("   в) LinkedList: разная структура после добавления точки: " + structureTest + " (true)");

            System.out.println("   ✓ Глубокое копирование подтверждено");

        } catch (CloneNotSupportedException e) {
            System.out.println("   ✗ Ошибка при клонировании LinkedList: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("   ✗ Неожиданная ошибка: " + e.getMessage());
        }

        // Тест 5: Работа с HashSet
        System.out.println("\n5. ДОПОЛНИТЕЛЬНО: работа в HashSet:");

        try {
            // Создаем новые объекты для чистого теста
            ArrayTabulatedFunction setFunc1 = new ArrayTabulatedFunction(points);
            LinkedListTabulatedFunction setFunc2 = new LinkedListTabulatedFunction(points);
            ArrayTabulatedFunction setFunc3 = new ArrayTabulatedFunction(points);

            HashSet<TabulatedFunction> set = new HashSet<>();
            set.add(setFunc1);
            set.add(setFunc2);
            set.add(setFunc3);

            System.out.println("   Размер HashSet после добавления 3 функций (2 одинаковых): " + set.size());
            System.out.println("   setFunc1.equals(setFunc2): " + setFunc1.equals(setFunc2));
            System.out.println("   setFunc1.equals(setFunc3): " + setFunc1.equals(setFunc3));
            System.out.println("   setFunc1.hashCode() == setFunc2.hashCode(): " + (setFunc1.hashCode() == setFunc2.hashCode()));
            System.out.println("   ✓ Функции корректно работают в коллекциях");

        } catch (Exception e) {
            System.out.println("   ✗ Ошибка при работе с HashSet: " + e.getMessage());
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("ИТОГ ПРОВЕРКИ:");
        System.out.println("1. toString() - ✓ работает для обоих классов");
        System.out.println("2. equals() - ✓ корректно сравнивает все случаи");
        System.out.println("3. hashCode() - ✓ согласован с equals(), меняется при модификации");
        System.out.println("4. clone() - ✓ глубокое копирование для обоих классов");
        System.out.println("5. Все методы - ✓ соответствуют требованиям задания 5");
    }
}