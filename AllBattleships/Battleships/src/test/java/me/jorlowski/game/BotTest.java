package me.jorlowski.game;

import me.jorlowski.State;
import org.junit.Test;
public class BotTest {
    @Test
    public void testBoard() {
        for (int k=0; k<10; k++){
            int sum = 0;
            Bot b = (Bot) Player.getAI(State.LEVEL_1);
            assert b != null;
            byte[][] board = b.getFinalBoard();
            for(int i=0; i<10; i++) {
                for(int j=0; j<10; j++) {
                    System.out.print(board[i][j] + " ");
                    if(board[i][j] == 1) {
                        sum++;
                    }
                }
                System.out.println();
            }
            assert sum == 20;
            System.out.println();
        }
    }
}
