package model;
public class CheckersDataTest {
    private static int errors = 0;
    private static int total = 0;
   public static void finalStatistic() {
        if (errors == 0) {
            System.out.println("No errors /number of tests " + total);
        } else
            System.out.println("Number of errors " + errors + " number of tests " + total);
    }
    public void check(boolean litmus) {
        total++;
        if (!litmus) {
            errors++;
        }
    }
    public void testmove() {
        CheckersData board = new CheckersData();
        board.makeMove(new CheckersData.CheckersMove(5, 2, 4, 1));
        board.makeMove(new CheckersData.CheckersMove(4, 1, 3, 2));
        check(board.canMove(CheckersData.BLACK, 3, 2, 2, 1));
    }
    public void testmove1() {
        CheckersData board = new CheckersData();
        board.setUp(3, 3, 4);
        board.makeMove(new CheckersData.CheckersMove(3, 3, 4, 4));
        board.makeMove(new CheckersData.CheckersMove(4, 4, 3, 5));
        check(board.canMove(CheckersData.BLACK, 4, 5, 4, 6));
    }
    public void testjump() {
        CheckersData board = new CheckersData();
        board.setUp(3, 3, 3);
        check(board.canJump(CheckersData.RED, 2, 4, 3, 3, 4, 2));
    }
    public void testjump1() {
        CheckersData board = new CheckersData();
        board.setUp(3, 5, 3);
        check(board.canJump(CheckersData.RED, 2, 4, 3, 5, 4, 6));
    }
}