package group_project.cis350.upenn.edu.a350_group_project;

/**
 * Created by johnquinn on 2/15/17.
 */

import java.util.ArrayList;
import java.util.List;

public class Group {

    public String string;
    public final List<String> children = new ArrayList<String>();

    public Group(String string) {
        this.string = string;
    }

}