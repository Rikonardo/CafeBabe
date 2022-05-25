package com.rikonardo.test;

class HelloWorld {
    private String name;

    public static void main(String[] args) {
        new HelloWorld("World").sayHello();
    }

    protected HelloWorld(String name) {
        this.name = name;
    }

    public void sayHello() {
        System.out.println("Hello " + name + "!");
    }
}