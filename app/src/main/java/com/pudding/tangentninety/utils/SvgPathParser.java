package com.pudding.tangentninety.utils;

/**
 * Created by Error on 2017/6/28 0028.
 */

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Path.FillType;
import java.text.ParseException;

public class SvgPathParser {
    private static final int TOKEN_ABSOLUTE_COMMAND = 1;
    private static final int TOKEN_RELATIVE_COMMAND = 2;
    private static final int TOKEN_VALUE = 3;
    private static final int TOKEN_EOF = 4;
    private int mCurrentToken;
    private PointF mCurrentPoint = new PointF();
    private int mLength;
    private int mIndex;
    private String mPathString;
    private float mScale;
    public SvgPathParser(float mScale) {
        this.mScale=mScale;
    }

    protected float transformX(float x) {
        return x;
    }

    protected float transformY(float y) {
        return y;
    }

    public Path parsePath(String s) throws ParseException {
        this.mCurrentPoint.set(Float.NaN, Float.NaN);
        this.mPathString = s;
        this.mIndex = 0;
        this.mLength = this.mPathString.length();
        PointF tempPoint1 = new PointF();
        PointF tempPoint2 = new PointF();
        PointF tempPoint3 = new PointF();
        Path p = new Path();
        p.setFillType(FillType.WINDING);
        boolean firstMove = true;

        while(true) {
            while(this.mIndex < this.mLength) {
                char command = this.consumeCommand();
                boolean relative = this.mCurrentToken == 2;
                float y;
                switch(command) {
                    case 'C':
                    case 'c':
                        if(this.mCurrentPoint.x == 0.0F / 0.0) {
                            throw new ParseException("Relative commands require current point", this.mIndex);
                        }

                        while(this.advanceToNextToken() == 3) {
                            this.consumeAndTransformPoint(tempPoint1, relative);
                            this.consumeAndTransformPoint(tempPoint2, relative);
                            this.consumeAndTransformPoint(tempPoint3, relative);
                            p.cubicTo(tempPoint1.x, tempPoint1.y, tempPoint2.x, tempPoint2.y, tempPoint3.x, tempPoint3.y);
                        }

                        this.mCurrentPoint.set(tempPoint3);
                        break;
                    case 'H':
                    case 'h':
                        if(this.mCurrentPoint.x == 0.0F / 0.0) {
                            throw new ParseException("Relative commands require current point", this.mIndex);
                        }

                        for(; this.advanceToNextToken() == 3; p.lineTo(y, this.mCurrentPoint.y)) {
                            y = this.transformX(this.consumeValue());
                            if(relative) {
                                y += this.mCurrentPoint.x;
                            }
                        }

                        this.mCurrentPoint.set(tempPoint1);
                        break;
                    case 'L':
                    case 'l':
                        if(this.mCurrentPoint.x == 0.0F / 0.0) {
                            throw new ParseException("Relative commands require current point", this.mIndex);
                        }

                        while(this.advanceToNextToken() == 3) {
                            this.consumeAndTransformPoint(tempPoint1, relative);
                            p.lineTo(tempPoint1.x, tempPoint1.y);
                        }

                        this.mCurrentPoint.set(tempPoint1);
                        break;
                    case 'M':
                    case 'm':
                        boolean y1 = true;

                        while(this.advanceToNextToken() == 3) {
                            this.consumeAndTransformPoint(tempPoint1, relative && this.mCurrentPoint.x != 0.0F / 0.0);
                            if(y1) {
                                p.moveTo(tempPoint1.x, tempPoint1.y);
                                y1 = false;
                                if(firstMove) {
                                    this.mCurrentPoint.set(tempPoint1);
                                    firstMove = false;
                                }
                            } else {
                                p.lineTo(tempPoint1.x, tempPoint1.y);
                            }
                        }

                        this.mCurrentPoint.set(tempPoint1);
                        break;
                    case 'V':
                    case 'v':
                        if(this.mCurrentPoint.x == 0.0F / 0.0) {
                            throw new ParseException("Relative commands require current point", this.mIndex);
                        }

                        for(; this.advanceToNextToken() == 3; p.lineTo(this.mCurrentPoint.x, y)) {
                            y = this.transformY(this.consumeValue());
                            if(relative) {
                                y += this.mCurrentPoint.y;
                            }
                        }

                        this.mCurrentPoint.set(tempPoint1);
                        break;
                    case 'Z':
                    case 'z':
                        p.close();
                }
            }

            return p;
        }
    }

    private int advanceToNextToken() {
        while(this.mIndex < this.mLength) {
            char c = this.mPathString.charAt(this.mIndex);
            if(97 <= c && c <= 122) {
                return this.mCurrentToken = 2;
            }

            if(65 <= c && c <= 90) {
                return this.mCurrentToken = 1;
            }

            if(48 <= c && c <= 57 || c == 46 || c == 45) {
                return this.mCurrentToken = 3;
            }

            ++this.mIndex;
        }

        return this.mCurrentToken = 4;
    }

    private char consumeCommand() throws ParseException {
        this.advanceToNextToken();
        if(this.mCurrentToken != 2 && this.mCurrentToken != 1) {
            throw new ParseException("Expected command", this.mIndex);
        } else {
            return this.mPathString.charAt(this.mIndex++);
        }
    }

    private void consumeAndTransformPoint(PointF out, boolean relative) throws ParseException {
        out.x = this.transformX(this.consumeValue())*mScale;
        out.y = this.transformY(this.consumeValue())*mScale;
        if(relative) {
            out.x += this.mCurrentPoint.x;
            out.y += this.mCurrentPoint.y;
        }

    }

    private float consumeValue() throws ParseException {
        this.advanceToNextToken();
        if(this.mCurrentToken != 3) {
            throw new ParseException("Expected value", this.mIndex);
        } else {
            boolean start = true;
            boolean seenDot = false;

            int index;
            for(index = this.mIndex; index < this.mLength; ++index) {
                char str = this.mPathString.charAt(index);
                if((48 > str || str > 57) && (str != 46 || seenDot) && (str != 45 || !start)) {
                    break;
                }

                if(str == 46) {
                    seenDot = true;
                }

                start = false;
            }

            if(index == this.mIndex) {
                throw new ParseException("Expected value", this.mIndex);
            } else {
                String var7 = this.mPathString.substring(this.mIndex, index);

                try {
                    float e = Float.parseFloat(var7);
                    this.mIndex = index;
                    return e;
                } catch (NumberFormatException var6) {
                    throw new ParseException("Invalid float value \'" + var7 + "\'.", this.mIndex);
                }
            }
        }
    }
}

