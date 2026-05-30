package client.util;

import common.models.*;

import java.util.Scanner;

public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in);

    public static Person readPerson() {
        try {
            String name;
            while (true) {
                System.out.print("Имя: ");
                name = scanner.nextLine().trim();
                if (!name.isEmpty()) {
                    break;
                }
                System.out.println("Имя не может быть пустым. Попробуйте снова.");
            }

            Float x = null;
            while (x == null) {
                System.out.print("X (> -690): ");
                String input = scanner.nextLine().trim();
                try {
                    x = Float.parseFloat(input);
                    if (x <= -690) {
                        System.out.println("X должно быть > -690. Попробуйте снова.");
                        x = null;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Неверный формат. Введите число.");
                }
            }

            Double y = null;
            while (y == null) {
                System.out.print("Y (≤ 28): ");
                String input = scanner.nextLine().trim();
                try {
                    y = Double.parseDouble(input);
                    if (y > 28) {
                        System.out.println("Y должно быть ≤ 28. Попробуйте снова.");
                        y = null;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Неверный формат. Введите число.");
                }
            }
            Coordinates coords = new Coordinates(x, y);

            Integer height = null;
            while (height == null) {
                System.out.print("Рост (> 0): ");
                String input = scanner.nextLine().trim();
                try {
                    height = Integer.parseInt(input);
                    if (height <= 0) {
                        System.out.println("Рост должен быть > 0. Попробуйте снова.");
                        height = null;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Неверный формат. Введите целое число.");
                }
            }

            String passport = null;
            while (true) {
                System.out.print("Паспорт (мин. 9 символов, Enter для пропуска): ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    passport = null;
                    break;
                }

                if (input.length() < 9) {
                    System.out.println("Длина паспорта должна быть >= 9. Попробуйте снова.");
                } else {
                    passport = input;
                    break;
                }
            }

            Color eyeColor = null;
            while (eyeColor == null) {
                System.out.print("Цвет глаз (BLACK/BLUE/BROWN, Enter для пропуска): ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    break;
                }

                try {
                    eyeColor = Color.valueOf(input.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверный цвет. Доступные: BLACK, BLUE, BROWN");
                }
            }

            Country nationality = null;
            while (nationality == null) {
                System.out.print("Национальность (ITALY/THAILAND/NORTH_KOREA): ");
                String input = scanner.nextLine().trim();

                try {
                    nationality = Country.valueOf(input.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверная национальность. Доступные: ITALY, THAILAND, NORTH_KOREA");
                }
            }

            Float locX = null;
            while (locX == null) {
                System.out.print("Локация X: ");
                String input = scanner.nextLine().trim();
                try {
                    locX = Float.parseFloat(input);
                } catch (NumberFormatException e) {
                    System.out.println("Неверный формат. Введите число.");
                }
            }

            Long locY = null;
            while (locY == null) {
                System.out.print("Локация Y: ");
                String input = scanner.nextLine().trim();
                try {
                    locY = Long.parseLong(input);
                } catch (NumberFormatException e) {
                    System.out.println("Неверный формат. Введите целое число.");
                }
            }

            String locName = null;
            while (locName == null) {
                System.out.print("Название места: ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Название не может быть пустым.");
                } else if (input.length() > 811) {
                    System.out.println("Длина названия должна быть ≤ 811.");
                } else {
                    locName = input;
                }
            }

            Location location = new Location(locX, locY, locName);

            return new Person(name, coords, height, passport, eyeColor, nationality, location);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}