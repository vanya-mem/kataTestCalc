import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class NegativeRimNum extends Exception {
    public String toString() {
        return "В римской системе счисления нет отрицательных чисел";
    }
}

class DifferentBases extends Exception {
    public String toString() {
        return "Используются разные системы счисления";
    }
}

class OnlyString extends Exception {
    public String toString() {
        return "Строка не является математической операцией";
    }
}

class MoreThanTwoOperands extends Exception {
    public String toString() {
        return "Формат мат. операции не удовлетворяет заданию - два операнда и один оператор";
    }
}

class NonInteger extends Exception {
    public String toString() {
        return "Можно использовать только целые числа";
    }
}

class MoreThanTenOrZero extends Exception {
    public String toString() {
        return "Число больше десяти или равно нулю";
    }
}

public class Main {

    private static final List<String> operationTypes = Arrays.asList("+", "-", "*", "/");
    private static final String rimsAlphabetWork = "IVX";

    public static String charsToUpperCase(String str) {
        String newString = "";
        for (int i = 0; i < str.length(); i++) {
            String elem = "" + str.charAt(i);
            if (rimsAlphabetWork.contains(elem.toUpperCase())) {
                newString += elem.toUpperCase();
            }
            else {
                newString += elem;
            }
        }
        return newString;
    }

    public static int calcRimsToArabic(String num) {
        int rimToArabicNum = 0;
        for (int i = 0; i < num.length(); i++) {
            if (num.charAt(i) == 'I' && num.length() == 1)
                rimToArabicNum++;
            else if(num.charAt(i) == 'I' && num.length() > 1 && num.charAt(1) != 'V' && num.charAt(1) != 'X')
                rimToArabicNum ++;
            else if(num.charAt(i) == 'I' && num.charAt(i + 1) == 'V')
                rimToArabicNum += 4;
            else if (num.charAt(i) == 'V' && num.charAt(0) != 'I')
                rimToArabicNum += 5;
            else if(num.charAt(i) == 'I' && num.charAt(i + 1) == 'X')
                rimToArabicNum += 9;
            else if (num.charAt(i) == 'X' && num.charAt(0) != 'I')
                rimToArabicNum += 10;
            else if(num.charAt(i) == 'L')
                rimToArabicNum += 50;
            else if(num.charAt(i) == 'C')
                rimToArabicNum += 100;
            else
                if(rimToArabicNum == 0)
                    rimToArabicNum += Integer.parseInt(num);
        }
        return rimToArabicNum;
    }

    public static boolean isRims(String num1, String num2) {
        if((num1.contains("I") || num1.contains("V") || num1.contains("X")) &&
                (num2.contains("I") || num2.contains("V") || num2.contains("X")))
            return true;
        return false;
    }

    public static boolean isDifferentBase(String num1, String num2) {
        if((num1.contains("I") || num1.contains("V") || num1.contains("X")) &&
                !(num2.contains("I") || num2.contains("V") || num2.contains("X")) || !(num1.contains("I") ||
                num1.contains("V") || num1.contains("X")) && (num2.contains("I") || num2.contains("V") ||
                num2.contains("X")))
            return true;
        return false;
    }

    public static String arabicToRims(int num) {
        StringBuilder sb = new StringBuilder();
        int times;
        String[] romansNums = new String[] { "I", "IV", "V", "IX", "X", "XL", "L",
                "XC", "C"};
        int[] ints = new int[] { 1, 4, 5, 9, 10, 40, 50, 90, 100};
        for (int i = ints.length - 1; i >= 0; i--) {
            times = num / ints[i];
            num %= ints[i];
            while (times > 0) {
                sb.append(romansNums[i]);
                times--;
            }
        }
        return sb.toString();
    }

    public static String calc(String input) throws OnlyString, MoreThanTwoOperands, NegativeRimNum
    , DifferentBases, NonInteger, MoreThanTenOrZero{
        input = charsToUpperCase(input).replaceAll("\\s+", "");
        String num1 = "";
        String num2 = "";
        String operationType = "";
        boolean num1OrNum2 = true;
        for (int i = 0; i < input.length(); i++) {
            String charToStr = "";
            charToStr += input.charAt(i);
            if (!operationTypes.contains(charToStr) && num1OrNum2)
                num1 += charToStr;
            else if (operationTypes.contains(charToStr)) {
                operationType += charToStr;
                num1OrNum2 = false;
            } else if (!operationTypes.contains(charToStr) && !num1OrNum2)
                num2 += charToStr;
        }
        if (num1 == "" || num2 == "" || operationType == "")
            throw new OnlyString();
        else if (operationType.length() > 1)
            throw new MoreThanTwoOperands();
        else if (num1.contains(".") || num2.contains("."))
            throw new NonInteger();
        else if (isRims(num1, num2)) {
            int rimToArabicNum1 = calcRimsToArabic(num1);
            int rimToArabicNum2 = calcRimsToArabic(num2);
            if (rimToArabicNum1 > 10 || rimToArabicNum2 > 10)
                throw new MoreThanTenOrZero();
        }
        else if(isDifferentBase(num1, num2))
            throw new DifferentBases();
        if((calcRimsToArabic(num1) < 1 || calcRimsToArabic(num1) > 10) || (calcRimsToArabic(num2) < 1 ||
                calcRimsToArabic(num2) > 10))
            throw new MoreThanTenOrZero();
        if(operationType.contains("+")) {
            if(isRims(num1, num2)) {
                return arabicToRims(calcRimsToArabic(num1) + calcRimsToArabic(num2));
            }
            else {
                return Integer.toString((Integer.parseInt(num1) + Integer.parseInt(num2)));
            }
        }
        else if(operationType.contains("-")) {
            if(isRims(num1, num2)) {
                if((calcRimsToArabic(num1) - calcRimsToArabic(num2)) < 0)
                    throw new NegativeRimNum();
                else
                    return arabicToRims(calcRimsToArabic(num1) - calcRimsToArabic(num2));
            }
            else
                return Integer.toString((Integer.parseInt(num1) - Integer.parseInt(num2)));

        }
        else if(operationType.contains("*")) {
            if(isRims(num1, num2)) {
                return arabicToRims(calcRimsToArabic(num1) * calcRimsToArabic(num2));
            }
            else
                return Integer.toString((Integer.parseInt(num1) * Integer.parseInt(num2)));
        }
        else if(operationType.contains("/")) {
            if(isRims(num1, num2)) {
                return arabicToRims(calcRimsToArabic(num1) / calcRimsToArabic(num2));
            }
            else
                return Integer.toString((Integer.parseInt(num1) / Integer.parseInt(num2)));
        }
        return "";
    }

    public static void main(String[] args) throws OnlyString, MoreThanTwoOperands, NegativeRimNum,
            DifferentBases, NonInteger, MoreThanTenOrZero {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите выражение для счета:");
        String expressionToCalc = scanner.nextLine();
        System.out.println(calc(expressionToCalc));
        scanner.close();
    }
}