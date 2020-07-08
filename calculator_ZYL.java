//一、实验实习目的及要求
//加强对Java的深入理解，达到提高学生分析问题，解决综合问题的能力。
//
//
//二、实验实习设备（环境）及要求（软硬件条件）
//设备：PC机
//操作系统：macOS
//编译软件：IntelliJ IDEA
//
//
//三、实验实习内容简介：
//
//系统应实现的主要功能：
//
//1、基础计算功能：加减乘除、取反
//2、进阶计算功能：取余、取倒数、开根号、取对数等
//3、基于awt和swing的GUI设计
//　　
//各模块的具体功能和简单算法：
//
//1、计算器的各个组件间的组合使用嵌套的方法，数字和运算符由按钮组成，由网格布局GridLayout组成，
// 整体则由边界布局BorderLayout组成，方便对界面的调整而不影响布局
//2、处理数字使用的是算法是：如果点击数字，则使用字符串拼接显示在文本框中；
// 如果点击小数点，则加入先判断之前有无输入过小数点，如果没有则使用字符串拼接显示在文本框中；
// 设置一个flag判断是否是第一个数字，是则直接显示，不是则拼接
//3、处理运算符使用的算法是：先将运算符传入一个函数中，使用switch-case功能，将每个运算符所做的运算与其对应，
// 并且如果不是=号等需要直接运算的符号，则显示在数字后面，方便检查是否输入错误
//
//
//四、源程序（代码）


package calculator;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;


//在初步设计的过程中，我首先想到使用java中内置的awt和swing进行编程。
// 之前在课程中，了解到eclipse和idea都是包含GUI界面设计器的，而且可以设计的比较美观，
// 但初步使用后，发现并不是很好使用，不清楚设计的具体使用、如何连接等，
// 总之学习成本比较大，所以放弃了这个可能会比较好用的方法，还是使用代码进行设计。
//设计时，我参考了给出的图片和手机上使用的计算器，权衡后，我选择了使用如下布局：
// 上方显示文本，中间显示数字和简单的运算符，左侧显示相对复杂的运算符。以此为基础，开始编程。


public class calculator_ZYL extends JFrame implements ActionListener {

    //按键上的字
    //数字和简单运算
    private final String[] simple =
            {
                    "AC", "CE", "Del", "÷",
                    "7", "8", "9", "x",
                    "4", "5", "6", "-",
                    "1", "2", "3", "+",
                    "±", "0", ".", "="
            };

    //复杂运算
    private final String[] complex =
            {
                    "%", "√", "^", "1/X", "ln", "ₕlog", "π", "!",
                    "sin", "cos", "tan", "arcsin", "arccos", "arctan"
            };

    //按键
    CircleButton[] buttons1 = new CircleButton[simple.length];
    CircleButton[] buttons2 = new CircleButton[complex.length];
    JTextField display = new JTextField("计算器--ZYL");


    public calculator_ZYL() {
        try {
            //字体
            Font font1 = new Font("黑体", Font.BOLD, 30);
            Font font2 = new Font("黑体", Font.BOLD, 20);

            //文本框
            JPanel pnlHead = new JPanel(new BorderLayout());
            pnlHead.add(display, BorderLayout.NORTH);
            display.setFont(font1);
            //不可编辑
            display.setEditable(false);

            //主体基础按键
            int r1 = 5;
            int c1 = 4;
            JPanel pnlMainBody = new JPanel(new GridLayout(r1, c1));

            for (int i = 0; i < simple.length; i++) {
                buttons1[i] = new CircleButton(simple[i]);
                buttons1[i].setFont(font2);
                pnlMainBody.add(buttons1[i]);
            }

            //额外复杂按键
            int r2 = 7;
            int c2 = 2;
            JPanel pnlExBody = new JPanel(new GridLayout(r2, c2));

            for (int i = 0; i < complex.length; i++) {
                buttons2[i] = new CircleButton(complex[i]);
                buttons2[i].setFont(font2);
                pnlExBody.add(buttons2[i]);
            }

            //边界布局的分布
            //布局嵌套
            getContentPane().setLayout(new BorderLayout());

            getContentPane().add("North", pnlHead);
            getContentPane().add("Center", pnlMainBody);
            getContentPane().add("West", pnlExBody);

            //注册监听器
            //不加的话，按键和功能没连接，按了没有反应
            for (int i = 0; i < simple.length; i++) {
                buttons1[i].addActionListener(this);
            }

            for (int i = 0; i < complex.length; i++) {
                buttons2[i].addActionListener(this);
            }

            display.addActionListener(this);

            //界面设置
            //可以关闭
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            //界面大小
            int height = 400;
            int width = 500;
            setSize(width, height);
            //页面可见
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            display.setText("出现了未知错误");
        }
    }


