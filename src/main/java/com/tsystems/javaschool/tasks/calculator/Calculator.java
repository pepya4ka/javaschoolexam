package com.tsystems.javaschool.tasks.calculator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    private final List<String> OPERATIONS = new ArrayList<>();
    private final List<Integer> PRIORITY = new ArrayList<>();

    public String evaluate(String statement) {
        OPERATIONS.add("+");
        OPERATIONS.add("-");
        OPERATIONS.add("*");
        OPERATIONS.add("/");

        PRIORITY.add(1);
        PRIORITY.add(1);
        PRIORITY.add(2);
        PRIORITY.add(2);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(statement);

        List<String> list = new ArrayList<>();
        ArrayDeque<String> numbersDeque = new ArrayDeque<>();
        ArrayDeque<String> operationsDeque = new ArrayDeque<>();

        appendToList(list, stringBuilder);
        if (roundingList(list)) {
            fillDeque(numbersDeque, operationsDeque, list);
        }
        if (numbersDeque.peekFirst() != null) {
            double result = Double.parseDouble(numbersDeque.peekFirst());
            if (result == (int) result)
                return String.valueOf((int) result);
            else
                return String.valueOf(roundingNumber(result));
        } else {
            return numbersDeque.peekFirst();
        }
    }


    public void appendToList(List<String> list, StringBuilder s) {
        StringBuilder temp = new StringBuilder();
        int i = 0;

        int tempCounter = 0;
        while (i < s.length()) {
            temp.append(s.charAt(i));
            if (s.charAt(i) == '(' || s.charAt(i) == ')') {
                list.add(temp.toString());
                temp.delete(0, temp.length());
                i++;
                continue;
            }
            if (s.charAt(i) == '+' || s.charAt(i) == '-'
                    || s.charAt(i) == '*' || s.charAt(i) == '/') {
                list.add(temp.toString());
                temp.delete(0, temp.length());
                i++;
                continue;
            } else {
                if (i + 1 >= s.length()) {
                    list.add(temp.toString());
                    temp.delete(0, temp.length());
                    i++;
                    continue;
                } else {
//                    System.out.println(s.charAt(i) );
                    if (s.charAt(i + 1) != '.')
                        if (s.charAt(i + 1) != '1' && s.charAt(i + 1) != '2' &&
                                s.charAt(i + 1) != '3' && s.charAt(i + 1) != '4' &&
                                s.charAt(i + 1) != '5' && s.charAt(i + 1) != '6' &&
                                s.charAt(i + 1) != '7' && s.charAt(i + 1) != '8' &&
                                s.charAt(i + 1) != '9' && s.charAt(i + 1) != '0') {
                            list.add(temp.toString());
                            temp.delete(0, temp.length());
                        }
                    i++;
                }
            }
        }
    }

    public boolean roundingList(List<String> list) {
        double value;
        for (String s : list) {
            if (s.matches(".*\\d+.*") || isDoubleNumeric(s)) {
                try {
                    value = Double.parseDouble(s);
                } catch (NumberFormatException e) {
                    return false;
                }
                value = roundingNumber(value);
                //list.set(list.indexOf(s), String.valueOf(value));
            }
        }
        return true;
    }

    public double roundingNumber(double value) {
        double scale = Math.pow(10, 5);
        int lastNumber = (int) Math.floor(value * scale);
        lastNumber = lastNumber % 10;

        scale = Math.pow(10, 4);
        if (lastNumber > 5) {
            value = Math.ceil(value * scale) / scale;
        } else {
            value = Math.floor(value * scale) / scale;
        }
        return value;
    }

    public String fillDeque(ArrayDeque<String> numbersDeque, ArrayDeque<String> operationsDeque, List<String> list) {
        Object[] arrayList = list.toArray();
        for (int i = 0; i < arrayList.length; i++) {
            String s = (String) arrayList[i];
            if (isIntNumeric(s) || isDoubleNumeric(s)) {
                numbersDeque.addFirst(s);
                continue;
            }
            if (isOperation(s) && operationsDeque.peekFirst() == null) {
                operationsDeque.addFirst(s);
                continue;
            }
            if (s.equals(")") && (operationsDeque.peekFirst().equals("("))) {
                operationsDeque.pollFirst();
                continue;
            }
            if (s.equals("(")) {
                operationsDeque.addFirst(s);
                continue;
            }
            if (s.equals(")") && operationsDeque.peekFirst() != null && isOperation(operationsDeque.peekFirst())) {
                while (!operationsDeque.peekFirst().equals("(")) {
                    try {
                        doOperation(numbersDeque, operationsDeque);
                    } catch (NullPointerException e) {
                        return null;
                    }
                }
                operationsDeque.pollFirst();
                continue;
            }
            if (isOperation(s) && operationsDeque.peekFirst() != null) {
                if (isOperation(operationsDeque.peekFirst())) {
                    if (isOperationPriority(s, operationsDeque.peekFirst())) {
                        try {
                            doOperation(numbersDeque, operationsDeque);
                        } catch (NullPointerException e) {
                            return null;
                        }
                        i--;
                        continue;
                    }
                }
                operationsDeque.addFirst(s);
                continue;
            }
            return null;
        }
        while (operationsDeque.peekFirst() != null)
            try {
                doOperation(numbersDeque, operationsDeque);
            } catch (ArithmeticException | NullPointerException e) {
                return null;
            }
        return numbersDeque.peekFirst();
    }

    public boolean isOperation(String operationString) {
        if (operationString.equals("+") || operationString.equals("-")
                || operationString.equals("*") || operationString.equals("/"))
            return true;
        return false;
    }

    public boolean isIntNumeric(String s) {
        try {
            int d = Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isDoubleNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public boolean isOperationPriority(String x, String y) {
        if (y.equals(")") || y.equals("("))
            return false;
        int a = PRIORITY.get(OPERATIONS.indexOf(x));
        int b = PRIORITY.get(OPERATIONS.indexOf(y));

        if (a <= b) return true;
        else return false;
    }

    public void doOperation(ArrayDeque<String> numbersDeque, ArrayDeque<String> operationsDeque) {
        double a = Double.parseDouble(Objects.requireNonNull(numbersDeque.pollFirst()));
        double b = Double.parseDouble(Objects.requireNonNull(numbersDeque.pollFirst()));
        String s = operationsDeque.pollFirst();

        int index = OPERATIONS.indexOf(s);
        switch (index) {
            case 0:
                numbersDeque.addFirst(String.valueOf(b + a));
                break;
            case 1:
                numbersDeque.addFirst(String.valueOf(b - a));
                break;
            case 2:
                numbersDeque.addFirst(String.valueOf(b * a));
                break;
            case 3:
                if (a > 0 || a < 0)
                    numbersDeque.addFirst(String.valueOf(b / a));
                else
                    numbersDeque.addFirst(null);
                break;
        }
    }

}
