package vn.dating.common.community;

import java.util.ArrayList;
import java.util.List;

public class OtherCommunity {
    public static List<String> getList(){
        List<String> communities = new ArrayList<>();
        communities.add("news");
        communities.add("relax");
        communities.add("ask");
        communities.add("drama");
        return  communities;
    }

    public static boolean isOther(String communityName){

        List<String> communities = new ArrayList<>();
        communities.add("news");
        communities.add("relax");
        communities.add("ask");
        communities.add("drama");

        return communities.contains(communityName);
    }
}
