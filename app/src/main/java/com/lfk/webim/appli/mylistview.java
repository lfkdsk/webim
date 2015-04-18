package com.lfk.webim.appli;

/**
 * Created by Administrator on 2015/4/12.
 */
public class mylistview {
    //private int ImageId;
    private String name;
    private int number;
    public mylistview(int ImageId,String name,int number)
    {
        //this.ImageId=ImageId;
        this.name=name;
        this.number=number;
    }
    public String getname()
    {
        return name;
    }
//    public int getImageId()
//    {
//        return ImageId;
//    }
    public int getNumber()
    {
        return number;
    }
}

