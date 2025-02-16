import java.util.*;

public class Game_logic {
    int score = 100;
    int user_number;
    boolean game_end = true;
    Scanner scn = new Scanner(System.in);
    List<Integer> guessed = new ArrayList<>();

    public int randomnumber(int min, int max) // generate random number
    {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;

    }

    public int game_logic(int min, int max) // logic of the game
    {
        int rnumber = randomnumber(min, max);


        System.out.print("Guess the number between " + min + " and " + max + ": ");
        try {
            user_number = scn.nextInt();
            scn.nextLine();
            if (user_number < min) {
                System.out.println("Write only positive numbers!");
                return 0;

            }
            score = max - Math.abs((rnumber - user_number));
            if (score <= 0) {
                score = 0;
            }

            System.out.println("The number was: " + rnumber);
            System.out.println("You got: " + score + " points.");


        } catch (Exception e) { //error handling
            System.out.println("Write a number!");
            game_logic(min, max);
        }
        return score;
    }

    public int game_logic2(int min, int max) {
        List<Integer> guessed_numbers = new ArrayList<>();
        int guess = randomnumber(min, max);
        score = max;
        System.out.print("Write the number from 0 to " + max + " to guess: ");
        try {
            user_number = scn.nextInt();
            scn.nextLine();
            if (user_number < min) {
                System.out.println("Write only positive numbers!");
                return 0;
            }
            while (user_number != guess) {
                if (!guessed_numbers.contains(guess)) {
                    guessed_numbers.add(guess);
                    score -= 1;

                }
                guess = randomnumber(min, max);
            }
            System.out.println("Game got: " + score + " points.");


        } catch (Exception e) { //error handling
            System.out.println("Write a number!");
            game_logic2(min, max);
        }
        return score;
    }

    public void game_logic3(int min, int max) {
        //chose who start
        int coin = randomnumber(0, 1000);
        int rnumber = randomnumber(min, max);


        while (game_end) {
            if (coin % 2 == 0) {
                game_logic3_1(rnumber, min, max);
            } else {
                game_logic3_2(rnumber, min, max);
            }

        }
    }

    public void game_logic3_1(int i, int min, int max) {
        System.out.print("Guess the number: ");
        try {
            user_number = scn.nextInt();

            if (user_number < 0) {
                game_logic3_1(i, min, max);
            }
            if (i == user_number) {
                System.out.println("You won!");
                game_end = false;
            } else {
                guessed.add(user_number);
                game_logic3_2(i, min, max);
            }
        } catch (Exception e) { //error handling
            System.out.println("Write a number!");
            game_logic3_1(i, min, max);
        }

    }

    public void game_logic3_2(int i, int min, int max) {
        int guess = 0;
        while (guessed.contains(guess)) {
            guess = randomnumber(min, max);
        }
        if (i == guess) {
            System.out.println("Game won!");
            game_end = false;
        } else {
            guessed.add(guess);
            System.out.println("Game guessed: " + guess);
            game_logic3_1(i, min, max);
        }


    }

    public int game_logic_multi(int random, String name) {
        String string_test = "";
        System.out.print(name + " guess the number: ");
        try {
            string_test = scn.nextLine();

            user_number = Integer.parseInt(string_test);


        } catch (Exception e) { //error handling
            if (Objects.equals("x", string_test)) {
                return 2;
            }
            System.out.println("Write a number!");
            game_logic_multi(random, name);
        }

        if (user_number < 0) {
            game_logic_multi(random, name);
        }
        if (random == user_number) {
            System.out.println(name + " you won!");
            return 1;
        } else {
            System.out.println("Miss ");

        }
        return 0;

    }

}


