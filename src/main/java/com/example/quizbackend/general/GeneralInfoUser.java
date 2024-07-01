package com.example.quizbackend.general;

import java.util.Date;

public interface GeneralInfoUser {
     Long getid();
     String getusername();
     String getemail();
     String getfirst_name();
     String getmiddle_name();
     String getlast_name();
     Date getdob();
     String getDepartment();
     interface roles{
          String getname();
     }
}
