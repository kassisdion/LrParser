package com.kassisdion;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/*
** Implement and test the LR parsing algorithm given in Section 4.5.3 in the textbook.
 */

public class LrParser {

    /**********
     * * Constructor
     **********/
    /*Simple constructor*/
    public LrParser() {

    }

    /**********
     * * Static field
     **********/
    /*Enumeration used for describing the token*/
    /*This enumeration will be use as table index so we associate every value with an int*/
    public enum TokenType {
        /*List every token*/
        ID(0),
        PLUS(1),
        TIMES(2),
        LEFT_PARENT(3),
        RIGHT_PARENT(4),
        EOF(5);

        int value;/*The value associate the the token*/

        /*Constructor*/
        TokenType(int value) {
            this.value = value;
        }
    }

    /*Enumeration used for describing the rules*/
    /*This enumeration will be use as table index so we associate every value with an int*/
    public enum RulesNames {
        /*List every rule*/
        E(0),
        T(1),
        F(2);

        int value;/*The value associate the the rule*/

        /*Constructor*/
        RulesNames(int value) {
            this.value = value;
        }
    }

    /*Enumeration used for describing the action*/
    /*This enumeration will be use as table index so we associate every value with an int*/
    public enum ActionType {
        /*List every possible action*/
        SHIFT(0),
        ERROR(1),
        REDUCE(2),
        ACCEPT(3);

        int value;/*The value associate the the action*/

        /*Constructor*/
        ActionType(int value) {
            this.value = value;
        }
    }

    /*Class used for simplify the creation of the action table*/
    /*It's allow us to have an action associate with a value (like REDUCE 10)*/
    public static class Action {
        final ActionType type; /*The action type*/
        final int value; /*The action value*/

        /*Constructor*/
        public Action(final ActionType action_type, int value) {
            this.type = action_type;
            this.value = value;
        }

        /*Utils function for making the action table more readable*/
        public static Action newAction(final ActionType action_type, int value) {
            return new Action(action_type, value);
        }
    }