    //按键反馈主体函数
    //重写actionPerformed方法
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String cmd = e.getActionCommand();
            //点击三个删除按键时
            if (cmd.equals(simple[0]) || cmd.equals(simple[1]) || cmd.equals(simple[2])) {
                handleClear(cmd);
            } else if ("0123456789.".contains(cmd)) {
                handleNumber(cmd);
            } else {
                //这里比较繁琐，因为涉及到的运算只需要一个数字运算，所以要特殊处理
                if (cmd.equals("√") || cmd.equals("1/X") || cmd.equals("±") || cmd.equals("ln") || cmd.equals("sin")
                        || cmd.equals("cos") || cmd.equals("tan") || cmd.equals("arcsin") || cmd.equals("arccos")
                        || cmd.equals("arctan") || cmd.equals("π") || cmd.equals("!")) {
                    operator = cmd;
                    cmd = "=";
                }
                handleOperator(cmd);
                if (!cmd.equals("="))
                    display.setText(display.getText() + cmd);
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
            display.setText("出现了运算错误");
        } catch (Exception ex) {
            ex.printStackTrace();
            display.setText("出现了未知错误");
        }
    }


    //重要变量
    //判断是否是第一个数字，是则直接显示，不是则拼接
    boolean isFirstDigit = true;
    double number = 0.0;
    String operator = "=";

    private void handleClear(String cmd) {
        try {
            switch (cmd) {
                //点击清零的函数设计最简单，只需要先将显示的文本框设置为0，再将小数点flag和运算符设为初始值即可
                case "AC":
                    display.setText("0");
                    number = 0;
                    isFirstDigit = true;
                    operator = "=";
                    break;
                //清除输入键因为要保持之前输入的数字和运算符，所以注意不要重置
                case "CE":
                    display.setText("0");
                    isFirstDigit = true;
                    break;
                //删除键则使用substring方法不断清除最后一位，但是要注意删到最后，要将flag重置，并显示为0
                case "Del":
                    display.setText(display.getText().substring(0, display.getText().length() - 1));
                    if (display.getText().length() == 0) {
                        isFirstDigit = true;
                        display.setText("0");
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + cmd);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            display.setText("出现了运算错误");
        } catch (Exception e) {
            e.printStackTrace();
            display.setText("出现了未知错误");
        }
    }


    //数字处理
    //点击数字的函数设计时，如果输入的是数字，只需要使用字符串拼接，就能完成，但是在设计小数点时要特别注意，
    // 因为一个数字只能有一个小数点，输入第一个小数点时，如果之前输入的不包含小数点，则使用字符串拼接即可。
    // 并且设置一个flag判断是否是第一个数字，是则直接显示，不是则拼接，不过也要注意要及时重新设置flag
    private void handleNumber(String cmd) {
        try {
            if (isFirstDigit) {
                display.setText(cmd);
            }
            //输入的不是小数点
            else if (!cmd.equals(".")) {
                display.setText(display.getText() + cmd);
            }
            //输入的为小数点，且之前输入的不包含小数点
            else if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
            isFirstDigit = false;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            display.setText("出现了运算错误");
        } catch (Exception e) {
            e.printStackTrace();
            display.setText("出现了未知错误");
        }
    }


    //运算符处理
    public void handleOperator(String cmd) {
        try {
            double inputValue;
            inputValue = Double.parseDouble(display.getText());
            double r;
            switch (operator) {
                //加法
                case "+":
                    number += inputValue;
                    display.setText(String.valueOf(number));
                    break;
                //减法
                case "-":
                    number -= inputValue;
                    display.setText(String.valueOf(number));
                    break;
                //乘法
                case "x":
                    number *= inputValue;
                    display.setText(String.valueOf(number));
                    break;
                //除法
                case "÷":
                    if (inputValue == 0)
                        display.setText("ERROR!除零错误");
                    else {
                        number /= inputValue;
                        display.setText(String.valueOf(number));
                    }
                    break;
                //取余
                case "%":
                    if (inputValue == 0)
                        display.setText("ERROR!除零错误");
                    else {
                        number %= inputValue;
                        display.setText(String.valueOf(number));
                    }
                    break;
                //根号
                case "√":
                    if (inputValue < 0) {
                        display.setText("ERROR!负数错误");
                    } else {
                        number = Math.sqrt(inputValue);
                        display.setText(String.valueOf(number));
                    }
                    break;
                //阶乘
                case "^":
                    number = Math.pow(number, inputValue);
                    display.setText(String.valueOf(number));
                    break;
                //倒数
                case "1/X":
                    if (inputValue == 0) {
                        display.setText("ERROR!除零错误");
                    } else {
                        number = 1.0 / inputValue;
                        display.setText(String.valueOf(number));
                    }
                    break;
                //自然倒数为底导数
                case "ln":
                    if (inputValue < 0) {
                        display.setText("ERROR!负数错误");
                    } else {
                        number = Math.log(inputValue);
                        display.setText(String.valueOf(number));
                    }
                    break;
                //导数
                case "ₕlog":
                    if (!(inputValue > 0) || !(number > 0)) {
                        display.setText("ERROR!非正数错误");
                    } else {
                        number = Math.log(inputValue) / Math.log(number);
                        display.setText(String.valueOf(number));
                    }
                    break;
                //数字乘上π
                case "π":
                    number = inputValue * Math.PI;
                    display.setText(String.valueOf(number));
                    break;
                //阶乘
                case "!":
                    number = 1;
                    if (inputValue < 0) {
                        display.setText("ERROR!负数错误");
                    }
                    if (inputValue == 0) {
                        number = 0;
                        display.setText(String.valueOf(number));
                    }
                    for (int i = 1; i <= inputValue; i++) {
                        number *= i;
                    }
                    display.setText(String.valueOf(number));
                    break;
                //三角函数在设计时，本来打算用常规的那种先输入三角函数，再输入数字，但考虑到我之前为了运算，会在没输入数字，
                // 首先点按键时，把数字设置为0，再单独设置太过麻烦，就设置成了和取倒数和ln一样先输入数字，再点击三角函数的形式，
                //而且也方便了完成括号内的运算再用三角函数
                case "sin":
                    r = Math.toRadians(inputValue);
                    number = Math.sin(r);
                    display.setText(String.valueOf(number));
                    break;
                case "cos":
                    r = Math.toRadians(inputValue);
                    number = Math.cos(r);
                    display.setText(String.valueOf(number));
                    break;
                case "tan":
                    r = Math.toRadians(inputValue);
                    number = Math.tan(r);
                    display.setText(String.valueOf(number));
                    break;
                case "arcsin":
                    if (inputValue < -0.5 * Math.PI || inputValue > 0.5 * Math.PI) {
                        display.setText("ERROR!范围错误");
                    } else {
                        r = Math.toRadians(inputValue);
                        number = Math.asin(r);
                        display.setText(String.valueOf(number));
                    }
                    break;
                case "arccos":
                    if (inputValue < 0 || inputValue > Math.PI) {
                        display.setText("ERROR!范围错误");
                    } else {
                        r = Math.toRadians(inputValue);
                        number = Math.acos(r);
                        display.setText(String.valueOf(number));
                    }
                    break;
                case "arctan":
                    if (inputValue < -0.5 * Math.PI || inputValue > 0.5 * Math.PI) {
                        display.setText("ERROR!范围错误");
                    } else {
                        r = Math.toRadians(inputValue);
                        number = Math.atan(r);
                        display.setText(String.valueOf(number));
                    }
                    break;
                case "±":
                    number = inputValue * -1;
                    display.setText(String.valueOf(number));
                    break;
                case "=":
                    number = inputValue;
                    display.setText(String.valueOf(number));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + operator);
            }

            operator = cmd;
            isFirstDigit = true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            display.setText("出现了运算错误");
        }
        //这个问题原因是直接点击运算符，但文本框内的"计算器--周宇龙"还在
        catch (java.lang.NumberFormatException e) {
            e.printStackTrace();
            display.setText("0.0");
        } catch (Exception e) {
            e.printStackTrace();
            display.setText("出现了未知错误");
        }
    }


    public static void main(String[] args) {
        try {
            //简单的方法
//            new calculator_ZYL();

            //网上找的复杂方法
            //请求事件分发线程，保证GUI在事件分发线程中创建，不能直接调用
            //lambda表达式
            SwingUtilities.invokeLater(() -> {
                new calculator_ZYL();
            });

            //这个是编译器建议的方法，不太清楚原理，简洁些
//        SwingUtilities.invokeLater(calculator.calculator_ZYL::new);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


//这次项目是我首次使用Java实现这样比较大的任务，总共约500行。
// 在初步设计时，参考了些别人写的代码，各有各的特色，最终我找到了一个比较简洁的模版进行改写。
//通过该项目，设计时，我深切体会到了那些编写规范带给人的舒适感，函数的设计可以极大的方便代码的阅读，也可以方便分开来编写和调试。
// 设计时可以把代码分成界面布局、按键交互两大部分，具体实现的过程在程序调试过程中有详细写出。具体实现环节，是非常考验基本功的，
// 而且GUI好像不能使用debug调试，需要在GUI里直接调试，不是非常明显，而且计算器的运算符交互也费了我不少功夫。
// 总而言之，通过长时间的编程后，基本上还是达到了预期结果。
// 最主要的不足是Java的GUI设计并不是非常方便和美观，对比安卓手机上使用的计算器，复杂度和流畅度还是比不上的，也没那么多时间把那么多功能都实现。
