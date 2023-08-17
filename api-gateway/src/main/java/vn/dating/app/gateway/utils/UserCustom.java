package vn.dating.app.gateway.utils;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
public class UserCustom {
    private String id;
    private boolean verify;
    private List<String> roles= new ArrayList<>();
    public void addRole(String role){
        this.roles.add(role);
    }
    public boolean isUser(){
        if(this.roles.contains("user")) return true;
        return false;
    }
    public boolean isAdmin(){
        if(this.roles.contains("admin")) return true;
        return false;
    }
    public boolean roleNotExist(){
        if(this.roles.size()==0) return false;
        return true;
    }
}
