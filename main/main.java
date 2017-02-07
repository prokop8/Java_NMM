package main;

import model.Model;
import view.*;
import controller.controller;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Pelmen on 20.11.2016.
 */
public class main {

    public static void main(String[] args) {

        mainFrame Frame = new mainFrame();
        Model Model = new Model();
        controller Controller = new controller(Frame, Model);



    }

}
