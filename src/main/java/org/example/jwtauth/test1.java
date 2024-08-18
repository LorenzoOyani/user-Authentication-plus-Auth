package org.example.jwtauth;

import java.io.Serializable;

public class test1 {

    public static String replace(String name, String oldName, String newName) {
        if(hasLength(name) && hasLength(oldName) && hasLength(newName)){
            int index = name.indexOf(oldName);
            if(index == -1){
                return name;
            }

            int capacity = 0;
            if(newName.length() > oldName.length()){
                capacity +=18;
            }
            StringBuilder sb = new StringBuilder(capacity);
            int sub_index = 0;
            for(int oldNameLen = oldName.length(); index > 0; index = name.indexOf(oldName, sub_index)){
                sub_index = sub_index+oldNameLen;
                sb.append(name, sub_index,index);
                sb.append(newName);
            }
            sb.append(name, sub_index,  name.length());
            return  sb.toString();
        }else{
            return name;
        }
    }


    private static boolean hasLength(String str) {
        return str != null && !str.isEmpty();
    }

}


