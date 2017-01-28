package com.mlctrez.tlmock;

import javax.swing.*;
import java.awt.*;


public class TlMock extends JPanel implements Runnable {


    // everything in one file makes it easier to write somewhat portable
    // code that can be cut from here and pasted into a C codebase

    static final int width = 1200;
    static final int height = 520;

    static final int rows = 4;
    static final int columns = 16;

    static final int dotSize = 10;
    static final int hSpace = 20;
    static final int vSpace = 30;

    static int[] top_buff = new int[70];
    static int[] bot_buff = new int[70];

    void drive(final int[] buff, final int start, final int finish, final int row, final int crow) {

        int col = 0;
        int ccol = 0;

        for (int sbb = start; sbb < finish; sbb++) {
            int data = buff[sbb];

            for (int shiftit = 0; shiftit < 16; shiftit++) {
                int db = data & 0xC0000000;

                if (db == 0x00000000) setDot(col, row, ccol, crow, Color.BLACK);
                if (db == 0x80000000) setDot(col, row, ccol, crow, Color.RED);
                if (db == 0x40000000) setDot(col, row, ccol, crow, Color.GREEN);
                if (db == 0xC0000000) setDot(col, row, ccol, crow, Color.ORANGE);

                ccol++;
                data = data << 2;
                if (ccol > 4) {
                    ccol = 0;
                    col++;
                }
            }
        }
    }

    public void run() {
        while (true) {
            // read top and bot buffers and draw correct dots

            int d = 0;
            for (int r = 0; r < 7; r++) {
                drive(top_buff, d, d + 5, 1, r);
                d = d + 5;
                drive(top_buff, d, d + 5, 0, r);
                d = d + 5;
            }

            d = 0;
            for (int r = 0; r < 7; r++) {
                drive(bot_buff, d, d + 5, 3, r);
                d = d + 5;
                drive(bot_buff, d, d + 5, 2, r);
                d = d + 5;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }

        }
    }

    void drawBase() {

        Graphics g = getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);


        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                int x = hSpace * 2 + c * (hSpace + dotSize * 5);
                int y = vSpace * 2 + r * (vSpace + dotSize * 7);
                int fx = 5 * dotSize + 1;
                int fy = 7 * dotSize + 1;
                g.setColor(new Color(0x30, 0x30, 0x30));

                g.fillRect(x, y, fx, fy);

                g.setColor(Color.BLACK);

