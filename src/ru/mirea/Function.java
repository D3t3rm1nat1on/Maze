package ru.mirea;

import java.util.ArrayList;
import java.util.List;

public class Function {

    // Функция проверки что строку можно представить в виде числа Integer
    public static boolean AbleParseInt(String str)
    {
        try {
            int i = Integer.parseInt(str);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public static void Clear(List<Room> rooms)
    {
        rooms.clear();
    }


    // Функция проверки что из лабиринта можно выйти
    public static boolean CorrectMaze(List<Room> rooms, int idx_exit, int idx_enter, int[] checked_paths) {
        checked_paths[idx_enter] = 1;
        if (idx_enter == idx_exit)
            return true;
        for (int idx_next_room : rooms.get(idx_enter).paths) {
            if (checked_paths[idx_next_room - 1] == 1)
                continue;
            if (CorrectMaze(rooms, idx_exit, idx_next_room - 1, checked_paths))
                return true;
        }
        return false;
    }

}