    /**********
     * * TABLE
     **********/
    /*Action table*/
    private final Action[][] action_table = new Action[][]{
            /*                  ID                                    +                                    *                             (                                )                                     $                */
            /* 0 */ {Action.newAction(ActionType.SHIFT, 5), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.SHIFT, 4), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.ERROR, 0)},
            /* 1 */ {Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.SHIFT, 6), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.ACCEPT, 0)},
            /* 2 */ {Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.REDUCE, 2), Action.newAction(ActionType.SHIFT, 7), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.REDUCE, 2), Action.newAction(ActionType.REDUCE, 2)},
            /* 3 */ {Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.REDUCE, 4), Action.newAction(ActionType.REDUCE, 4), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.REDUCE, 4), Action.newAction(ActionType.REDUCE, 4)},
            /* 4 */ {Action.newAction(ActionType.SHIFT, 5), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.SHIFT, 4), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.ERROR, 0)},
            /* 5 */ {Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.REDUCE, 6), Action.newAction(ActionType.REDUCE, 6), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.REDUCE, 6), Action.newAction(ActionType.REDUCE, 6)},
            /* 6 */ {Action.newAction(ActionType.SHIFT, 5), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.SHIFT, 4), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.ERROR, 0)},
            /* 7 */ {Action.newAction(ActionType.SHIFT, 5), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.SHIFT, 4), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.ERROR, 0)},
            /* 8 */ {Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.SHIFT, 6), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.SHIFT, 11), Action.newAction(ActionType.ERROR, 0)},
            /* 9 */ {Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.REDUCE, 1), Action.newAction(ActionType.SHIFT, 7), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.REDUCE, 1), Action.newAction(ActionType.REDUCE, 1)},
            /*10 */ {Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.REDUCE, 3), Action.newAction(ActionType.REDUCE, 3), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.REDUCE, 3), Action.newAction(ActionType.REDUCE, 3)},
            /*11 */ {Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.REDUCE, 5), Action.newAction(ActionType.REDUCE, 5), Action.newAction(ActionType.ERROR, 0), Action.newAction(ActionType.REDUCE, 5), Action.newAction(ActionType.REDUCE, 5)}
    };

    /*Goto table*/
    private final Integer[][] goto_table = new Integer[][]{
            /*        E     T     F            */
            /* 0 */ {1, 2, 3},
            /* 1 */ {null, null, null},
            /* 2 */ {null, null, null},
            /* 3 */ {null, null, null},
            /* 4 */ {8, 2, 3},
            /* 5 */ {null, null, null},
            /* 6 */ {null, 9, 3},
            /* 7 */ {null, null, 10},
            /* 8 */ {null, null, null},
            /* 9 */ {null, null, null},
            /*10 */ {null, null, null},
            /*11 */ {null, null, null}
    };

    /*Rules table*/
    private final Integer[][] rules_table = new Integer[][]{
                    /*/Name            size*/
            /* 1 */ {RulesNames.E.value, 3},
            /* 2 */ {RulesNames.E.value, 1},
            /* 3 */ {RulesNames.T.value, 3},
            /* 4 */ {RulesNames.T.value, 1},
            /* 5 */ {RulesNames.F.value, 3},
            /* 6 */ {RulesNames.F.value, 1}
    };

    /*
    **public method
     */
    public void parse(final List<TokenType> inputList) {
        Stack<Integer> stack = new Stack<>();

        /*The position of the current input token under the inputList*/
        int current_token_position = 0;

        /*init the stack by pushing a 0*/
        stack.push(0);

        boolean run = true;
        while (run) {

            /*Display some log*/
            displayCurrentState(stack, inputList, current_token_position);

            /*Get the current token*/
            final TokenType current_token = inputList.get(current_token_position);

            /*Get the current state*/
            final int current_state = stack.lastElement();

            /*Check the action we have to do*/
            final Action action = action_table[current_state][current_token.value];
            switch (action.type) {
                case SHIFT:
                    /*We have to consume 1 input */
                    current_token_position += 1;

                    /*We have to the push the shift value to the stack */
                    final int shift_value = action.value;
                    stack.push(shift_value);

                    System.out.print(" -> Shift " + shift_value);
                    break;
                case ACCEPT:
                    /*We have successfully done the parsing so we stop the parsing loop*/
                    run = false;

                    System.out.print(" -> Accept");
                    break;
                case ERROR:
                    /*We have a parsing error so we stop the parsing loop*/
                    run = false;

                    System.out.print(" -> Error");
                    break;
                case REDUCE:
                    /*Get the rule size and rule name (we remove cause index start at 1)*/
                    final int reduce_value = action.value;
                    final int rule_size = rules_table[reduce_value -1][1];
                    final int rule_name = rules_table[reduce_value - 1][0];

                    /*We pop the stack according to the rule size*/
                    for (int i = 0; i < rule_size; i++) {
                        stack.pop();
                    }

                    /*We check the current state and we find the new state under the goto table*/
                    final int tmp_state = stack.lastElement();
                    final int new_state = goto_table[tmp_state][rule_name];

                    /*We push the new state onto the stack*/
                    stack.push(new_state);

                    System.out.print(" -> Reduce " + (reduce_value));
                    break;
            }
            System.out.println();
        }

    }

    /**********
     * * Utils func for displaying some log
     **********/

    private static void displayCurrentState(final Stack stack, final List<TokenType> inputList, final int current_token_position) {
        printStack(stack);
        System.out.print(" ");
        printListFromPosition(inputList, current_token_position);
    }

    /*Print the content of the stack pass as parameter*/
    private static void printStack(final Stack stack) {
        System.out.print("[");
        for (Object aStack : stack) {
            final String separator = (aStack == stack.lastElement() ? "" : ", ");
            System.out.print(aStack + separator);
        }
        System.out.print("]");
    }

    /*Display the content of the list by starting from startPosition*/
    private static void printListFromPosition(final List<TokenType> input, final int startPosition) {
        System.out.print("[");
        for (int i = startPosition; i < input.size(); i++) {
            final String separator = ((i == input.size() - 1) ? "" : ", ");
            System.out.print(input.get(i) + separator);
        }
        System.out.print("]");
    }

    /***********
     * * Main function
     ***********/
    public static void main(String[] args) {
        /*Initialze a token list which represent: id + id * id $*/
        final List<LrParser.TokenType> list = Arrays.asList(
                LrParser.TokenType.ID,
                LrParser.TokenType.PLUS,
                LrParser.TokenType.ID,
                LrParser.TokenType.TIMES,
                LrParser.TokenType.ID,
                LrParser.TokenType.EOF);

        /*start the parser with the list */
        new LrParser().parse(list);
    }
}