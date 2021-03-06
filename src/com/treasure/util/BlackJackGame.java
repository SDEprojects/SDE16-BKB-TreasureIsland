package com.treasure.util;

import com.treasure.Attributes;
import com.treasure.components.SubGames;

public class BlackJackGame {
    public static int player_random1 = 100;
    public static int player_random2 = 100;
    public static int dealer_random1 = 100;
    public static int dealer_random2 = 100;
    public static int player_total;
    public static int dealer_total;
    // Used to denote that we are in game right now, user may choose to continue playing at end
    private static boolean GAME_LOOP_IN_PROGRESS = false;

    private static boolean PLAYER_WINS = true;

    public static void playBlackJack() {
        if (Attributes.player.getsGold() >= 1) {
            Attributes.player.takesGold(1);
            GAME_LOOP_IN_PROGRESS = true;
            Attributes.currentSubGame = SubGames.BLACKJACK;
            initGame();
            mainLoop();
        } else {
            Methods.setMessage("You don't have enough Gold to play");
            Methods.setMessage2(" ");
            Methods.setMessage3(" ");
        }
    }

    private static void initGame() {
        while (player_random1 >= 12 || player_random2 >= 12 || player_random1 < 3 || player_random2 < 3) {
            player_random1 = (int) (Math.random() * 100);
            player_random2 = (int) (Math.random() * 100);
        }
        player_total = player_random1 + player_random2;

        // Dealer cards
        while (dealer_random1 >= 12 || dealer_random2 >= 12 || dealer_random1 < 3 || dealer_random2 < 3) {
            dealer_random1 = (int) (Math.random() * 100);
            dealer_random2 = (int) (Math.random() * 100);
        }
    }

    private static void mainLoop() {
        int dealer_total = dealer_random1 + dealer_random2;
        boolean hidden = Math.random() < 0.5; // to decide whether to hide one card or not
        if (player_total == 21) {
            Methods.setMessage("Blackjack!  Player Wins!");
            return;
        } else {
            System.out.println();
//            System.out.println("hand1");
            Methods.setMessage("Player      " + "["+ player_random1 + "]" + "  [" + player_random2 + "]    Total  " + player_total);
            if (hidden) {
                Methods.setMessage2("Dealer      " + "["+dealer_random1 + "]  [  ] ");
            } else {
                Methods.setMessage2("Dealer       " + "["+dealer_random1 + "]" + "  ["+dealer_random2 + "]    Total  " + dealer_total);
                if (dealer_total == 21) {
                    Methods.setMessage3("Blackjack!  Dealer Wins!");
                    return;
                }
            }
            Methods.setMessage3("Would you like to Hit [Y] or Stay [N]");
        }
    }

    public static void decisionRouting(boolean tf) {
        if(Attributes.currentSubGame != SubGames.BLACKJACK) {
            // we are not currently in a blackjack session so ignore any commands routed here
            return;
        }
        if(GAME_LOOP_IN_PROGRESS) {
            hitDecision(tf);
        } else {
            if(tf){
                playBlackJack();
            } else {
                Attributes.currentSubGame = SubGames.NONE;
                Methods.setMessage("Okay thanks for playing");
                Methods.setMessage2("Come back anytime for more");
                Methods.setMessage3(" ");
                Methods.setMessage4(" ");
//                Methods.setMessage5(" ");
//                Methods.setMessage6(" ");
            }

        }
    }

    private static void hitDecision(boolean tf) {
        if (tf) {
            System.out.println("player hit next hand");
            int player_random3 = 100;
            while (player_random3 >= 12 || player_random3 < 3) {
                player_random3 = (int) (Math.random() * 100);
            }
            player_total = player_total + player_random3;

            Methods.setMessage("Player      " + "["+ player_random1 + "]" + "  [" + player_random2 + "]  ["+ player_random3+"]    Total   " + player_total);
            if (player_total > 21) {
                endGame(!PLAYER_WINS);
                return;
            } else if (player_total == 21) {
                endGame(PLAYER_WINS);
                return;
            }
            Methods.setMessage3("Would you like to Hit [Y] or Stay [N]");

        } else {
            System.out.println("Player stayed next hand");
            int dealer_random3 = 100;
            while (dealer_random3 >= 12 || dealer_random3 < 3) {
                dealer_random3 = (int) (Math.random() * 100);
            }
            Methods.setMessage("Dealer       " + "["+dealer_random1 + "]  [" + dealer_random2 + "]     Total   " + dealer_total);

            if (dealer_total > 16) {
                Methods.setMessage2("Dealer stays.");
            } else {
                while (dealer_total <= 16) {
                    dealer_total = dealer_total + dealer_random3;
                    Methods.setMessage2("Dealer hits... He draws a  " + dealer_random3 + ". His total is  " + dealer_total);
                }
            }

            Methods.setMessage3("Dealer's total is  " + dealer_total + ". Your total is  " + player_total);
            if ((player_total > dealer_total && player_total < 21) || dealer_total > 21) {
                endGame(PLAYER_WINS);
                return;
            } else if ((dealer_total < 21 && player_total < dealer_total) || player_total > 21) {
                endGame(!PLAYER_WINS);
                return;
            }
        }
    }

    public static void endGame(boolean playerWins) {
        Methods.setMessage2(playerWins ? "YOU WIN!" : "Busted!  Dealer wins!");
        Methods.setMessage3("Would you like to play again [Y] or [N]");
        GAME_LOOP_IN_PROGRESS = false;
        if(playerWins) {
            Attributes.player.addsGold(2);
        }
    }
}
