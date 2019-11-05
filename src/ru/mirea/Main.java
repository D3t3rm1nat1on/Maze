package ru.mirea;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Количество комнат графа
        int n = 20;

        // Массив для представления графа
        int a[][] = new int[n][n];

        // Создание списка предметов, которые можно найти (а можно и не найти) в лабиирнте
        List<Item> avaliable_items = new ArrayList<Item>();
        avaliable_items.add(new Item("Тортик"));
        avaliable_items.add(new Item("Пузо"));
        avaliable_items.add(new Item("Вилка"));
        avaliable_items.add(new Item("Дверь с надписью \"выход\""));
        avaliable_items.add(new Item("Палка"));

        // Список комнат лабиринта (вершин графа, только со списком соседних конмат и предметами)
        List<Room> rooms = new ArrayList<Room>();
        // Массив комнат, в которых мы уже побывали (нужен для проверки графа на корректность)
        int[] checked_rooms = new int[n];
        // Номера вершин графа со входом и выходом
        int idx_enter;
        int idx_exit;

        // Генерируем лабиринт до тех пор, пока из него нельзя выйти
        do {
            // Генерация путей (генерация графа)
            for (int i = 0; i < n; i++) {
                for (int j = i; j < n; j++) {
                    if (i != j) {
                        if ((int) (Math.random() * 100) % 5 == 1)
                            a[i][j] = 1;
                        else
                            a[i][j] = 0;
                        a[j][i] = a[i][j];
                    } else {
                        a[i][j] = 0;
                    }
                }
            }

            System.out.println("Генерируем...");

            // Задаем номера комнат с входом и выходом
            idx_enter = (int)(Math.random()*100 % n);
            idx_exit = idx_enter;
            while (idx_enter == idx_exit)
                idx_exit = (int)(Math.random()*100 % n);
            rooms.clear();
            // Создание комнат с доступными путями
            for (int i = 0; i < n; i++) {
                Room room = new Room();

                // в случайном порядке заполняем комнаты предметами
                if (!avaliable_items.isEmpty() && (int)(Math.random()*100) % 5 == 1) {
                    int idx_item = (int)(Math.random()*100) % avaliable_items.size();
                    Item item = avaliable_items.get(idx_item);
                    avaliable_items.remove(idx_item);
                    room = new Room(item);
                }

                // Добавляем комнатам возможные пути, которые есть в графе
                for (int j = 0; j < n; j++) {
                    if (a[i][j] == 1) {
                        room.paths.add(j + 1);
                    }
                }

                // Комнатам с нужным номерам присваеваем вход и выход
                if (i == idx_enter)
                    room = new Enter(room.paths);
                if (i == idx_exit)
                    room = new Exit(room.paths);

                // Добавляем готовую комнату в массив
                rooms.add(room);
            }

        } while(!Function.CorrectMaze(rooms, idx_exit, idx_enter, checked_rooms));

        // Вывод матрицы доступных путей
        System.out.println("Граф лабиринта:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j)
                    System.out.print("X ");
                else
                    System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();

        // Вывод комнат и их путей
        int idx = 1;
        for (Room room : rooms) {
            if (!room.paths.isEmpty()) {
                System.out.format("room %2d   paths: ", idx);
                for (int path : room.paths) {
                    System.out.print(path + " ");
                }
                System.out.println();
            }
            idx++;
        }

        // Условия выхода из лабиринта
        boolean exit = false;
        // Начальной комнатой становится вход
        Room in_room = rooms.get(idx_enter);
        Scanner in = new Scanner(System.in);
        // Рюкзак игрока
        List<Item> founded_items = new ArrayList<Item>();

        // Симуляция брождения по лабиринту
        while (!exit) {
            System.out.println("\nВы находитесь в комнате " + (rooms.indexOf(in_room) + 1));

            // Если игрок нашел в комнате предмет
            if(in_room.getItem() != null)
            {
                System.out.println("Вы нашли " + in_room.getItem().name);
                founded_items.add(in_room.getItem());
                in_room.takeItem();
            }

            // Если игрок пришел ко входу
            if (in_room.getClass() == Enter.class)
                System.out.println("Это вход");

            // Если игрок приел к выходу
            if (in_room.getClass() == Exit.class)
                System.out.println("Это выход");

            // Узнаем соседние комнаты
            System.out.print("Соседние комнаты:");
            for (int path : in_room.paths) {
                System.out.format(" %d", path);
            }
            System.out.println();

            // Получаем от игрока команду
            String comand = in.next();
            if (Function.AbleParseInt(comand) && in_room.paths.contains(Integer.parseInt(comand))) {
                in_room = rooms.get(Integer.parseInt(comand) - 1);
            } else if (in_room.getClass() == Exit.class && comand.equals("exit")) {
                exit = true;
            }
        }

        System.out.println("Вы успешно покинули лабиринт");
        if (founded_items.isEmpty())
            System.out.println("Вы ничего не нашли");
        else
            System.out.println("Список предметов которые вы нашли: ");
        for (Item item: founded_items
             ) {
            System.out.println(item.name);
        }
    }
}
