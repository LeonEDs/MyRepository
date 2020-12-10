package com.demo.MainTest;

public class Singleton
{
    private static Singleton instance;

    private Singleton name;

    private Singleton()
    {
        this.name = getInstance();
    }

    public static Singleton getInstance()
    {
        if (instance == null)
        {
            instance = new Singleton();
            return instance;
        }
        return instance;
    }

    public Singleton getName()
    {
        return name;
    }
}
