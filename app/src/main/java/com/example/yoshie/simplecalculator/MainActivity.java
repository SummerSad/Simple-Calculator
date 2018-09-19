package com.example.yoshie.simplecalculator;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View
        .OnLongClickListener {

    private static int ROW_PORTRAIT = 5;
    private static int COL_PORTRAIT = 4;
    private static int ROW_LANDSCAPE = 4;
    private static int COL_LANDSCAPE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add button to grid layout
        if (this.getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_PORTRAIT)
            addMathButtons(ROW_PORTRAIT, COL_PORTRAIT, true);
        else if (this.getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_LANDSCAPE)
            addMathButtons(ROW_LANDSCAPE, COL_LANDSCAPE, false);

        // turn off virtual keyboard
        EditText inputText = findViewById(R.id.idInput);
        inputText.setShowSoftInputOnFocus(false);
    }

    private static String msg_not_valid = "NOT VALID";
    private static String msg_divide_0 = "DIVIDE 0 ERROR";
    private static String tag_error = "ERROR";

    private void addMathButtons(int row, int col, boolean isPortrait) {
        GridLayout mathGridLayout = findViewById(R.id.idMathButtons);
        mathGridLayout.removeAllViews();
        mathGridLayout.setRowCount(row);
        mathGridLayout.setColumnCount(col);

        int SIZE_LABELS = 18;
        String[] strMathLabels = new String[SIZE_LABELS];

        if (isPortrait) {
            // - + * /
            // 7 8 9 DEL
            // 4 5 6
            // 1 2 3
            // . 0 = %
            int i = 0;
            strMathLabels[i++] = "-";
            strMathLabels[i++] = "+";
            strMathLabels[i++] = "*";
            strMathLabels[i++] = "/";
            strMathLabels[i++] = "7";
            strMathLabels[i++] = "8";
            strMathLabels[i++] = "9";
            strMathLabels[i++] = "DEL";
            strMathLabels[i++] = "4";
            strMathLabels[i++] = "5";
            strMathLabels[i++] = "6";
            strMathLabels[i++] = "1";
            strMathLabels[i++] = "2";
            strMathLabels[i++] = "3";
            strMathLabels[i++] = ".";
            strMathLabels[i++] = "0";
            strMathLabels[i++] = "=";
            strMathLabels[i] = "%";
        } else {
            // 7 8 9 DEL
            // 4 5 6 - +
            // 1 2 3 * /
            // . 0 = %
            int i = 0;
            strMathLabels[i++] = "7";
            strMathLabels[i++] = "8";
            strMathLabels[i++] = "9";
            strMathLabels[i++] = "DEL";
            strMathLabels[i++] = "4";
            strMathLabels[i++] = "5";
            strMathLabels[i++] = "6";
            strMathLabels[i++] = "-";
            strMathLabels[i++] = "+";
            strMathLabels[i++] = "1";
            strMathLabels[i++] = "2";
            strMathLabels[i++] = "3";
            strMathLabels[i++] = "*";
            strMathLabels[i++] = "/";
            strMathLabels[i++] = ".";
            strMathLabels[i++] = "0";
            strMathLabels[i++] = "=";
            strMathLabels[i] = "%";
        }

        for (String label : strMathLabels) {
            creSingleMathButton(label, isPortrait);
        }
    }

    @Override
    public void onClick(View v) {
        Button singleButton = (Button) v;

        EditText inputText = findViewById(R.id.idInput);
        TextView resultText = findViewById(R.id.idResult);

        if (singleButton.getText().toString().equals("DEL")) {
            // delete left cursor
            int pos = inputText.getSelectionStart();
            if (pos > 0) {
                inputText.getText().delete(pos - 1, pos);
            }
        } else if (singleButton.getText().toString().equals("=")) {
            // process user input then print result
            String strResult = getResult(inputText.getText().toString());
            resultText.setText(strResult);
        } else {
            // insert to left cursor
            // remove error msg
            if (resultText.getText().toString().equals(msg_not_valid) || resultText
                    .getText().toString().equals(msg_divide_0))
                resultText.setText("");
            int pos = inputText.getSelectionStart();
            inputText.getText().insert(pos, singleButton.getText());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Button singleButton = (Button) v;

        EditText inputText = findViewById(R.id.idInput);
        TextView resultText = findViewById(R.id.idResult);

        if (singleButton.getText().toString().equals("DEL")) {
            // long click DEL to clear all
            inputText.setText("");
            resultText.setText("");
            return true;
        }

        return false;
    }

    private void creSingleMathButton(String strMathButton, boolean isPortrait) {
        Button singleButton = new Button(this);
        singleButton.setText(strMathButton);
        singleButton.setTextSize(18);

        // set layout for button
        GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams();
        if (isPortrait) {
            gridParam.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1);
            gridParam.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1);
            if (singleButton.getText().toString().equals("DEL")) {
                gridParam.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 3);
                gridParam.setGravity(Gravity.FILL);
            }
        } else {
            gridParam.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1);
            gridParam.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1);
            if (singleButton.getText().toString().equals("DEL") || singleButton.getText()
                    .toString().equals("=")) {
                gridParam.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 2, 1);
                gridParam.setGravity(Gravity.FILL);
            }
        }

        // add view
        GridLayout mathGridLayout = findViewById(R.id.idMathButtons);
        mathGridLayout.addView(singleButton, gridParam);

        // set click
        singleButton.setOnClickListener(this);
        singleButton.setOnLongClickListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // check the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            addMathButtons(ROW_PORTRAIT, COL_PORTRAIT, true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            addMathButtons(ROW_LANDSCAPE, COL_LANDSCAPE, false);
        }
    }

    private String getResult(String s) {
        // https://stackoverflow.com/questions/13525024/how-to-split-a-mathematical-expression-on
        // -operators-as-delimiters-while-keeping
        String[] input = s.split("(?<=[-+*/])|(?=[-+*/])");
        for (int i = 0; i < input.length; ++i) {
            if (!isValidMath(input[i])) {
                Log.d(tag_error, input[i]);
                return msg_not_valid;
            }
            if (isPercent(input[i])) {
                // delete %
                StringBuilder strFromPercent = new StringBuilder(input[i]);
                strFromPercent.deleteCharAt(input[i].length() - 1);
                input[i] = strFromPercent.toString();
                // to double
                double doubleFromPercent = Double.valueOf(input[i]);
                doubleFromPercent /= 100;
                input[i] = String.valueOf(doubleFromPercent);
            }
        }

        // https://en.wikipedia.org/wiki/Shunting-yard_algorithm
        // https://www.chris-j.co.uk/parsing.php
        Queue<String> output_queue = shuntingYardAlgo(input);

        // https://en.wikipedia.org/wiki/Reverse_Polish_notation
        return (reversePolishNotation(output_queue));
    }

    private boolean notNumber(String s) {
        if (s.isEmpty())
            return true;
        for (char ch : s.toCharArray()) {
            if (!isDigit(ch) && ch != '.' && ch != '%')
                return true;
        }
        return false;
    }

    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private String reversePolishNotation(Queue<String> output_queue) {
        Stack<String> rpn_stack = new Stack<>();
        while (!output_queue.isEmpty()) {
            String token = output_queue.poll();
            if (isOperator(token)) {
                if (rpn_stack.isEmpty()) {
                    Log.d(tag_error, "missing operand");
                    return msg_not_valid;
                }
                String operand_2 = rpn_stack.pop();
                if (rpn_stack.isEmpty()) {
                    Log.d(tag_error, "missing operand");
                    return msg_not_valid;
                }
                String operand_1 = rpn_stack.pop();

                if (isDouble(operand_1) || isDouble(operand_2)) {
                    rpn_stack.push(doubleCalc(token, operand_1, operand_2));
                } else {
                    rpn_stack.push(intCalc(token, operand_1, operand_2));
                }
            } else {
                rpn_stack.push(token);
            }
        }
        return rpn_stack.pop();
    }

    private String intCalc(String token, String operand_1, String operand_2) {
        if (notNumber(operand_1) || notNumber(operand_2)) {
            Log.d(tag_error, "expect operand not operator");
            return msg_not_valid;
        }

        int int_op_1 = Integer.valueOf(operand_1);
        int int_op_2 = Integer.valueOf(operand_2);
        int int_result = 0;
        switch (token) {
            case "-":
                int_result = int_op_1 - int_op_2;
                break;
            case "+":
                int_result = int_op_1 + int_op_2;
                break;
            case "*":
                int_result = int_op_1 * int_op_2;
                break;
            case "/":
                if (int_op_2 == 0) {
                    Log.d(tag_error, "divide 0");
                    return msg_divide_0;
                }
                int_result = int_op_1 / int_op_2;
                if (int_result * int_op_2 != int_op_1)
                    return doubleCalc(token, operand_1, operand_2);
                break;
            default:
                break;
        }
        return String.valueOf(int_result);
    }

    private String doubleCalc(String token, String operand_1, String operand_2) {
        if (notNumber(operand_1) || notNumber(operand_2)) {
            Log.d(tag_error, "expect operand not operator");
            return msg_not_valid;
        }

        double double_op_1 = Double.valueOf(operand_1);
        double double_op_2 = Double.valueOf(operand_2);
        double double_result = 0;
        switch (token) {
            case "-":
                double_result = double_op_1 - double_op_2;
                break;
            case "+":
                double_result = double_op_1 + double_op_2;
                break;
            case "*":
                double_result = double_op_1 * double_op_2;
                break;
            case "/":
                if (double_op_2 == 0) {
                    Log.d(tag_error, "divide 0");
                    return msg_divide_0;
                }
                double_result = double_op_1 / double_op_2;
                break;
            default:
                break;
        }
        return String.valueOf(double_result);
    }

    private Queue<String> shuntingYardAlgo(String[] input) {
        Queue<String> output_queue = new LinkedList<>();
        Stack<String> operator_stack = new Stack<>();
        for (String token : input) {
            if (!isOperator(token)) {
                // number
                output_queue.offer(token);
            } else {
                // operator A
                // While there is an operator B of higher or equal precidence than A at the top
                // of the stack, pop B off the stack and append it to the output.
                while (!operator_stack.isEmpty() && compareOperator(token, operator_stack.peek())
                        <= 0) {
                    String str_pop = operator_stack.pop();
                    output_queue.add(str_pop);
                }
                operator_stack.push(token);
            }
        }
        // Pop the operator on the top of the stack, and append it to the output.
        while (!operator_stack.isEmpty()) {
            String str_pop = operator_stack.pop();
            output_queue.add(str_pop);
        }
        return output_queue;
    }

    private boolean isDouble(String operand) {
        for (char s : operand.toCharArray()) {
            if (s == '.')
                return true;
        }
        return false;
    }

    private int compareOperator(String str_left, String str_right) {
        int token_left = str_left.equals("+") || str_left.equals("-") ? 0 : 1;
        int token_right = str_right.equals("+") || str_right.equals("-") ? 0 : 1;
        return token_left - token_right; // 0 is same level, -1 is below, 1 is higher
    }

    private boolean isOperator(String str) {
        String[] operators = {"-", "+", "*", "/"};
        for (String operator : operators) {
            if (str.equals(operator))
                return true;
        }
        return false;
    }

    private boolean isPercent(String str) {
        return !str.isEmpty() && str.charAt(str.length() - 1) == '%';
    }

    private boolean isValidMath(String str) {
        // check - + * /
        if (isOperator(str))
            return true;
        // if number 1, 2.0, 3%
        if (notNumber(str))
            return false;

        // check number of . %
        int countDot = 0;
        int countPercent = 0;
        for (char ch : str.toCharArray()) {
            if (ch == '.')
                ++countDot;
            else if (ch == '%')
                ++countPercent;
        }
        if (countDot > 1 || countPercent > 1)
            return false;

        // only .
        if (countDot == 1 && str.length() == 1)
            return false;

        if (countPercent == 1) {
            // only %
            if (str.length() == 1)
                return false;
            // % must in the end
            if (str.charAt(str.length() - 1) != '%')
                return false;
        }

        return true;
    }
}