package com.jhlkdz.temperatremeasure.widget;

import android.graphics.Path;

public class Quadrangle {

    private Dot first;
    private Dot second;
    private Dot third;
    private Dot fourth;

    private Path path;

    public Quadrangle(Dot first, Dot second, Dot third, Dot fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        initPath();
    }

    private void initPath(){
        Path temp = new Path();
        temp.moveTo(first.getX(),first.getY());
        temp.lineTo(second.getX(),second.getY());
        temp.lineTo(third.getX(),third.getY());
        temp.lineTo(fourth.getX(),fourth.getY());
        temp.lineTo(first.getX(),first.getY());
        this.path = temp;
    }

    public float getCenterY(){
        return (first.getY()+fourth.getY())/2.0f;
    }

    public float getCenterX(){
        return (first.getX()+second.getX())/2.0f;
    }

    public Path getPath() {
        return path;
    }

    public Dot getFirst() {
        return first;
    }

    public Dot getSecond() {
        return second;
    }

    public Dot getThird() {
        return third;
    }

    public Dot getFourth() {
        return fourth;
    }
}
