import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class Player extends Game_logic {
    String name;
    String score = "0";
    Path pathname = Paths.get("./highscore.txt");
    List<String> usernames = new ArrayList<>();
    String temp_score = "0";
    String choice;
    String difficulty;
    String exit_flag;
    String single_or_multiplayer;
    String wins = "0";
    int min = 0;
    int max;
    int line_index = -1;
    int players;


    private void game() throws IOException {
        Scanner scn = new Scanner(System.in);
        System.out.println("Write 1 for singleplayer or 2 for multiplayer: ");
        single_or_multiplayer = scn.nextLine();
        if (Objects.equals(single_or_multiplayer, "1")) {
            singleplayer();
        }
        if (Objects.equals(single_or_multiplayer, "2")) {
            multiplayer();
        } else {
            System.out.println("You can only chose 1, 2 or 3!");
            game();
        }
    }

    private void singleplayer() throws IOException {

        checkUsername(1);

        //set difficulty
        System.out.println("Chose difficulty: 1 for easy, 2 for medium and 3 for hard:  ");
        difficulty = scn.nextLine();
        setDifficulty(difficulty);
        //chose type of game
        System.out.println("Chose type of game: ");
        System.out.println("Press 1 if you want to guess the number. ");
        System.out.println("Press 2 for game to guess the number. ");
        System.out.println("Press 3 for you and game guess number in turns: ");

        choice = scn.nextLine();
        choseGame(choice);

        //end program
        endGame();
    }

    private void multiplayer() throws IOException {
        String number_of_players;
        boolean flag = true;

        System.out.println("Write number of players: ");
        number_of_players = scn.nextLine();
        try {
            players = Integer.parseInt(number_of_players);
            if (players <= 1) {
                System.out.println("Multiplayer is only possible for 2 or more players.");
                multiplayer();
            }
        } catch (Exception e) {
            System.out.println("Write only numbers ");
            multiplayer();
        }
        for (int i = 0; i < players; i += 1) {
            checkUsername(2);
        }
        int random = randomnumber(min, max);
        while (flag) {
            for (int i = 0; i < players; i += 1) {
                int a = game_logic_multi(random, usernames.get(i));
                if (a == 1) {
                    multiSaveScore(usernames.get(i));
                    flag = false;
                    userDifficulty();

                }
                if (a == 2) {
                    game();
                    flag = false;
                }
            }

            System.out.println("If you want to start another game write x otherwise write number you want to guess.");


        }
    }

    private void checkUsername(int i) throws IOException {
        List<String> lines = Files.readAllLines(pathname, StandardCharsets.UTF_8);
        Scanner scn = new Scanner(System.in);

        System.out.print("Write your username: ");
        this.name = scn.nextLine();

        for (int j = 4; j < lines.size(); j += 4) {
            if (lines.get(j).equals(this.name)) {
                line_index = j;
                break;
            }

        }
        if (i == 1) { //singleplayer
            if (line_index != -1) { //check if player highscore is saved
                this.score = lines.get(line_index + 1);

                System.out.println("Welcome back " + this.name);
                System.out.println("Your saved highscore is: " + this.score);
            }
        } else { //multiplayer
            if (line_index != -1) {
                this.wins = lines.get(line_index + 3);
                System.out.println("Welcome back " + this.name);
                System.out.println("Your wins in multiplayer: " + this.wins);
            } else {
                lines.add(this.name);
                lines.add(this.temp_score);
                lines.add(this.difficulty);
                lines.add(this.wins);
                Files.write(pathname, lines, StandardCharsets.UTF_8);
            }
            usernames.add(this.name);

        }
    }

    private void multiSaveScore(String name) throws IOException {
        List<String> lines = Files.readAllLines(pathname, StandardCharsets.UTF_8);
        for (int j = 4; j < lines.size(); j += 4) {
            if (lines.get(j).equals(name)) {
                line_index = j;
                break;
            }
        }
        int tepm_win = Integer.parseInt(lines.get(line_index + 3)) + 1;
        lines.set(line_index + 3, Integer.toString(tepm_win));
        lines.set(line_index + 2, difficulty);
        Files.write(pathname, lines, StandardCharsets.UTF_8);
    }

    private void setDifficulty(String str) throws IOException {
        if (Objects.equals(str, "1")) {
            max = 100;
            difficulty = "Easy";
        } else if (Objects.equals(str, "2")) {
            max = 10000;
            difficulty = "Medium";
        } else if (Objects.equals(str, "3")) {
            max = 1000000;
            difficulty = "Hard";

        } else {
            System.out.println("You can only chose 1, 2 or 3!");
            game();

        }
    }

    private void userDifficulty() throws IOException {
        System.out.println("Write 1 to make a custom difficulty or 2 to play without changing anything. ");
        String answer = scn.nextLine();
        if (Objects.equals(answer, "1")) {
            try {
                System.out.println("Write a minimum number: ");
                min = scn.nextInt();
                scn.nextLine();
                System.out.println("Write a maximum number: ");
                max = scn.nextInt();
                scn.nextLine();
                difficulty = "Min: " + min + " Max: " + max;
                game();

            } catch (Exception e) {
                System.out.println("Write a number!");
                userDifficulty();
            }
        } else {
            game();
        }

    }

    private void choseGame(String str) throws IOException { //chose type of game
        int game_score;
        if (Objects.equals(str, "1")) {
            game_score = (game_logic(min, max));
            if (Integer.parseInt(temp_score) < game_score) {
                this.temp_score = Integer.toString(game_score);
            }
            endGame();
        } else if (Objects.equals(str, "2")) {
            game_score = (game_logic2(min, max));
            if (Integer.parseInt(temp_score) < game_score) {
                this.temp_score = Integer.toString(game_score);
            }
            this.line_index = 0;
            endGame();
        } else if (Objects.equals(str, "3")) {
            game_logic3(min, max);
            endGame();
        } else {
            System.out.println("You can only chose 1, 2 or 3!");
            game();

        }
    }

    private void endGame() throws IOException {  //end program

        System.out.println("Write any button to continue playing or x to exit: ");
        exit_flag = scn.nextLine();

        if (Objects.equals("x", exit_flag)) {
            saveScore();
            System.exit(0);
        } else {
            System.out.println("Chose type of game: ");
            System.out.println("Press 1 if you want to guess the number. ");
            System.out.println("Press 2 for game to guess the number. ");
            System.out.println("Press 3 for you and game guess number in turns: ");

            choice = scn.nextLine();
            choseGame(choice);
        }
    }

    private void saveScore() throws IOException { //save new highscore
        List<String> lines = Files.readAllLines(pathname, StandardCharsets.UTF_8);

        if (Integer.parseInt(this.temp_score) > Integer.parseInt(this.score)) {
            if (line_index != -1) {
                lines.set(line_index + 1, this.temp_score);
                lines.set(line_index + 2, this.difficulty);
                lines.set(line_index + 3, this.wins);
            } else {
                lines.add(this.name);
                lines.add(this.temp_score);
                lines.add(this.difficulty);
                lines.add(this.wins);
            }
        }
        Files.write(pathname, lines, StandardCharsets.UTF_8);
    }

    //create highscore file
    public void create_file() throws IOException {

        File file = new File(this.pathname.toString());
        if (!file.exists()) {
            try {
                // Create the empty file
                Files.createFile(this.pathname);
            } catch (FileAlreadyExistsException x) {
                System.err.format("file named %s" + " already exists%n", this.pathname);
            } catch (IOException x) {

                System.err.format("createFile error: %s%n", x);
            }
        }

        game();
    }

}
