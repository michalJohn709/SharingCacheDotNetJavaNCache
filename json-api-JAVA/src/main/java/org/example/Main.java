package org.example;

public class Main {
    public static void main(String[] args)
    {
        try
        {
            BasicOperation.run();
        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }
    }
}