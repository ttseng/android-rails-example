package com.mgm.phobos;

import java.io.Serializable;

public class myProjects implements Serializable {
    public String name  = "";
  
    public String getProjectName(){
        return name;
    }
    public void setEventName(String name){
        this.name = name;
    }
}