                for (int cc = 0; cc < 5; cc++) {
                    for (int cr = 0; cr < 7; cr++) {
                        g.fillOval(x + cc * dotSize, y + cr * dotSize, dotSize, dotSize);
                    }
                }
            }
        }
    }

    void setDot(int col, int row, int ccol, int crow, Color color) {
        int x = hSpace * 2 + col * (hSpace + dotSize * 5);
        int y = vSpace * 2 + row * (vSpace + dotSize * 7);
        Graphics g = getGraphics();
        g.setColor(color);
        g.fillOval(x + ccol * dotSize, y + crow * dotSize, dotSize, dotSize);
    }

    static byte[] font = new byte[]{
            0x00, 0x00, 0x00, 0x00, 0x00,// (space)
            0x00, 0x00, 0x5F, 0x00, 0x00,// !
            0x00, 0x07, 0x00, 0x07, 0x00,// "
            0x14, 0x7F, 0x14, 0x7F, 0x14,// #
            0x24, 0x2A, 0x7F, 0x2A, 0x12,// $
            0x23, 0x13, 0x08, 0x64, 0x62,// %
            0x36, 0x49, 0x55, 0x22, 0x50,// &
            0x00, 0x05, 0x03, 0x00, 0x00,// '
            0x00, 0x1C, 0x22, 0x41, 0x00,// (
            0x00, 0x41, 0x22, 0x1C, 0x00,// )
            0x08, 0x2A, 0x1C, 0x2A, 0x08,// *
            0x08, 0x08, 0x3E, 0x08, 0x08,// +
            0x00, 0x50, 0x30, 0x00, 0x00,// ,
            0x08, 0x08, 0x08, 0x08, 0x08,// -
            0x00, 0x60, 0x60, 0x00, 0x00,// .
            0x20, 0x10, 0x08, 0x04, 0x02,// /
            0x3E, 0x51, 0x49, 0x45, 0x3E,// 0
            0x00, 0x42, 0x7F, 0x40, 0x00,// 1
            0x42, 0x61, 0x51, 0x49, 0x46,// 2
            0x21, 0x41, 0x45, 0x4B, 0x31,// 3
            0x18, 0x14, 0x12, 0x7F, 0x10,// 4
            0x27, 0x45, 0x45, 0x45, 0x39,// 5
            0x3C, 0x4A, 0x49, 0x49, 0x30,// 6
            0x01, 0x71, 0x09, 0x05, 0x03,// 7
            0x36, 0x49, 0x49, 0x49, 0x36,// 8
            0x06, 0x49, 0x49, 0x29, 0x1E,// 9
            0x00, 0x36, 0x36, 0x00, 0x00,// :
            0x00, 0x56, 0x36, 0x00, 0x00,// ;
            0x00, 0x08, 0x14, 0x22, 0x41,// <
            0x14, 0x14, 0x14, 0x14, 0x14,// =
            0x41, 0x22, 0x14, 0x08, 0x00,// >
            0x02, 0x01, 0x51, 0x09, 0x06,// ?
            0x32, 0x49, 0x79, 0x41, 0x3E,// @
            0x7E, 0x11, 0x11, 0x11, 0x7E,// A
            0x7F, 0x49, 0x49, 0x49, 0x36,// B
            0x3E, 0x41, 0x41, 0x41, 0x22,// C
            0x7F, 0x41, 0x41, 0x22, 0x1C,// D
            0x7F, 0x49, 0x49, 0x49, 0x41,// E
            0x7F, 0x09, 0x09, 0x01, 0x01,// F
            0x3E, 0x41, 0x41, 0x51, 0x32,// G
            0x7F, 0x08, 0x08, 0x08, 0x7F,// H
            0x00, 0x41, 0x7F, 0x41, 0x00,// I
            0x20, 0x40, 0x41, 0x3F, 0x01,// J
            0x7F, 0x08, 0x14, 0x22, 0x41,// K
            0x7F, 0x40, 0x40, 0x40, 0x40,// L
            0x7F, 0x02, 0x04, 0x02, 0x7F,// M
            0x7F, 0x04, 0x08, 0x10, 0x7F,// N
            0x3E, 0x41, 0x41, 0x41, 0x3E,// O
            0x7F, 0x09, 0x09, 0x09, 0x06,// P
            0x3E, 0x41, 0x51, 0x21, 0x5E,// Q
            0x7F, 0x09, 0x19, 0x29, 0x46,// R
            0x46, 0x49, 0x49, 0x49, 0x31,// S
            0x01, 0x01, 0x7F, 0x01, 0x01,// T
            0x3F, 0x40, 0x40, 0x40, 0x3F,// U
            0x1F, 0x20, 0x40, 0x20, 0x1F,// V
            0x7F, 0x20, 0x18, 0x20, 0x7F,// W
            0x63, 0x14, 0x08, 0x14, 0x63,// X
            0x03, 0x04, 0x78, 0x04, 0x03,// Y
            0x61, 0x51, 0x49, 0x45, 0x43,// Z
            0x00, 0x00, 0x7F, 0x41, 0x41,// [
            0x02, 0x04, 0x08, 0x10, 0x20,// "\"
            0x41, 0x41, 0x7F, 0x00, 0x00,// ]
            0x04, 0x02, 0x01, 0x02, 0x04,// ^
            0x40, 0x40, 0x40, 0x40, 0x40,// _
            0x00, 0x01, 0x02, 0x04, 0x00,// `
            0x20, 0x54, 0x54, 0x54, 0x78,// a
            0x7F, 0x48, 0x44, 0x44, 0x38,// b
            0x38, 0x44, 0x44, 0x44, 0x20,// c
            0x38, 0x44, 0x44, 0x48, 0x7F,// d
            0x38, 0x54, 0x54, 0x54, 0x18,// e
            0x08, 0x7E, 0x09, 0x01, 0x02,// f
            0x08, 0x14, 0x54, 0x54, 0x3C,// g
            0x7F, 0x08, 0x04, 0x04, 0x78,// h
            0x00, 0x44, 0x7D, 0x40, 0x00,// i
            0x20, 0x40, 0x44, 0x3D, 0x00,// j
            0x00, 0x7F, 0x10, 0x28, 0x44,// k
            0x00, 0x41, 0x7F, 0x40, 0x00,// l
            0x7C, 0x04, 0x18, 0x04, 0x78,// m
            0x7C, 0x08, 0x04, 0x04, 0x78,// n
            0x38, 0x44, 0x44, 0x44, 0x38,// o
            0x7C, 0x14, 0x14, 0x14, 0x08,// p
            0x08, 0x14, 0x14, 0x18, 0x7C,// q
            0x7C, 0x08, 0x04, 0x04, 0x08,// r
            0x48, 0x54, 0x54, 0x54, 0x20,// s
            0x04, 0x3F, 0x44, 0x40, 0x20,// t
            0x3C, 0x40, 0x40, 0x20, 0x7C,// u
            0x1C, 0x20, 0x40, 0x20, 0x1C,// v
            0x3C, 0x40, 0x30, 0x40, 0x3C,// w
            0x44, 0x28, 0x10, 0x28, 0x44,// x
            0x0C, 0x50, 0x50, 0x50, 0x3C,// y
            0x44, 0x64, 0x54, 0x4C, 0x44,// z
            0x00, 0x08, 0x36, 0x41, 0x00,// {
            0x00, 0x00, 0x7F, 0x00, 0x00,// |
            0x00, 0x41, 0x36, 0x08, 0x00,// }
            0x08, 0x08, 0x2A, 0x1C, 0x08,// ->
            0x08, 0x1C, 0x2A, 0x08, 0x08 // <-
    };


    void charAt(int x, int y, int idx, int fgColor, int bgColor) {

        for (int col = 0; col < 5; col++) {
            int ver = font[idx * 5 + col];
            for (int row = 0; row < 7; row++) {
                if ((ver & 0x01) > 0) {
                    dotAt(x * 5 + col, y * 7 + row, fgColor);
                } else {
                    dotAt(x * 5 + col, y * 7 + row, bgColor);
                }
                ver = ver >> 1;
            }
        }
    }

    void dotAt(final int x, final int y, final int rg) {

        int ay = (y > 13) ? y - 14 : y;

        int addToMod = ((ay > 6) ? (ay - 7) * 10 : 5 + (ay * 10)) + x / 16;
        int addrColShift = 30 - (x % 16 * 2);

        if (y > 13) {
            bot_buff[addToMod] &= ~(0b11 << addrColShift);
            if (rg > 0) {
                bot_buff[addToMod] |= (rg & 0b11) << addrColShift;
            }
        } else {
            top_buff[addToMod] &= ~(0b11 << addrColShift);
            if (rg > 0) {
                top_buff[addToMod] |= (rg & 0b11) << addrColShift;
            }
        }
    }

    // everything from here on down is simluator code

    public static void main(String[] args) throws Exception {
        new TlMock().start(args);
    }

    static String binary(int in) {
        String result = Integer.toBinaryString(in);
        while (result.length() < 32) {
            result = "0" + result;
        }
        return result;
    }

    void start(String[] args) throws Exception {
        JFrame frame = new JFrame("Trans-Lux Simulator");
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setVisible(true);

        Thread.sleep(500);

        drawBase();
        Thread t = new Thread(this);
        t.start();

        int cf = 0;
        while (t.isAlive()) {
            int cfi = cf;
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 16; x++) {
                    int clr = cfi % 3 + 1;
                    int bgclr = (y + x) % 4;
                    charAt(x, y, cfi, clr, bgclr);
                    cfi++;
                    if (cfi >= (font.length / 5)) {
                        cfi = 0;
                    }
                }
            }
            Thread.sleep(1000);
            cf++;
            if (cf >= (font.length / 5)) {
                cf = 0;
            }
        }
    }
}
