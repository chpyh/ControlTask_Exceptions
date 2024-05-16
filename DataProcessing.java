package ControlTask_Exceptions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DataProcessing {
    public static void main(String[] args) throws IOException {
        System.out.println(
                "Введите через пробел свои данные:\n Фамилию, имя, Отчество,\n дату рождения в формате: дд.мм.гггг,\n номер телефона в формате: 81234567890,\n пол обозначьте латиницей: f - женский, m - мужской.");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();
        String[] data = input.split(" ");

        if (exceptionCode(data) == -1) {
            throw new RuntimeException("Недостаточно данных: введите данные полностью.");
        } else {
            writeCheckedData(data);
        }
    }

    // + проверяем, достаточно ли данных ввел пользователь
    public static int exceptionCode(String[] data) {
        if (data.length != 6) {
            return -1;
        } else {
            return 0;
        }
    }

    // + проверяем, состоит ли слово полностью из букв
    public static boolean isAllLetter(String element) {
        char[] charArr = element.toCharArray();
        for (int i = 0; i < charArr.length; i++) {
            if (!Character.isLetter(charArr[i])) {
                return false;
            }
        }
        return true;
    }

    // метод для проверки, может елемент быть номером телефона
    public static boolean isPhoneNumber(String element) {
        if (element.length() < 11) {
            return false;
        }
        for (int i = 0; i < element.length(); i++) {
            if (!Character.isDigit(element.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // метод для нахождения проверки элемента в массиве - может ли он быть датой
    public static boolean isDate(String element) {
        char[] newData = element.toCharArray();
        if (element.length() < 10) {
            return false;
        }
        for (int i = 0; i < newData.length; i++) {
            if (i != 2 && i != 5) {
                if (!Character.isDigit(newData[i])) {
                    return false;
                }
            } else if (newData[i] != '.') {
                return false;
            }
        }
        return true;
    }

    // метод для создания нового массива срок с необходимыми данными в правильном
    // порядке
    public static String[] getData(String[] data) {
        String[] newData = new String[6];
        int i = 0;
        for (String element : data) {
            if (isAllLetter(element)) {
                if (element.length() > 1 && i <= 3) {
                    newData[i] = element;
                    i++;
                } else {
                    newData[5] = element;
                }
            }
            if (isPhoneNumber(element)) {
                newData[4] = element;
            }
            if (isDate(element)) {
                newData[3] = element;
            }
        }
        return newData;
    }

    // метод для парсинга и вывода данных в файл
    public static void writeCheckedData(String[] data) throws IOException {
        String[] newData = getData(data);

        String filename = newData[0] + ".txt";
        BufferedWriter writer = null;
        LocalDate birthdate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            birthdate = LocalDate.parse(newData[3], formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Вы ввели неверный формат даты рождения");
            return;
        }
        Long phoneNumber;

        try {
            phoneNumber = Long.parseLong(newData[4]);
        } catch (NumberFormatException e) {
            System.out.println("Вы ввели неверный формат номера телефона");
            return;
        }
        char gen = newData[5].charAt(0);
        String gender = null;
        if (gen == 'm' || gen == 'f') {
            gender = newData[5];
        } else {
            System.out.println("Вы указали неверный формат пола, ставьте 'f' -если вы мужчина, и 'm', если вы женщина");
            return;
        }
        try {
            writer = new BufferedWriter(new FileWriter(filename, true));
            writer.write(newData[0] + " " + newData[1] + " " + newData[2] + " " + birthdate + " " + phoneNumber + " "
                    + gender);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Ошибка записи");
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
